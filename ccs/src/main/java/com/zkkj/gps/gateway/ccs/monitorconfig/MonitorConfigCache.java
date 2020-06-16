package com.zkkj.gps.gateway.ccs.monitorconfig;

import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.dto.alarmConfig.OutAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchAreaMonitor;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchRouteMonitor;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.WebSocketDispatchInfo;
import com.zkkj.gps.gateway.ccs.enume.WebSocketTypeEnum;
import com.zkkj.gps.gateway.ccs.service.AlarmConfigService;
import com.zkkj.gps.gateway.ccs.service.DispatchService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarNumTerminalIdDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import org.greenrobot.eventbus.EventBus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MonitorConfigCache {

    private static ThreadPoolExecutor positionExecutor;

    private static MonitorConfigCache singleInstance;

    public Map<String, DispatchInfoDto> getDispatchInfoDtoMap() {
        return dispatchInfoDtoMap;
    }

    //定义需要保存的运单信息
    private Map<String, DispatchInfoDto> dispatchInfoDtoMap;

    static {
        singleInstance = new MonitorConfigCache();
        singleInstance.init();
        positionExecutor = new ThreadPoolExecutor(6, 12, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(200), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    /**
     * 新增运单信息到运单缓存中
     *
     * @param terminal        设备号
     * @param dispatchInfoDto 运单信息
     */
    public static void putMonitorConfigCache(String terminal, DispatchInfoDto dispatchInfoDto) {
        Map<String, DispatchInfoDto> dispatchInfoDtoMap = Maps.newHashMap();
        WebSocketDispatchInfo webSocketDispatchInfo = new WebSocketDispatchInfo();
        webSocketDispatchInfo.setDispatchInfoDto(dispatchInfoDto);
        webSocketDispatchInfo.setWebSocketDispatchType(WebSocketTypeEnum.ADDITIONAL.name());
        dispatchInfoDtoMap.put(terminal, dispatchInfoDto);
        MonitorConfigCache.getInstance().getDispatchInfoDtoMap().putAll(dispatchInfoDtoMap);
        positionExecutor.execute(() -> EventBus.getDefault().post(webSocketDispatchInfo));

    }

    /**
     * 从缓存中移除该设备对应的运单
     *
     * @param terminal 设备号
     */
    public static void removeMonitorConfigCache(String terminal) {
        DispatchInfoDto dispatchInfoDto = singleInstance.getDispatchInfoDtoMap().get(terminal);
        if (dispatchInfoDto != null) {
            MonitorConfigCache.getInstance().getDispatchInfoDtoMap().remove(terminal);
            WebSocketDispatchInfo webSocketDispatchInfo = new WebSocketDispatchInfo();
            webSocketDispatchInfo.setDispatchInfoDto(dispatchInfoDto);
            webSocketDispatchInfo.setWebSocketDispatchType(WebSocketTypeEnum.REMOVED.name());
            positionExecutor.execute(() -> EventBus.getDefault().post(webSocketDispatchInfo));
        }

    }

    public static MonitorConfigCache getInstance() {
        return singleInstance;
    }

    private MonitorConfigCache() {
        dispatchInfoDtoMap = new HashMap<>();
    }

    public void init() {

    }

    @Async
    public void addDispatchInfoInfo(DispatchInfoDto dispatchInfoDto, AlarmConfigService alarmService, DispatchService dispatchService) throws Exception {
        //获取原先的运单信息
        DispatchInfoDto dispatchInfoOld = dispatchInfoDtoMap.get(dispatchInfoDto.getTerminalNo());
        if (!ObjectUtils.isEmpty(dispatchInfoOld)) {
            DispatchUpdateDto dispatchUpdateDto = new DispatchUpdateDto(dispatchInfoOld.getDispatchNo(), 270, "新添加运单自动结束");
            dispatchService.updateDispatchInfo(dispatchUpdateDto);
        }
        //调用报警信息配置进行保存对应的报警信息
        alarmService.updateAlarmConfig(getAlarmDtoByDispatchDto(dispatchInfoDto));
        //将运单信息保存到缓存中
        Map<String, DispatchInfoDto> dispatchInfoDtoMap = new HashMap<>();
        dispatchInfoDtoMap.put(dispatchInfoDto.getTerminalNo(), dispatchInfoDto);
        putMonitorConfigCache(dispatchInfoDto.getTerminalNo(), dispatchInfoDto);
        //dispatchInfoDtoMap.put(dispatchInfoDto.getTerminalNo(), dispatchInfoDto);
    }

    /*
     * @Author lx
     * @Description 将获取到的对象封装为报警配置所需的对象
     * @Date 17:44 2019/5/28
     * @Param
     * @return
     **/
    private OutAlarmConfigDto getAlarmDtoByDispatchDto(DispatchInfoDto dispatchInfoDto) {
        OutAlarmConfigDto alarmConfigDto = new OutAlarmConfigDto();
        alarmConfigDto.setAppKey(dispatchInfoDto.getAppkey());
        //运单编号
        alarmConfigDto.setDispatchNo(dispatchInfoDto.getDispatchNo());
        List<CarAlarmConfigDto> carAlarmConfigDTOList = new ArrayList<>();
        //定义报警配置类
        CarAlarmConfigDto carAlarmConfigDTO = new CarAlarmConfigDto();
        //定义报警配置类下的车辆信息
        List<CarNumTerminalIdDto> carNumTerminalIdDTOList = new ArrayList<>();
        CarNumTerminalIdDto carNumTerminalIdDTO = new CarNumTerminalIdDto();
        carNumTerminalIdDTO.setCarNum(dispatchInfoDto.getCarNumber());
        carNumTerminalIdDTO.setTerminalId(dispatchInfoDto.getTerminalNo());
        carNumTerminalIdDTOList.add(carNumTerminalIdDTO);
        carAlarmConfigDTO.setCars(carNumTerminalIdDTOList);
        //定义报警配置类下的配置信息
        List<AlarmConfigDto> alarmConfigDTOList = new ArrayList<>();
        //添加区域报警信息
        if (dispatchInfoDto.getDispatchAreaMonitorList() != null && dispatchInfoDto.getDispatchAreaMonitorList().size() > 0) {
            for (DispatchAreaMonitor dispatchAreaMonitor : dispatchInfoDto.getDispatchAreaMonitorList()) {
                AlarmConfigDto alarmConfigDTO = new AlarmConfigDto();
                alarmConfigDTO.setAlarmTypeEnum(dispatchAreaMonitor.getAlarmTypeEnum());
                alarmConfigDTO.setConfigValue(dispatchAreaMonitor.getConfigValue());
                alarmConfigDTO.setCorpName(dispatchAreaMonitor.getCorpName());
                alarmConfigDTO.setEndTime(dispatchAreaMonitor.getEndTime());
                alarmConfigDTO.setStartTime(dispatchAreaMonitor.getStartTime());
                alarmConfigDTO.setArea(dispatchAreaMonitor.getArea());
                alarmConfigDTO.setCorpIdentity(dispatchInfoDto.getIdentity());
                alarmConfigDTO.setLineStrings(null);
                alarmConfigDTO.setCustomAlarmConfigId(dispatchAreaMonitor.getCustomConfigId());
                alarmConfigDTOList.add(alarmConfigDTO);
            }
        }
        //添加线路报警信息
        if (dispatchInfoDto.getDispatchRouteMonitorList() != null && dispatchInfoDto.getDispatchRouteMonitorList().size() > 0) {
            for (DispatchRouteMonitor dispatchRouteMonitor : dispatchInfoDto.getDispatchRouteMonitorList()) {
                AlarmConfigDto alarmConfigDTO = new AlarmConfigDto();
                alarmConfigDTO.setAlarmTypeEnum(AlarmTypeEnum.LINE_OFFSET);
                alarmConfigDTO.setConfigValue(dispatchRouteMonitor.getConfigValue());
                alarmConfigDTO.setCorpName(dispatchRouteMonitor.getCorpName());
                alarmConfigDTO.setEndTime(dispatchRouteMonitor.getEndTime());
                alarmConfigDTO.setStartTime(dispatchRouteMonitor.getStartTime());
                alarmConfigDTO.setArea(null);
                alarmConfigDTO.setCorpIdentity(dispatchInfoDto.getIdentity());
                alarmConfigDTO.setLineStrings(dispatchRouteMonitor.getLineStrings());
                alarmConfigDTO.setCustomAlarmConfigId(dispatchRouteMonitor.getCustomConfigId());
                alarmConfigDTOList.add(alarmConfigDTO);
            }
        }
        //添加发货区域报警
        alarmConfigDTOList.addAll(addAlarmConfigDtoList(dispatchInfoDto.getConsignerArea(), dispatchInfoDto));
        //添加收货区域报警
        alarmConfigDTOList.addAll(addAlarmConfigDtoList(dispatchInfoDto.getReceiverArea(), dispatchInfoDto));
        carAlarmConfigDTO.setAlarmConfig(alarmConfigDTOList);
        carAlarmConfigDTOList.add(carAlarmConfigDTO);
        alarmConfigDto.setCarAlarmConfigs(carAlarmConfigDTOList);
        return alarmConfigDto;
    }

    /*
     * @Author lx
     * @Description 添加对应的区域报警信息
     * @Date 17:38 2019/5/28
     * @Param
     * @return
     **/
    private List<AlarmConfigDto> addAlarmConfigDtoList(AreaDto areaDTO, DispatchInfoDto dispatchInfoDto) {
        String dateTimeNow = DateTimeUtils.dateToStrLong(new Date());
        String yearTimeNow = DateTimeUtils.getAfterDayToString(365);
        List<AlarmConfigDto> alarmConfigDTOList = new ArrayList<>();
        //添加收货区域报警
        if (areaDTO != null && areaDTO.getCustomAreaId().length() > 0) {
            //添加进区域报警
            AlarmConfigDto alarmConfigDTO = new AlarmConfigDto();
            alarmConfigDTO.setCorpIdentity(dispatchInfoDto.getIdentity());
            alarmConfigDTO.setCustomAlarmConfigId(UUID.randomUUID().toString());
            alarmConfigDTO.setAlarmTypeEnum(AlarmTypeEnum.VIOLATION_AREA);
            alarmConfigDTO.setArea(areaDTO);
            alarmConfigDTO.setIsDeliveryArea(1);
            alarmConfigDTO.setCorpName(dispatchInfoDto.getReceiverCorpName());
            alarmConfigDTO.setStartTime(dateTimeNow);
            alarmConfigDTO.setEndTime(yearTimeNow);
            alarmConfigDTOList.add(alarmConfigDTO);
        }
        return alarmConfigDTOList;
    }
}
