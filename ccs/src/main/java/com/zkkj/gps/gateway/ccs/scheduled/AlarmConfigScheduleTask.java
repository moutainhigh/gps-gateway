package com.zkkj.gps.gateway.ccs.scheduled;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-05-30
 * 报警配置定时任务
 */

@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class AlarmConfigScheduleTask {

    private Logger logger = LoggerFactory.getLogger(AlarmConfigScheduleTask.class);

    /**
     * 定时任务方法
     */
    public void clearAlarmConfigScheduleRun() {
        try {
            //获取所有的报警配置信息
            Map<String, List<AppKeyAlarmConfigDto>> alarmConfigs = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
            if (MapUtils.isNotEmpty(alarmConfigs)) {
                LocalDateTime currentLocalDateTime = DateTimeUtils.getCurrentLocalDateTime();
                for (Map.Entry<String, List<AppKeyAlarmConfigDto>> entry : alarmConfigs.entrySet()) {
                    String terminalId = entry.getKey();
                    List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = entry.getValue();
                    if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs)) {
                        Iterator<AppKeyAlarmConfigDto> iterator = appKeyAlarmConfigs.iterator();
                        while (iterator.hasNext()) {
                            AppKeyAlarmConfigDto appKeyAlarmConfig = iterator.next();
                            LocalDateTime localDateTime = DateTimeUtils.parseLocalDateTime(appKeyAlarmConfig.getAlarmConfig().getEndTime());
                            if (DateTimeUtils.durationMillis(localDateTime, currentLocalDateTime) > 0) {
                                //推送报警配置信息
                                pushAlarmInfo(appKeyAlarmConfig, terminalId);
                                //清除逾期在缓存中的报警配置信息
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("AlarmConfigScheduleTask.clearAlarmConfigScheduleRun is error", e);
        }

    }

    private void pushAlarmInfo(AppKeyAlarmConfigDto appKeyAlarmConfig, String terminalId) {
        String appKey = appKeyAlarmConfig.getAppKey();
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        //获取终端最新的点位信息
        BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
        //获取缓存中的未结束的报警信息
        Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
        List<TerminalAlarmInfoDto> terminalAlarmInfos = terminalAlarmCache.get(terminalId) == null ? Lists.newArrayList() : terminalAlarmCache.get(terminalId);
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfos.stream().filter(s -> s.getAppKey().equals(appKey) &&
                s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId())
                && s.getAlarmResType() == AlarmResTypeEnum.start).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            for (TerminalAlarmInfoDto terminalAlarm : collect) {
                //设置报警值
                if (terminalAlarm.getAlarmType().equals(AlarmTypeEnum.VIOLATION_AREA) || terminalAlarm.getAlarmType().equals(AlarmTypeEnum.OFF_LINE)
                        || terminalAlarm.getAlarmType().equals(AlarmTypeEnum.LINE_OFFSET)) {
                    double durationMinutes = DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(terminalAlarm.getAlarmTime()), DateTimeUtils.getCurrentLocalDateTime());
                    terminalAlarm.setAlarmValue(durationMinutes);
                }
                terminalAlarm.setAlarmTime(DateTimeUtils.getCurrentDateTimeStr());
                terminalAlarm.setAlarmInfo("系统时间已经大于该次报警配置结束时间，于【" + DateTimeUtils.getCurrentDateTimeStr() + "】结束报警,");
                terminalAlarm.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarm.setLongitude(baseGPSPositionDto == null ? 0.0 : baseGPSPositionDto.getPoint() == null ? 0.0 : baseGPSPositionDto.getPoint().getLongitude());
                terminalAlarm.setLatitude(baseGPSPositionDto == null ? 0.0 : baseGPSPositionDto.getPoint() == null ? 0.0 : baseGPSPositionDto.getPoint().getLatitude());
                EventBus.getDefault().post(new AlarmInfoEvent(terminalAlarm.getAppKey(), terminalAlarm));
                terminalAlarmInfos.remove(terminalAlarm);
                logger.info("报警配置结束时间超过当前系统时间，产生终止为结束的报警发送通知,appKey===============" + terminalAlarm.getAppKey() + ";alarmInfo=" + terminalAlarm);
            }
        }
    }
}
