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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-04-30
 * 区域超速报警
 */
public class OverSpeedTerminalAlarmAlgorithm {


    /**
     * 区域超速报警
     * 1.先判断是否进入区域
     * 2.如果进入区域了，判断缓存中是否有该终端在该区域已经发出超速报警
     * 2.1 如果存在，则不需要再发出超速报警
     * 2.1.1 继续判断区域内超速啥时结束，如果离开区域还在超速状态，则应该发出结束超速报警，缓存中清除区域内超速报警
     * 2.1.2 如果在离开区域前速度在限定值内，则发出结束超速报警，缓存中清除区域内超速报警
     * 2.2 如果不存在，则需要发出超速开始报警，并且保存到缓存中
     * 3.离开区域后如果还超速报警还没有结束，则应该发出超速报警结束，清除缓存中的超速报警信息
     *
     * @param terminalId
     * @param hisListPosition
     * @param appKeyAlarmConfig
     * @return
     */
    public static TerminalAlarmInfoDto terminalAlarmOverSpeed(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        TerminalAlarmInfoDto terminalAlarmInfo = null;
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        AreaDto area = alarmConfig.getArea();
        String startTime = alarmConfig.getStartTime();
        boolean moreThanStartTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, startTime, null);
        if (!alarmConfig.validate().isSuccess() || !moreThanStartTime) {
            return terminalAlarmInfo;
        }

