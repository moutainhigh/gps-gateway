package com.zkkj.gps.gateway.ccs.subscribealarm;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.websocket.WebSocketAlarmGlobal;
import com.zkkj.gps.gateway.ccs.websocket.WebSocketAlarmInfo;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 用于回调通知各个系统实时产生的报警
 */
@Component
public class AlarmInfoCallbackSubscribe {
    private Logger logger = LoggerFactory.getLogger(AlarmInfoCallbackSubscribe.class);

    public AlarmInfoCallbackSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(AlarmInfoEvent alarmInfoEvent) {
        try {
            //TODO 用于回调通知各个系统实时产生的报警
            TerminalAlarmInfoDto alarmDto = alarmInfoEvent.getAlarmInfo();
            if ((alarmDto.getAlarmType() == AlarmTypeEnum.VIOLATION_AREA && alarmDto.getIsDeliveryArea() == 0) || alarmDto.getAlarmType() != AlarmTypeEnum.VIOLATION_AREA) {
                AlarmInfoSocket alarmInfoEvent1 = new AlarmInfoSocket(alarmInfoEvent.getAppKey(), alarmDto.getAlarmGroupId(), alarmDto.getAlarmResType(), alarmDto.getIdentity(),
                        alarmDto.getTerminalId(), alarmDto.getCarNum(), alarmDto.getAlarmTime(), alarmDto.getLongitude(), alarmDto.getLatitude(),
                        alarmDto.getAlarmInfo(), alarmDto.getAlarmType(), alarmDto.getRemark());
                //2019-06-20 添加订单编号
                if (ObjectUtils.isEmpty(alarmDto.getDispatchNo())) {
                    WebSocketAlarmGlobal.sendMessage(alarmInfoEvent1);
                } else {
                    WebSocketAlarmInfo.sendMessage(alarmInfoEvent1);
                }
            }
        } catch (Exception e) {
            logger.error("AlarmInfoCallbackSubscribe.subscribe is error", e);
        }
    }
}
