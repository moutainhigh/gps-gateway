package com.zkkj.gps.gateway.terminal.monitor.algorithm;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-05-05
 * 低电量报警
 */
public class LowPowerTerminalAlarmAlgorithm {


    public static TerminalAlarmInfoDto terminalAlarmLowPower(String terminalId, BasicPositionDto basicPositionDto, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
        /**
         * 1.如果当前缓存中有低电量报警
         * 1.1判断是否继续传来的数据是低电量则不需要更新报警信息，如果不是低电量则更新报警信息，发出低电量报警结束
         * 2.如果当前缓存中没有低电量报警则直接发出低电量报警
         */
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        String startTime = alarmConfig.getStartTime();
        /**
         * 校验所有的点大于等于报警配置开始时间，才会进一步判断产生报警信息
         */
        boolean moreThanStartTime = GPSPositionUtil.validateLastedPositionTime(basicPositionDto, startTime, null);
        if (!moreThanStartTime || (!alarmConfig.validate().isSuccess())) {
            return terminalAlarmInfoDTO;
        }
        //从缓存中获取该终端进区域报警信息
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = Constant.terminalAlarmInfoCache.get(terminalId) == null ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId()) && s.getAppKey().equals(appKeyAlarmConfig.getAppKey())).collect(Collectors.toList());
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateLastedPositionTime(basicPositionDto, null, endTime);
        double curPower = Double.valueOf(basicPositionDto.getPower());
        //如果当前最新的电量小于配置值，则发出低电量报警
        if (moreThanStartTime && !moreThanEndTime) {
            if (CollectionUtils.isEmpty(collect)) {
                if (curPower < alarmConfig.getConfigValue()) {
                    terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
                    terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
                    terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                    terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                    terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
                    terminalAlarmInfoDTO.setTerminalId(terminalId);
                    terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfig.getAppKey());
                    terminalAlarmInfoDTO.setLatitude(basicPositionDto.getLatitude());
                    terminalAlarmInfoDTO.setLongitude(basicPositionDto.getLongitude());
                    terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
                    String carNum = CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId);
                    terminalAlarmInfoDTO.setCarNum(carNum);
                    terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
                    terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.LOW_POWER);
                    //2019-06-20添加运单编号
                    terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
                    terminalAlarmInfoDTO.setAlarmValue(curPower);
                    terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
                    terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】,低电量开始报警");
                    terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                    Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                    return terminalAlarmInfoDTO;
                }
            }
            //缓存中存在低电量报警信息，则需要判断低电量报警啥时结束
            if (collect.size() > 0 && curPower >= alarmConfig.getConfigValue()) {
                terminalAlarmInfoDTO = collect.get(0);
                terminalAlarmInfoDTO.setLatitude(basicPositionDto.getLatitude());
                terminalAlarmInfoDTO.setLongitude(basicPositionDto.getLongitude());
                terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarmInfoDTO.setAlarmValue(curPower);
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】,低电量结束报警");
                terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
               // Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                return terminalAlarmInfoDTO;
            }
        }
        return terminalAlarmInfoDTO;
    }

}