        //从缓存中获取该终端进区域报警信息
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = Constant.terminalAlarmInfoCache.get(terminalId) == null ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
        //获取缓存中该公司该区域该报警类型下是否存在报警
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAppKey().equals(appKeyAlarmConfig.getAppKey()) && s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId())).collect(Collectors.toList());
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, null, endTime);
        if (moreThanStartTime && !moreThanEndTime) {
            //全局超度报警。产生开始报警
            if (area == null && CollectionUtils.isEmpty(collect)) {
                return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig);
            }
            if (area == null && CollectionUtils.isNotEmpty(collect)) {
                boolean unOverSpeed = GPSPositionUtil.unOverSpeed(hisListPosition, alarmConfig.getConfigValue());
                //如果连续的点都没有超速则区域超速结束,提示超速结束报警
                if (unOverSpeed) {
                    return getOverSpeedEndTerminalAlarmInfoDTO(terminalId, hisListPosition, terminalAlarmInfoDTOS, area, collect);
                }
            }

            if(area != null){
                //判断先进入该区域
                boolean isInArea = GPSPositionUtil.isInArea(area, hisListPosition);
                //判断是否进入区域
                if (isInArea) {
                    //判断缓存中该终端是否有缓存信息，如果没有则直接添加到缓存中
                    if (CollectionUtils.isNotEmpty(terminalAlarmInfoDTOS)) {
                        //判断该报警缓存中是否有在该区域内超速的报警信息，
                        //如果缓存中有该终端对应的缓存信息，需要判断缓存信息中是否已经有停车报警信息，如果有则需要判断啥时结束停车，
                        if (CollectionUtils.isNotEmpty(collect)) {
                            /**
                             * true，则区域内超速结束，给予报警
                             * false，则终端在当前区域还是超速状态，不需要提示报警信息
                             */
                            boolean unOverSpeed = GPSPositionUtil.unOverSpeed(hisListPosition, alarmConfig.getConfigValue());
                            //如果连续的点都没有超速则区域超速结束,提示超速结束报警
                            if (unOverSpeed) {
                                return getOverSpeedEndTerminalAlarmInfoDTO(terminalId, hisListPosition, terminalAlarmInfoDTOS, area, collect);
                            }
                            return terminalAlarmInfo;
                        }
                        //判断超速
                        return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig);
                    }
                    //判断超速
                    return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig);
                }
                /**
                 * 3.离开区域后如果还超速报警还没有结束，则应该发出超速报警结束，清除缓存中的超速报警信息
                 */
                if (GPSPositionUtil.isOutArea(area, hisListPosition)) {
                    if (CollectionUtils.isNotEmpty(terminalAlarmInfoDTOS)) {
                        if (collect != null && collect.size() > 0) {
                            return getOverSpeedEndTerminalAlarmInfoDTO(terminalId, hisListPosition, terminalAlarmInfoDTOS, area, collect);
                        }
                    }
                }
            }
        }
        return terminalAlarmInfo;
    }

    /**
     * 获取超速结束报警信息
     *
     * @param terminalId
     * @param hisListPosition
     * @param terminalAlarmInfoDTOS
     * @param area
     * @param collect
     * @return
     */
    private static TerminalAlarmInfoDto getOverSpeedEndTerminalAlarmInfoDTO(String terminalId, QueueList<BasicPositionDto> hisListPosition, List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS, AreaDto area, List<TerminalAlarmInfoDto> collect) {
        TerminalAlarmInfoDto terminalAlarmInfo;
        terminalAlarmInfo = collect.get(0);
        terminalAlarmInfo.setLatitude(hisListPosition.get(0).getLatitude());
        terminalAlarmInfo.setLongitude(hisListPosition.get(0).getLongitude());
        terminalAlarmInfo.setAlarmResType(AlarmResTypeEnum.end);
        double average = hisListPosition.stream().mapToDouble(s -> s.getSpeedKmH()).summaryStatistics().getAverage();
        terminalAlarmInfo.setAlarmValue(average);
        terminalAlarmInfo.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】" + (area == null ? "" : "在区域【" + area.getAreaName() + "】") + ",超速结束报警");
        terminalAlarmInfo.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
        //清空该报警的缓存
        terminalAlarmInfoDTOS.remove(terminalAlarmInfo);
        Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
        return terminalAlarmInfo;
    }

    private static TerminalAlarmInfoDto getTerminalAlarmInfoDTO(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        TerminalAlarmInfoDto terminalAlarmInfo = null;
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS;
        if (GPSPositionUtil.overSpeed(hisListPosition, alarmConfig.getConfigValue())) {
            terminalAlarmInfo = new TerminalAlarmInfoDto();
            AreaDto area = alarmConfig.getArea();
            if (area != null) {
                terminalAlarmInfo.setAreaId(area.getCustomAreaId());
            } else {
                terminalAlarmInfo.setAreaId(null);
            }
            terminalAlarmInfo.setAlarmGroupId(UUID.randomUUID().toString());
            terminalAlarmInfo.setTerminalId(terminalId);
            terminalAlarmInfo.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
            terminalAlarmInfo.setAppKey(appKeyAlarmConfig.getAppKey());
            terminalAlarmInfo.setLatitude(hisListPosition.get(0).getLatitude());
            terminalAlarmInfo.setLongitude(hisListPosition.get(0).getLongitude());
            terminalAlarmInfo.setAlarmGroupId(UUID.randomUUID().toString());
            terminalAlarmInfo.setAlarmType(AlarmTypeEnum.OVER_SPEED);
            terminalAlarmInfo.setAlarmResType(AlarmResTypeEnum.start);
            terminalAlarmInfo.setConfigValue(alarmConfig.getConfigValue());
            terminalAlarmInfo.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
            terminalAlarmInfo.setCorpName(alarmConfig.getCorpName());
            //2019-06-20添加运单编号
            terminalAlarmInfo.setDispatchNo(alarmConfig.getDispatchNo());
            //超速平均值
            double average = hisListPosition.stream().mapToDouble(s -> s.getSpeedKmH()).summaryStatistics().getAverage();
            terminalAlarmInfo.setAlarmValue(average);
            terminalAlarmInfo.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】" + (area == null ? "" : "在区域【" + area.getAreaName() + "】") + ",超速开始报警");
            terminalAlarmInfo.setIdentity(alarmConfig.getCorpIdentity());
            terminalAlarmInfo.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
            terminalAlarmInfoDTOS = Constant.terminalAlarmInfoCache.get(terminalId) == null ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
            if (!terminalAlarmInfoDTOS.contains(terminalAlarmInfo)) {
                terminalAlarmInfoDTOS.add(terminalAlarmInfo);
            }
            Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
        }
        return terminalAlarmInfo;
    }


}
