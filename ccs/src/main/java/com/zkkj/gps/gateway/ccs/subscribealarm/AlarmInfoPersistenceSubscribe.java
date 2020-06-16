package com.zkkj.gps.gateway.ccs.subscribealarm;

import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmInfoDbDto;
import com.zkkj.gps.gateway.ccs.service.AlarmInfoService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 产生的报警信息进行持久化
 */
@Component
public class AlarmInfoPersistenceSubscribe {

    private Logger logger = LoggerFactory.getLogger(AlarmInfoPersistenceSubscribe.class);

    public AlarmInfoPersistenceSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Autowired
    private AlarmInfoService alarmInfoService;

    @Subscribe
    public void subscribe(AlarmInfoEvent alarmInfoEvent) {
        try {
            saveAlarmInfo(alarmInfoEvent);
        } catch (Exception e) {
            logger.error("AlarmInfoPersistenceSubscribe.subscribe is error", e);
        }
    }

    /**
     * 持久化报警数据
     *
     * @param alarmInfoEvent
     */
    private void saveAlarmInfo(AlarmInfoEvent alarmInfoEvent) {

        if (alarmInfoEvent != null) {
            AlarmInfoDbDto alarmInfo = converseAlarmInfo(alarmInfoEvent);
            if (alarmInfo != null) {
                alarmInfoService.saveAlarmInfo(alarmInfo);
            }
        }
    }

    /**
     * 报警数据转化
     *
     * @param alarmInfoEvent
     * @return
     */
    private AlarmInfoDbDto converseAlarmInfo(AlarmInfoEvent alarmInfoEvent) {
        String appKey = alarmInfoEvent.getAppKey();
        TerminalAlarmInfoDto alarmInfo = alarmInfoEvent.getAlarmInfo();

        AlarmInfoDbDto alarmInfoDbDto = new AlarmInfoDbDto();
        alarmInfoDbDto.setId(UUID.randomUUID().toString());
        //2019-60-20 添加运单编号
        alarmInfoDbDto.setDispatchNo(alarmInfo.getDispatchNo());
        alarmInfoDbDto.setAlarmGroupId(alarmInfo.getAlarmGroupId());
        alarmInfoDbDto.setAlarmInfo(alarmInfo.getAlarmInfo());
        alarmInfoDbDto.setAlarmTime(alarmInfo.getAlarmTime());
        alarmInfoDbDto.setAlarmType(alarmInfo.getAlarmType().getSeq());
        alarmInfoDbDto.setAlarmValue(alarmInfo.getAlarmValue());
        alarmInfoDbDto.setAppKey(appKey);
        alarmInfoDbDto.setAreaId(alarmInfo.getAreaId());
        alarmInfoDbDto.setCarNum(alarmInfo.getCarNum());
        alarmInfoDbDto.setConfigValue(alarmInfo.getConfigValue());
        alarmInfoDbDto.setIdentity(alarmInfo.getIdentity());
        alarmInfoDbDto.setCorpName(alarmInfo.getCorpName());
        alarmInfoDbDto.setLatitude(alarmInfo.getLatitude());
        alarmInfoDbDto.setLongitude(alarmInfo.getLongitude());
        alarmInfoDbDto.setAlarmConfigId(alarmInfo.getAlarmConfigId());
        alarmInfoDbDto.setRemark(alarmInfo.getRemark());
        alarmInfoDbDto.setIsDeliveryArea(alarmInfo.getIsDeliveryArea());
        if (AlarmResTypeEnum.start == alarmInfo.getAlarmResType()) {
            alarmInfoDbDto.setResType(0);
        }
        if (AlarmResTypeEnum.end == alarmInfo.getAlarmResType()) {
            alarmInfoDbDto.setResType(1);
        }
        alarmInfoDbDto.setTerminalId(alarmInfo.getTerminalId());
        alarmInfoDbDto.setAlarmCreateTime(DateTimeUtils.getCurrentDateTimeStr());
        alarmInfoDbDto.setCreateTime(DateTimeUtils.getCurrentDateTimeStr());
        // TODO: 2019-05-17 创建人来源
        // alarmInfoDbDto.setCreateBy();
        return alarmInfoDbDto;
    }
}
