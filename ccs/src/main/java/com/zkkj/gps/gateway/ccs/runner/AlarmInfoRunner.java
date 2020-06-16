package com.zkkj.gps.gateway.ccs.runner;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmInfoDbDto;
import com.zkkj.gps.gateway.ccs.service.AlarmInfoService;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-05-28
 * 程序启动将报警未结束的报警信息加载到缓存中
 */
@Component
@Order(value = 4)
public class AlarmInfoRunner implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(AlarmInfoRunner.class);
    @Autowired
    private AlarmInfoService alarmInfoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            converseAlarmInfo();
        } catch (Exception e) {
            logger.error("AlarmInfoRunner.run", e);
        }
    }

    private void converseAlarmInfo() {
        List<AlarmInfoDbDto> disEndAlarmInfoList = alarmInfoService.getDisEndAlarmInfo();
        List<TerminalAlarmInfoDto> terminalAlarmInfoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(disEndAlarmInfoList)) {
            for (AlarmInfoDbDto alarmInfo : disEndAlarmInfoList) {
                TerminalAlarmInfoDto terminalAlarmInfo = new TerminalAlarmInfoDto();
                BeanUtils.copyProperties(alarmInfo, terminalAlarmInfo);
                terminalAlarmInfo.setAlarmResType(alarmInfo.getResType() == 0 ? AlarmResTypeEnum.start : AlarmResTypeEnum.end);
                changeAlarmType(alarmInfo, terminalAlarmInfo);
                terminalAlarmInfoList.add(terminalAlarmInfo);
            }
            //然后将报警配置放到缓存中
            Map<String, List<TerminalAlarmInfoDto>> collect = terminalAlarmInfoList.stream().collect(Collectors.groupingBy(s -> s.getTerminalId()));
            Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
            terminalAlarmCache.putAll(collect);
        }
    }

    private void changeAlarmType(AlarmInfoDbDto alarmInfo, TerminalAlarmInfoDto terminalAlarmInfo) {
        int alarmTypeNum = alarmInfo.getAlarmType();
        switch (alarmTypeNum) {
            case 1:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.OFF_LINE);
                break;
            case 2:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.OVER_SPEED);
                break;
            case 4:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.STOP_OVER_TIME);
                break;
            case 8:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.VIOLATION_AREA);
                break;
            case 16:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.VEHICLE_LOAD);
                break;
            case 32:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.EQUIP_REMOVE);
                break;
            case 64:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.LINE_OFFSET);
                break;
            case 128:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.LOW_POWER);
                break;
            case 2048:
                terminalAlarmInfo.setAlarmType(AlarmTypeEnum.EQUIP_EX);
                break;
        }
    }
}
