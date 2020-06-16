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
 * Date : 2019-05-06
 * 防拆报警
 */
public class TamperTerminalAlarmAlgorithm {


    public static TerminalAlarmInfoDto terminalAlarmTamper(String terminalId, BasicPositionDto basicPositionDto, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        /**
         * 1.如果当前缓存中有防拆报警
         * 1.1判断是否继续传来的数据是防拆则不需要更新报警信息，如果不是防拆即正常则更新报警信息，发出防拆报警结束
         * 2.如果当前缓存中没有防拆报警则直接发出防拆报警
         */
        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
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
        //1.是被触发防拆报警；0.是正常
        int antiDismantle;
        try {
            antiDismantle = Integer.valueOf(basicPositionDto.getAntiDismantle());
        }catch (Exception e){
            antiDismantle = 0;
        }
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateLastedPositionTime(basicPositionDto, null, endTime);
        if (moreThanStartTime && !moreThanEndTime) {
            if (CollectionUtils.isEmpty(collect)) {
                if (antiDismantle == 1) {
                    terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
                    terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
                    terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                    terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                    terminalAlarmInfoDTO.setTerminalId(terminalId);
                    terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
                    terminalAlarmInfoDTO.setLatitude(basicPositionDto.getLatitude());
                    terminalAlarmInfoDTO.setLongitude(basicPositionDto.getLongitude());
                    terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
                    terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
                    terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.EQUIP_REMOVE);
                    terminalAlarmInfoDTO.setAlarmValue(antiDismantle);
                    terminalAlarmInfoDTO.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
                    terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
                    //2019-06-20添加运单编号
                    terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
                    terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfig.getAppKey());
                    terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】防拆开始报警");
                    terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                    Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                    return terminalAlarmInfoDTO;
                }
            }
            //缓存中存在防拆报警信息，则需要判断防拆报警啥时结束
            if (CollectionUtils.isNotEmpty(collect) && antiDismantle == 0) {
                terminalAlarmInfoDTO = collect.get(0);
                terminalAlarmInfoDTO.setLatitude(basicPositionDto.getLatitude());
                terminalAlarmInfoDTO.setLongitude(basicPositionDto.getLongitude());
                terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarmInfoDTO.setAlarmValue(antiDismantle);
                terminalAlarmInfoDTO.setConfigValue(antiDismantle);
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】防拆结束报警");
                terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
                Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                return terminalAlarmInfoDTO;
            }
        }
        return terminalAlarmInfoDTO;
    }
}
