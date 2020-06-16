package com.zkkj.gps.gateway.terminal.monitor.controller;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * author : cyc
 * Date : 2019-05-07
 */
public class TerminalMonitorController {

    /**
     * 更新终端点位信息
     *
     * @param terminalId
     * @param baseGPSPositionDto
     * @param dispatchNo
     */
    public void positionChange(String terminalId, BaseGPSPositionDto baseGPSPositionDto, String dispatchNo) {
        GpsMonitorSingle.getInstance().positionChange(terminalId, baseGPSPositionDto, dispatchNo);
    }


    /**
     * 更新终端配置信息
     *
     * @param appKey
     * @param dispatchNo
     * @param carAlarmConfigs
     */
    public void updateAlarmConfig(String appKey, String dispatchNo, List<CarAlarmConfigDto> carAlarmConfigs) {
        CarsAlarmConfigCacheSingle.getInstance().updateAlarmConfig(appKey, dispatchNo, carAlarmConfigs);
    }

    /**
     * 根据terminalId和appKey清除缓存中的报警配置信息，
     *
     * @param terminalId
     * @param appKey
     * @param customConfigIdList
     */
    public void clearAlarmConfigCache(String terminalId, String appKey, String endTimeStr, List<String> customConfigIdList) {
        Map<String, List<AppKeyAlarmConfigDto>> alarmConfigs = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = alarmConfigs.get(terminalId);
        if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs)) {
            for (String customConfigId : customConfigIdList) {
                for (AppKeyAlarmConfigDto appKeyAlarmConfig : appKeyAlarmConfigs) {
                    if (customConfigId.equals(appKeyAlarmConfig.getAlarmConfig().getCustomAlarmConfigId()) && appKey.equals(appKeyAlarmConfig.getAppKey())) {
                        appKeyAlarmConfig.getAlarmConfig().setEndTime(endTimeStr);
                    }
                }
            }
        }
    }
}
