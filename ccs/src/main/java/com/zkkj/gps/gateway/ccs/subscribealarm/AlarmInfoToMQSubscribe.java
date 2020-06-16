package com.zkkj.gps.gateway.ccs.subscribealarm;

import com.zkkj.gps.gateway.ccs.dto.amqp.RcvMonitorDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.mq.producer.ProducerSubscribe;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author : cyc
 * Date : 2019-06-24
 * 报警信息向mq推送订阅
 */

@Component
public class AlarmInfoToMQSubscribe {

    private Logger logger = LoggerFactory.getLogger(AlarmInfoToMQSubscribe.class);

    public AlarmInfoToMQSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Autowired
    private ProducerSubscribe producerSubscribe;

    @Subscribe
    public void subscribe(AlarmInfoEvent alarmInfoEvent) {
        try {
            TerminalAlarmInfoDto alarmDto = alarmInfoEvent.getAlarmInfo();
            if ((alarmDto.getAlarmType() == AlarmTypeEnum.VIOLATION_AREA && alarmDto.getIsDeliveryArea() == 0) || alarmDto.getAlarmType() != AlarmTypeEnum.VIOLATION_AREA) {
                AlarmInfoSocket alarmInfoSocket = new AlarmInfoSocket(alarmInfoEvent.getAppKey(), alarmDto.getAlarmGroupId(), alarmDto.getAlarmResType(), alarmDto.getIdentity(),
                        alarmDto.getTerminalId(), alarmDto.getCarNum(), alarmDto.getAlarmTime(), alarmDto.getLongitude(), alarmDto.getLatitude(),
                        alarmDto.getAlarmInfo(), alarmDto.getAlarmType(), alarmDto.getRemark());
                //2019-06-20 添加订单编号
                alarmInfoSocket.setDispatchNo(alarmInfoEvent.getAlarmInfo().getDispatchNo());
                alarmInfoSocket.setCorpName(alarmDto.getCorpName());
                alarmInfoSocket.setAlarmValue(alarmDto.getAlarmValue() + "");
                alarmInfoSocket.setConfigValue(alarmDto.getConfigValue() + "");
                alarmInfoSocket.setAlarmCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                String appKey = alarmInfoEvent.getAppKey();
                RcvMonitorDto<AlarmInfoSocket> result = new RcvMonitorDto<>();
                result.setFlag(2);
                result.setData(alarmInfoSocket);
                result.setAppKey(appKey);
                boolean pushMessage = producerSubscribe.produceMessage(appKey, result);
                logger.info("报警消息发布结果：【" + pushMessage + "】；appKey：【" + appKey + "】；result：【" + result.toString() + "】 \n");
            }
        } catch (Exception e) {
            logger.error("AlarmInfoToMQSubscribe.subscribe is error", e);
        }
    }
}
