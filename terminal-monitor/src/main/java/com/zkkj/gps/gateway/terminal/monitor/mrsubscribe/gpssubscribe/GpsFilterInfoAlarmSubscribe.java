package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsFilterInfoEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-07
 * 针对过滤后的点位报警信息订阅，尤其是针对防拆和低电量报警
 */
public class GpsFilterInfoAlarmSubscribe {
    public GpsFilterInfoAlarmSubscribe() {
        EventBus.getDefault().register(this);
    }

    private final Logger logger = LoggerFactory.getLogger(GpsFilterInfoAlarmSubscribe.class);


    @Subscribe
    public void subscribe(GpsFilterInfoEvent gpsFilterInfoEvent) {
        String terminalId = gpsFilterInfoEvent.getTerminalId();
        //获取最新的经纬度信息
        BasicPositionDto basicPositionDto = gpsFilterInfoEvent.getBaseGPSPositionDto().getPoint();
        //获取此终端设备对应的所有报警配置信息
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = GpsListInfoAlarmSubscribe.getTerminalAlarmConfig(terminalId);
        //进行报警算法处理
        if (appKeyAlarmConfigs != null && appKeyAlarmConfigs.size() > 0) {
            for (int i = 0; i < appKeyAlarmConfigs.size(); i++) {
                AppKeyAlarmConfigDto appKeyAlarmConfig = appKeyAlarmConfigs.get(i);
                String appKey = appKeyAlarmConfig.getAppKey();
                AlarmTypeEnum alarmTypeEnum = appKeyAlarmConfig.getAlarmConfig().getAlarmTypeEnum();
                //返回报警信息对象
                if (alarmTypeEnum == AlarmTypeEnum.LOW_POWER || alarmTypeEnum == AlarmTypeEnum.EQUIP_REMOVE) {
                    TerminalAlarmInfoDto alarmInfo = GpsListInfoAlarmSubscribe.terminalMonitor(terminalId, null, basicPositionDto, appKeyAlarmConfig);
                    if (alarmInfo != null) {
                        //产生报警发送通知
                        EventBus.getDefault().post(new AlarmInfoEvent(appKey, alarmInfo));
                        //logger.info("产生报警发送通知,appKey=" + appKey + ";alarmInfo=" + alarmInfo);
                    }
                }
            }
        }
    }
}
