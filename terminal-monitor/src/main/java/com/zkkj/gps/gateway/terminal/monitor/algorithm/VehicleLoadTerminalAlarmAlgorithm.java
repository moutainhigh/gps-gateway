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
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-06-14
 * 车辆载重异常报警
 */
public class VehicleLoadTerminalAlarmAlgorithm {


    public static TerminalAlarmInfoDto terminalAlarmVehicleLoad(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        //报警信息集合
        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        //报警配置开始时间
        /**
         * 校验所有的点大于等于报警配置开始时间，才会进一步判断产生报警信息
         */
        String startTime = alarmConfig.getStartTime();
        boolean moreThanStartTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, startTime, null);
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, null, endTime);
        if (!alarmConfig.validate().isSuccess()) {
            return terminalAlarmInfoDTO;
        }
        Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = terminalAlarmCache.get(terminalId) == null ? Lists.newArrayList() : terminalAlarmCache.get(terminalId);
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId()) && s.getAppKey().equals(appKeyAlarmConfig.getAppKey())).collect(Collectors.toList());
        /**
         * 产生的有效报警应该都是在连续的点在配置开始和结束时间之内的，
         * 如果所有的点时间都大于报警配置时间，并且缓存中还存在该终端的报警信息，则应该发出报警结束信息
         */
        if (moreThanStartTime && !moreThanEndTime) {
            /**
             * 判断连续的点的载重处于异常载重的范围，则发出载重异常报警开始
             */
            boolean unusualLoad = GPSPositionUtil.unusualLoad(alarmConfig.getConfigValue(), hisListPosition);
            double average = hisListPosition.stream().mapToDouble(s -> s.getLoadSensorValue() == null ? 0.0 : s.getLoadSensorValue()).summaryStatistics().getAverage();
            if (unusualLoad) {
                //如果缓存中没有，则需要添加该报警信息，如果有则不需要添加
                if (CollectionUtils.isEmpty(collect)) {
                    terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
                    terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
                    terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
                    terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                    terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】车辆载重开始报警");
                    terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.VEHICLE_LOAD);
                    terminalAlarmInfoDTO.setTerminalId(terminalId);
                    terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
                    terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
                    terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
                    terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
                    terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfig.getAppKey());
                    terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
                    terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
                    terminalAlarmInfoDTO.setAlarmValue(average);
                    //2019-06-20添加运单编号
                    terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
                    terminalAlarmInfoDTO.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
                    terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                    terminalAlarmCache.put(terminalId, terminalAlarmInfoDTOS);
                    return terminalAlarmInfoDTO;
                }
            }
            //判断离开违规区域产生报警
            if (CollectionUtils.isNotEmpty(collect) && !unusualLoad) {
                terminalAlarmInfoDTO = collect.get(0);
                //已经出区域，则应该给予报警信息
                terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
                terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】车辆载重结束报警");
                //报警间隔时长
                terminalAlarmInfoDTO.setAlarmValue(average);
                terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
                //然后缓存中移除这一对报警信息
                terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
                terminalAlarmCache.put(terminalId, terminalAlarmInfoDTOS);
                return terminalAlarmInfoDTO;
            }
        }
        return terminalAlarmInfoDTO;
    }
}
