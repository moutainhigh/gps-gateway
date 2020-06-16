package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe;

import com.zkkj.gps.gateway.terminal.monitor.algorithm.TerminalAlarmAlgorithm;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsListInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 根据经纬度产生报警信息
 */
public class GpsListInfoAlarmSubscribe {

    public GpsListInfoAlarmSubscribe() {
        EventBus.getDefault().register(this);
    }

    private final Logger logger = LoggerFactory.getLogger(GpsListInfoAlarmSubscribe.class);

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void subscribe(GpsListInfoEvent gpsInfoEvent) {
        String terminalId = gpsInfoEvent.getTerminalId();
        QueueList<BasicPositionDto> hisListPosition = gpsInfoEvent.getHisListPosition();
        //获取此终端设备对应的所有报警配置信息
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = getTerminalAlarmConfig(terminalId);
        QueueList<BasicPositionDto> tempListPosition = hisListPosition;
        //进行报警算法处理
        if (appKeyAlarmConfigs != null && appKeyAlarmConfigs.size() > 0) {
            //去除非监控报警区域
            for (int i = 0; i < appKeyAlarmConfigs.size(); i++) {
                if (appKeyAlarmConfigs.get(i).getAlarmConfig().getAlarmTypeEnum().getSeq() == 0) {
                    for (BasicPositionDto basicPositionDto : hisListPosition) {
                        //判断单个点是否在区域中
                        if (GPSPositionUtil.checkSingleInArea(basicPositionDto, appKeyAlarmConfigs.get(i).getAlarmConfig().getArea())) {
                           return;
                        }
                    }
                }
            }
            for (int i = 0; i < appKeyAlarmConfigs.size(); i++) {
                AppKeyAlarmConfigDto appKeyAlarmConfig = appKeyAlarmConfigs.get(i);
                String appKey = appKeyAlarmConfig.getAppKey();
                //返回报警信息对象
                AlarmTypeEnum alarmTypeEnum = appKeyAlarmConfig.getAlarmConfig().getAlarmTypeEnum();
                if (alarmTypeEnum == AlarmTypeEnum.STOP_OVER_TIME || alarmTypeEnum == AlarmTypeEnum.OVER_SPEED ||
                        alarmTypeEnum == AlarmTypeEnum.LINE_OFFSET || alarmTypeEnum == AlarmTypeEnum.VEHICLE_LOAD ||
                        (alarmTypeEnum == AlarmTypeEnum.VIOLATION_AREA && appKeyAlarmConfig.getAlarmConfig().getIsDeliveryArea() == 0)) {
                    TerminalAlarmInfoDto alarmInfo = terminalMonitor(terminalId, tempListPosition, null, appKeyAlarmConfig);
                    if (alarmInfo != null) {
                        //产生报警发送通知
                        EventBus.getDefault().post(new AlarmInfoEvent(appKey, alarmInfo));
                        //logger.info("产生报警发送通知,appKey=" + appKey + ";alarmInfo=" + alarmInfo);
                    }
                }
            }
        }
    }

    /**
     * 进行报警监控处理
     *
     * @param terminalId        终端id
     * @param hisListPosition   最近历史经纬度信息
     * @param basicPositionDto
     * @param appKeyAlarmConfig 报警配置
     * @return
     */
    public static TerminalAlarmInfoDto terminalMonitor(String terminalId, QueueList<BasicPositionDto> hisListPosition, BasicPositionDto basicPositionDto, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        //TODO 报警计算
        return TerminalAlarmAlgorithm.terminalAlarm(terminalId, hisListPosition, basicPositionDto, appKeyAlarmConfig);

    }

    /**
     * 根据终端ID获取此设备所有第三方应用，以及对应公司的报警配置信息
     *
     * @param terminalId 终端ID
     * @return
     */
    public static List<AppKeyAlarmConfigDto> getTerminalAlarmConfig(String terminalId) {
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs;
        //获取终端的所有报警配置信息
        Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
        if (!mapAlarmConfig.containsKey(terminalId)) {
            appKeyAlarmConfigs = null;

        } else {
            appKeyAlarmConfigs = mapAlarmConfig.get(terminalId);
        }
        return appKeyAlarmConfigs;
    }
}
