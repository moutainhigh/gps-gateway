package com.zkkj.gps.gateway.terminal.monitor.algorithm;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-04-26
 * 掉线报警
 */
public class DisconnectTerminalAlarmAlgorithm {

    private static Logger logger = LoggerFactory.getLogger(DisconnectTerminalAlarmAlgorithm.class);

    public static List<TerminalAlarmInfoDto> disconnectAlarmList() {

        List<TerminalAlarmInfoDto> list = Lists.newCopyOnWriteArrayList();

        //获取所有的报警配置信息
        Map<String, List<AppKeyAlarmConfigDto>> alarmConfigs = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();

        //获取所有的最新点位信息
        Map<String, BaseGPSPositionDto> latestPositions = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();

        handleAlarmInfoByAlarmConfigs(list, alarmConfigs, latestPositions);
        return list;

    }

    /**
     * 根据报警配置处理报警信息
     *
     * @param list
     * @param alarmConfigs
     * @param latestPositions
     */
    private static void handleAlarmInfoByAlarmConfigs(List<TerminalAlarmInfoDto> list, Map<String, List<AppKeyAlarmConfigDto>> alarmConfigs, Map<String, BaseGPSPositionDto> latestPositions) {
        TerminalAlarmInfoDto terminalAlarmInfo;
        if (MapUtils.isNotEmpty(alarmConfigs)) {
            for (Map.Entry<String, List<AppKeyAlarmConfigDto>> entry : alarmConfigs.entrySet()) {
                String terminalId = entry.getKey();
                BaseGPSPositionDto baseGPSPositionDto = latestPositions.get(terminalId);
                List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = entry.getValue();
                if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs) && baseGPSPositionDto != null && baseGPSPositionDto.getPoint() != null) {
                    BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
                    List<AppKeyAlarmConfigDto> finalAlarmConfigList = appKeyAlarmConfigs.stream().filter(s -> s.getAlarmConfig().getAlarmTypeEnum().equals(AlarmTypeEnum.OFF_LINE)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(finalAlarmConfigList)) {
                        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = CollectionUtils.isEmpty(Constant.terminalAlarmInfoCache.get(terminalId)) ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
                        for (AppKeyAlarmConfigDto appKeyAlarmConfig : finalAlarmConfigList) {
                            List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAlarmType().equals(AlarmTypeEnum.OFF_LINE) && s.getAlarmConfigId().equals(appKeyAlarmConfig.getAlarmConfig().getCustomAlarmConfigId())).collect(Collectors.toList());
                            AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
                            String startTime = alarmConfig.getStartTime();
                            boolean moreThanStartTime = GPSPositionUtil.validateLastedPositionTime(basicPositionDto, startTime, null);
                            String endTime = alarmConfig.getEndTime();
                            boolean moreThanEndTime = GPSPositionUtil.validateLastedPositionTime(basicPositionDto, null, endTime);
                            if (CollectionUtils.isNotEmpty(collect) && CollectionUtils.isNotEmpty(terminalAlarmInfoDTOS)) {
                                if (moreThanStartTime && !moreThanEndTime && DateTimeUtils.durationMinutes(basicPositionDto.getDate(), DateTimeUtils.getCurrentLocalDateTime()) <= appKeyAlarmConfig.getAlarmConfig().getConfigValue()) {
                                    for (TerminalAlarmInfoDto terminalAlarmInfoDto : collect) {
                                        if (terminalAlarmInfoDto.getAppKey().equals(appKeyAlarmConfig.getAppKey()) &&
                                                terminalAlarmInfoDto.getAlarmConfigId().equals(appKeyAlarmConfig.getAlarmConfig().getCustomAlarmConfigId()) &&
                                                terminalAlarmInfoDto.getAlarmResType() == AlarmResTypeEnum.start) {
                                            double alarmValue = DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(terminalAlarmInfoDto.getAlarmTime()), basicPositionDto.getDate());
                                            terminalAlarmInfoDto.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                                            terminalAlarmInfoDto.setAlarmValue(alarmValue);
                                            terminalAlarmInfoDto.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】发出掉线结束报警");
                                            terminalAlarmInfoDto.setAlarmResType(AlarmResTypeEnum.end);
                                            list.add(terminalAlarmInfoDto);
                                            terminalAlarmInfoDTOS.remove(terminalAlarmInfoDto);
                                        }
                                    }
                                }
                            } else {
                                if (moreThanStartTime && !moreThanEndTime) {
                                    long durationMinutes = DateTimeUtils.durationMinutes(basicPositionDto.getDate(), DateTimeUtils.getCurrentLocalDateTime());
                                    if (durationMinutes > appKeyAlarmConfig.getAlarmConfig().getConfigValue()) {
                                        terminalAlarmInfo = new TerminalAlarmInfoDto();
                                        terminalAlarmInfo.setAppKey(appKeyAlarmConfig.getAppKey());
                                        terminalAlarmInfo.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
                                        terminalAlarmInfo.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()) + "】发出掉线开始报警");
                                        terminalAlarmInfo.setCorpName(appKeyAlarmConfig.getAlarmConfig().getCorpName());
                                        terminalAlarmInfo.setIdentity(appKeyAlarmConfig.getAlarmConfig().getCorpIdentity());
                                        //2019-06-20添加运单编号
                                        terminalAlarmInfo.setDispatchNo(appKeyAlarmConfig.getAlarmConfig().getDispatchNo());
                                        terminalAlarmInfo.setConfigValue(appKeyAlarmConfig.getAlarmConfig().getConfigValue());
                                        terminalAlarmInfo.setAlarmTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                                        terminalAlarmInfo.setLatitude(basicPositionDto.getLatitude());
                                        terminalAlarmInfo.setLongitude(basicPositionDto.getLongitude());
                                        terminalAlarmInfo.setAlarmResType(AlarmResTypeEnum.start);
                                        terminalAlarmInfo.setAlarmType(AlarmTypeEnum.OFF_LINE);
                                        terminalAlarmInfo.setAlarmGroupId(UUID.randomUUID().toString());
                                        terminalAlarmInfo.setTerminalId(terminalId);
                                        terminalAlarmInfo.setAlarmValue(durationMinutes);
                                        terminalAlarmInfo.setAlarmConfigId(appKeyAlarmConfig.getAlarmConfig().getCustomAlarmConfigId());
                                        terminalAlarmInfoDTOS.add(terminalAlarmInfo);
                                        list.add(terminalAlarmInfo);
                                        Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void pushDisconnectAlarm() {
        List<TerminalAlarmInfoDto> terminalAlarmInfos = disconnectAlarmList();
        if (CollectionUtils.isNotEmpty(terminalAlarmInfos)) {
            for (TerminalAlarmInfoDto terminalAlarm : terminalAlarmInfos) {
                if (terminalAlarm != null) {
                    EventBus.getDefault().post(new AlarmInfoEvent(terminalAlarm.getAppKey(), terminalAlarm));
                    //logger.info("产生报警发送通知,appKey===============" + terminalAlarm.getAppKey() + ";alarmInfo=" + terminalAlarm);
                }
            }

        }
    }

}

