package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmInfoDbDto;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-17
 */
public interface AlarmInfoService {

    /**
     * 持久化报警信息
     * @param alarmInfo
     */
    void saveAlarmInfo(AlarmInfoDbDto alarmInfo);

    /**
     * 获取所有没有结束的报警配置
     * @return
     */
    List<AlarmInfoDbDto> getDisEndAlarmInfo();
}
