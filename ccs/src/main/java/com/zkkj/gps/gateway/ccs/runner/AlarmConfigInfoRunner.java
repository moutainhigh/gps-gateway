package com.zkkj.gps.gateway.ccs.runner;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.AlarmConfigCache;
import com.zkkj.gps.gateway.ccs.dto.dbDto.AreaDbDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.RouteDbDto;
import com.zkkj.gps.gateway.ccs.service.AlarmConfigService;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.*;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(value = 3)
public class AlarmConfigInfoRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(AlarmConfigInfoRunner.class);

    @Autowired
    private AlarmConfigService alarmService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            //当程序启动时，需要从数据库中读取有效的报警配置信息加载到内存当中
            List<AlarmConfigCache> allAlarmConfigs = alarmService.getAllAlarmConfigs();
            Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig = new HashMap<>();
            Map<String, String> carTerminalId = new HashMap<>();
            getAllAlarmConfig(allAlarmConfigs, mapAlarmConfig, carTerminalId);
            Map<String, List<AppKeyAlarmConfigDto>> cacheAlarmConfig = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
            cacheAlarmConfig.putAll(mapAlarmConfig);
            Map<String, String> carTerminalIdMap = CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId();
            carTerminalIdMap.putAll(carTerminalId);
        } catch (Exception e) {
            logger.error("AlarmConfigInfoRunner.run is error", e);
        }
    }

    public static void getAllAlarmConfig(List<AlarmConfigCache> allAlarmConfigs, Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig, Map<String, String> carTerminalId) {
        if (CollectionUtils.isNotEmpty(allAlarmConfigs)) {
            Map<String, List<AlarmConfigCache>> collect = allAlarmConfigs.stream().collect(Collectors.groupingBy(s -> s.getTerminalId()));
            for (Map.Entry<String, List<AlarmConfigCache>> entry : collect.entrySet()) {
                List<AppKeyAlarmConfigDto> appKeyAlarmList = Lists.newArrayList();
                String terminalId = entry.getKey();
                Map<String, List<AlarmConfigCache>> collect1 = entry.getValue().stream().collect(Collectors.groupingBy(s -> s.getAppKey()));
                for (Map.Entry<String, List<AlarmConfigCache>> entry1 : collect1.entrySet()) {
                    String appKey = entry1.getKey();
                    List<AlarmConfigCache> alarmConfigCacheList = entry1.getValue();
                    for (AlarmConfigCache alarmConfigCache : alarmConfigCacheList) {
                        carTerminalId.put(alarmConfigCache.getTerminalId(), alarmConfigCache.getCarNum());
                        AppKeyAlarmConfigDto appKeyAlarmConfigDTO = new AppKeyAlarmConfigDto();
                        AlarmConfigDto alarmConfig = new AlarmConfigDto();
                        AreaDbDto areaDbDto = alarmConfigCache.getAreaDbDto();
                        if (areaDbDto != null) {
                            AreaDto areaDTO = new AreaDto();
                            if (areaDbDto.getGraphType() == 1) {
                                areaDTO.setGraphTypeEnum(GraphTypeEnum.CIRCLE);
                            }
                            if (areaDbDto.getGraphType() == 2) {
                                areaDTO.setGraphTypeEnum(GraphTypeEnum.POLYGON);
                            }
                            areaDTO.setAddress(areaDbDto.getAddress());
                            areaDTO.setCenterLat(areaDbDto.getCenterLat());
                            areaDTO.setCenterLng(areaDbDto.getCenterLng());
                            areaDTO.setRadius(areaDbDto.getRadius());
                            areaDTO.setCustomAreaId(areaDbDto.getCustomAreaId());
                            areaDTO.setAreaName(areaDbDto.getAreaName());
                            // TODO: 2019-05-15 区域转化
                            String areaPointsArrStr = areaDbDto.getAreaPoints();
                            if (StringUtils.isNotBlank(areaPointsArrStr)) {
                                String[] areaPointArr = areaPointsArrStr.split(";");
                                AreaPointsDto[] areaPoints = new AreaPointsDto[areaPointArr.length];
                                for (int i = 0; i < areaPointArr.length; i++) {
                                    String[] areaPointsArr = areaPointArr[i].split(",");
                                    AreaPointsDto areaPointsDTO = new AreaPointsDto(i, Double.valueOf(areaPointsArr[0]), Double.valueOf(areaPointsArr[1]));
                                    areaPoints[i] = areaPointsDTO;
                                }
                                areaDTO.setAreaPoints(areaPoints);
                            }
                            alarmConfig.setArea(areaDTO);
                        }
                        List<RouteDbDto> routeList = alarmConfigCache.getRouteList();
                        if (CollectionUtils.isNotEmpty(routeList)) {
                            LineStringDto[] lineStrings = new LineStringDto[routeList.size()];
                            for (int i = 0; i < routeList.size(); i++) {
                                RouteDbDto routeDbDto = routeList.get(i);
                                LineStringDto lineStringDTO = new LineStringDto();
                                lineStringDTO.setName(routeDbDto.getRouteName());
                                lineStringDTO.setPointSequence(routeDbDto.getPointSequence());
                                lineStringDTO.setCustomLineId(routeDbDto.getCustomRouteId());
                                lineStringDTO.setWidth(routeDbDto.getWidth());
                                lineStrings[i] = lineStringDTO;
                            }
                            alarmConfig.setLineStrings(lineStrings);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.VIOLATION_AREA.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.VIOLATION_AREA);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.STOP_OVER_TIME.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.STOP_OVER_TIME);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.OVER_SPEED.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.OVER_SPEED);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.LOW_POWER.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.LOW_POWER);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.EQUIP_REMOVE.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.EQUIP_REMOVE);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.LINE_OFFSET.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.LINE_OFFSET);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.OFF_LINE.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.OFF_LINE);
                        }
                        if (alarmConfigCache.getAlarmType() == AlarmTypeEnum.VEHICLE_LOAD.getSeq()) {
                            alarmConfig.setAlarmTypeEnum(AlarmTypeEnum.VEHICLE_LOAD);
                        }
                        alarmConfig.setConfigValue(alarmConfigCache.getConfigValue());
                        alarmConfig.setStartTime(alarmConfigCache.getStartTime());
                        alarmConfig.setEndTime(alarmConfigCache.getEndTime());
                        alarmConfig.setCorpIdentity(alarmConfigCache.getIdentity());
                        alarmConfig.setCorpName(alarmConfigCache.getCorpName());
                        alarmConfig.setIsDeliveryArea(alarmConfigCache.getIsDeliveryArea());
                        alarmConfig.setCustomAlarmConfigId(alarmConfigCache.getCustomConfigId());
                        //2019-06-60 添加运单编号
                        alarmConfig.setDispatchNo(alarmConfigCache.getDispatchNo());
                        appKeyAlarmConfigDTO.setAppKey(appKey);
                        appKeyAlarmConfigDTO.setAlarmConfig(alarmConfig);
                        appKeyAlarmList.add(appKeyAlarmConfigDTO);
                    }

                }
                mapAlarmConfig.put(terminalId, appKeyAlarmList);
            }
        }
    }
}
