package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsSpecialListInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 根据经纬度产生报警信息，只针对进出区域
 */
public class GpsSpecialListInfoAlarmSubscribe {

    public GpsSpecialListInfoAlarmSubscribe() {
        EventBus.getDefault().register(this);
    }

    private final Logger logger = LoggerFactory.getLogger(GpsSpecialListInfoAlarmSubscribe.class);

    @Subscribe
    public void subscribe(GpsSpecialListInfoEvent gpsSpecialListInfoEvent) {
        String terminalId = gpsSpecialListInfoEvent.getTerminalId();
        QueueList<BasicPositionDto> hisListPosition = gpsSpecialListInfoEvent.getHisListPosition();
        //获取此终端设备对应的所有报警配置信息
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = GpsListInfoAlarmSubscribe.getTerminalAlarmConfig(terminalId);
        //进行报警算法处理
        if (appKeyAlarmConfigs != null && appKeyAlarmConfigs.size() > 0) {
            for (int i = 0; i < appKeyAlarmConfigs.size(); i++) {
                AppKeyAlarmConfigDto appKeyAlarmConfig = appKeyAlarmConfigs.get(i);
                String appKey = appKeyAlarmConfig.getAppKey();
                //返回报警信息对象
                AlarmTypeEnum alarmTypeEnum = appKeyAlarmConfig.getAlarmConfig().getAlarmTypeEnum();
                if(alarmTypeEnum == AlarmTypeEnum.VIOLATION_AREA && appKeyAlarmConfig.getAlarmConfig().getIsDeliveryArea() == 1){
                    TerminalAlarmInfoDto alarmInfo = GpsListInfoAlarmSubscribe.terminalMonitor(terminalId, hisListPosition, null, appKeyAlarmConfig);
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
