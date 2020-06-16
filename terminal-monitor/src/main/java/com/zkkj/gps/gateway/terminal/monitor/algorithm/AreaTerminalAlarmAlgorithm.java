package com.zkkj.gps.gateway.terminal.monitor.algorithm;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
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
 * Date : 2019-04-26
 * 区域报警
 */
public class AreaTerminalAlarmAlgorithm {

    /**
     * 进区域报警
     *
     * @param terminalId
     * @param hisListPosition
     * @param appKeyAlarmConfig
     * @return
     */
    public static TerminalAlarmInfoDto terminalAlarmInArea(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
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
        /**
         * 如果连续的点大于配置结束时间，但是缓存中还存着没有结束的报警信息，应该发出结束报警
         */
        AreaDto area = alarmConfig.getArea();
        BasicPositionDto basicPositionDto = hisListPosition.get(0);
        Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = terminalAlarmCache.get(terminalId) == null ? Lists.newArrayList() : terminalAlarmCache.get(terminalId);
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId()) && s.getAppKey().equals(appKeyAlarmConfig.getAppKey())).collect(Collectors.toList());
        /**
         * 产生的有效报警应该都是在连续的点在配置开始和结束时间之内的，
         * 如果所有的点时间都大于报警配置时间，并且缓存中还存在该终端的报警信息，则应该发出报警结束信息
         */
        if (moreThanStartTime && !moreThanEndTime) {
            /**
             *  判断是否进入违规区域，
             *  true为进入区域，产生报警信息
             *  false没有进入区域，直接返回
             */
            boolean isInArea = GPSPositionUtil.isInArea(area, hisListPosition);
            if (isInArea) {
                //如果缓存中没有，则需要添加该报警信息，如果有则不需要添加
                if (CollectionUtils.isEmpty(collect)) {
                    terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
                    terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
                    terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
                    terminalAlarmInfoDTO.setAreaId(area.getCustomAreaId());
                    terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                    terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】进入【" + area.getAreaName() + "】区域,开始报警");
                    terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.VIOLATION_AREA);
                    terminalAlarmInfoDTO.setTerminalId(terminalId);
                    terminalAlarmInfoDTO.setLatitude(basicPositionDto.getLatitude());
                    terminalAlarmInfoDTO.setLongitude(basicPositionDto.getLongitude());
                    terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
                    terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
                    terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfig.getAppKey());
                    terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
                    terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
                    terminalAlarmInfoDTO.setAlarmValue(0);
                    terminalAlarmInfoDTO.setIsDeliveryArea(alarmConfig.getIsDeliveryArea());
                    //2019-06-20添加运单编号
                    terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
                    terminalAlarmInfoDTO.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
                    terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                    terminalAlarmCache.put(terminalId, terminalAlarmInfoDTOS);
                    return terminalAlarmInfoDTO;
                }
            }
            //判断离开违规区域产生报警
            if (CollectionUtils.isNotEmpty(collect) && GPSPositionUtil.isOutArea(area, hisListPosition)) {
                terminalAlarmInfoDTO = collect.get(0);
                //已经出区域，则应该给予报警信息
                terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
                terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】离开【" + area.getAreaName() + "】区域,結束报警");
                //报警间隔时长
                double alarmTime = DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(terminalAlarmInfoDTO.getAlarmTime()), hisListPosition.get(0).getDate());
                terminalAlarmInfoDTO.setAlarmValue(alarmTime);
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
