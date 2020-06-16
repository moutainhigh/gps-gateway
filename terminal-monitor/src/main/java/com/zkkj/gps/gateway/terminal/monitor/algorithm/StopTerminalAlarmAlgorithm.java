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
 * Date : 2019-04-29
 */
public class StopTerminalAlarmAlgorithm {

    /**
     * 区域内停车超时报警GpsMonitorSingle.java
     * 即先要判断先进入区域，然后在判断停车超时
     *
     * @param terminalId
     * @param hisListPosition
     * @param appKeyAlarmConfig
     * @return
     */
    public static TerminalAlarmInfoDto terminalAlarmStopOverTime(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        String startTime = alarmConfig.getStartTime();
        boolean moreThanStartTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, startTime, null);
        if (!alarmConfig.validate().isSuccess() || !moreThanStartTime) {
            return terminalAlarmInfoDTO;
        }
        AreaDto area = alarmConfig.getArea();
        //从缓存中获取该终端进区域报警信息
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = Constant.terminalAlarmInfoCache.get(terminalId) == null ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAppKey().equals(appKeyAlarmConfig.getAppKey()) && s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId())).collect(Collectors.toList());
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, null, endTime);
        //1.先判断进去区域
        //判断先进入该区域
        if (moreThanStartTime && !moreThanEndTime) {
            //全局的停车超时，先判断停车，然后再判断超时，然后产生报警
            if (area == null) {
                if (CollectionUtils.isEmpty(collect)) {
                    return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig, terminalAlarmInfoDTOS);
                }
                if (CollectionUtils.isNotEmpty(collect)) {
                    terminalAlarmInfoDTO = collect.get(0);
                    //如果再次传入点不符合停车，则移除缓存中之前的停车缓存
                    if (terminalAlarmInfoDTO.getAlarmResType() == null) {
                        boolean isStop = GPSPositionUtil.isStop(hisListPosition);
                        if (!isStop) {
                            terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
                            return null;
                        }
                        boolean allOverTime = GPSPositionUtil.allStopOverTime(terminalAlarmInfoDTO, hisListPosition, alarmConfig);
                        if (allOverTime) {
                            terminalAlarmInfoDTO.setAlarmInfo("于【" + terminalAlarmInfoDTO.getAlarmTime() + "】" + (area == null ? "" : "在区域【" + area.getAreaName() + "】") + ",停车超时开始报警");
                            terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                            //因为缓存中已经有 该数据则不需要放入缓存中
                            return terminalAlarmInfoDTO;
                        }
                    }
                    if (stopAlarmInfoEnd(terminalId, hisListPosition, terminalAlarmInfoDTO, area, terminalAlarmInfoDTOS)) {
                        return terminalAlarmInfoDTO;
                    }
                    return null;
                }
            } else {
                boolean isInArea = GPSPositionUtil.isInArea(area, hisListPosition);
                //判断位置发生变化并且速度大于零
                if (isInArea) {
                    //判断缓存中该终端是否有缓存信息，如果没有则直接添加到缓存中
                    if (CollectionUtils.isNotEmpty(terminalAlarmInfoDTOS)) {
                        //判断该报警缓存中是否有在该区域内停车超时的报警信息，
                        //如果缓存中有该终端对应的缓存信息，需要判断缓存信息中是否已经有停车报警信息，如果有则需要判断啥时结束停车，
                        if (CollectionUtils.isNotEmpty(collect)) {
                            /**
                             * 先判断是否超时，如果超时则发出停车超时报警
                             * true，则区域内停车结束，给予报警
                             * false，则终端在当前区域还是停车超时状态，不需要提示报警信息
                             */
                            terminalAlarmInfoDTO = collect.get(0);
                            if (terminalAlarmInfoDTO.getAlarmResType() == null) {
                                boolean isStop = GPSPositionUtil.isStop(hisListPosition);
                                if (!isStop) {
                                    terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
                                    return null;
                                }
                                boolean allOverTime = GPSPositionUtil.allStopOverTime(terminalAlarmInfoDTO, hisListPosition, alarmConfig);
                                if (allOverTime) {
                                    terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                                    terminalAlarmInfoDTO.setAlarmInfo("于【" + terminalAlarmInfoDTO.getAlarmTime() + "】" + (area == null ? "" : "在区域【" + area.getAreaName() + "】") + ",停车超时开始报警");
                                    //因为缓存中已经有 该数据则不需要放入缓存中
                                    return terminalAlarmInfoDTO;
                                }
                            }
                            if (stopAlarmInfoEnd(terminalId, hisListPosition, terminalAlarmInfoDTO, area, terminalAlarmInfoDTOS))
                                return terminalAlarmInfoDTO;
                            return null;
                        }
                        /**
                         * 当缓存中没有停车的报警配置
                         * 1，先将报警配置添加到缓存中
                         * 2.然后下次在满足停车并且判断停车时间是否大于停车报警配置信息
                         * 2.1如果满足则发出停车超时报警
                         * 2.2如果不满足则不用发出停车超时报警
                         * AppKeyAlarmConfigDto appKeyAlarmConfig
                         */
                        return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig, terminalAlarmInfoDTOS);
                    }
                    return getTerminalAlarmInfoDTO(terminalId, hisListPosition, appKeyAlarmConfig, terminalAlarmInfoDTOS);
                }
            }
        }
        return terminalAlarmInfoDTO;
    }

    private static boolean stopAlarmInfoEnd(String terminalId, QueueList<BasicPositionDto> hisListPosition, TerminalAlarmInfoDto terminalAlarmInfoDTO, AreaDto area, List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS) {
        boolean iSNotStop = GPSPositionUtil.iSNotStop(hisListPosition);
        if (iSNotStop && terminalAlarmInfoDTO.getAlarmResType() == AlarmResTypeEnum.start) {
            terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】" + (area == null ? "" : "在区域【" + area.getAreaName()) + "】,停车超时结束报警");
            terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
            terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
            double alarmValue = DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(terminalAlarmInfoDTO.getAlarmTime()), hisListPosition.get(0).getDate());
            terminalAlarmInfoDTO.setAlarmValue(alarmValue);
            terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
            terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
            terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
            Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
            return true;
        }
        return false;
    }

    private static TerminalAlarmInfoDto getTerminalAlarmInfoDTO(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfigDTO, List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS) {
        TerminalAlarmInfoDto terminalAlarmInfoDTO;
        boolean isStop = GPSPositionUtil.isStop(hisListPosition);
        if (isStop) {
            AlarmConfigDto alarmConfig = appKeyAlarmConfigDTO.getAlarmConfig();
            terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
            terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
            terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
            terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
            terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
            terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.STOP_OVER_TIME);
            terminalAlarmInfoDTO.setTerminalId(terminalId);
            terminalAlarmInfoDTO.setAreaId(alarmConfig.getArea() == null ? null : alarmConfig.getArea().getCustomAreaId());
            terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
            terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
            terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
            terminalAlarmInfoDTO.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
            terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
            terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfigDTO.getAppKey());
            terminalAlarmInfoDTO.setAlarmValue(0.0);
            //2019-06-20添加运单编号
            terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
            List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAppKey().equals(appKeyAlarmConfigDTO.getAppKey()) && s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect) && !terminalAlarmInfoDTOS.contains(terminalAlarmInfoDTO)) {
                terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
            }
        }
        return null;
    }


}
