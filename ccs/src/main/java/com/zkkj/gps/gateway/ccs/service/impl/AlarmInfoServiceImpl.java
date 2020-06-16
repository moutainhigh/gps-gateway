package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmInfoDbDto;
import com.zkkj.gps.gateway.ccs.mappper.AlarmInfoMapper;
import com.zkkj.gps.gateway.ccs.service.AlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-17
 */
@Service
public class AlarmInfoServiceImpl implements AlarmInfoService {


    @Autowired
    private AlarmInfoMapper alarmInfoMapper;

    @Override
    public void saveAlarmInfo(AlarmInfoDbDto alarmInfo) {
        alarmInfoMapper.saveAlarmInfo(alarmInfo);
    }

    @Override
    public List<AlarmInfoDbDto> getDisEndAlarmInfo() {
        return alarmInfoMapper.getDisEndAlarmInfo();
    }


}
