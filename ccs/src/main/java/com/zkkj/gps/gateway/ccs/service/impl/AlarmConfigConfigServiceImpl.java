package com.zkkj.gps.gateway.ccs.service.impl;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.AlarmConfigCache;
import com.zkkj.gps.gateway.ccs.dto.alarmConfig.AlarmConfigOutDto;
import com.zkkj.gps.gateway.ccs.dto.alarmConfig.OutAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.*;
import com.zkkj.gps.gateway.ccs.dto.dispatch.BaseUpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.UpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.mappper.AlarmConfigMapper;
import com.zkkj.gps.gateway.ccs.service.AlarmConfigService;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.controller.TerminalMonitorController;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.*;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-05-13
 */

@Service
public class AlarmConfigConfigServiceImpl implements AlarmConfigService {


    private TerminalMonitorController terminalMonitorController;

    private Logger logger = LoggerFactory.getLogger(AlarmConfigConfigServiceImpl.class);

    @PostConstruct
    private void init() {
        if (terminalMonitorController == null) {
            terminalMonitorController = new TerminalMonitorController();
        }

    }

    @Value("${alarm.config.endIime}")
    private Long alarmEndTimeConfig;

    @Autowired
    private AlarmConfigMapper alarmConfigMapper;

    @Autowired
    private IGenerator iGenerator;

    /**
     * 更新报警配置信息
     * 1.先过滤出需要添加和修改的配置数据
     * 2.数据库保存操作
     * 3.更新缓存
     *
     * @param alarmConfigDto
     * @return
     * @throws Exception
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAlarmConfig(OutAlarmConfigDto alarmConfigDto) throws Exception {
        BeanValidate.checkParam(alarmConfigDto);
        if (StringUtils.isNotBlank(alarmConfigDto.getDispatchNo())) {
            //删除终端的报警配置信息
            updateOriginAlarmConfig(alarmConfigDto, "");
        }
        //处理终端的报警配置信息
        dealAlarmConfig(alarmConfigDto);
    }

    private void dealAlarmConfig(OutAlarmConfigDto alarmConfigDto) {

        List<CarAlarmConfigDto> carAlarmConfigs = alarmConfigDto.getCarAlarmConfigs();
        String appKey = alarmConfigDto.getAppKey();
        //最新的报警配置信息
        List<AlarmConfigDbDto> newAlarmConfigList = Lists.newArrayList();
        //区域
        List<AreaDbDto> areaList = Lists.newArrayList();
        //路线
        List<RouteDbDto> routeList = Lists.newArrayList();
        transformAlarmConfig(alarmConfigDto, newAlarmConfigList, areaList, routeList);
        //2.数据库保存和修改操作
        persistentAlarmConfig(newAlarmConfigList, areaList, routeList);
        //3.更新缓存
        //2019-06-20 修改接口，添加任务单编号
        terminalMonitorController.updateAlarmConfig(appKey, alarmConfigDto.getDispatchNo(), carAlarmConfigs);
    }

    /**
     * 删除终端的报警配置信息
     *
     * @param alarmConfigDto
     * @param originDispatchNo
     */
    private void updateOriginAlarmConfig(OutAlarmConfigDto alarmConfigDto, String originDispatchNo) {
        //获取该终端的缓存中存在的报警配置
        List<CarAlarmConfigDto> carAlarmConfigs = alarmConfigDto.getCarAlarmConfigs();
        List<String> terminalIdList = Lists.newArrayList();
        for (CarAlarmConfigDto carAlarmConfig : carAlarmConfigs) {
            List<CarNumTerminalIdDto> cars = carAlarmConfig.getCars();
            List<String> collect = cars.stream().map(s -> s.getTerminalId()).collect(Collectors.toList());
            terminalIdList.addAll(collect);
        }
        String appKey = alarmConfigDto.getAppKey();
        //将所有终端的报警配置信息结束时间设置为当前时间
        clearUnEndAlarmInfo(terminalIdList, appKey, originDispatchNo);
    }


    public void clearUnEndAlarmInfo(List<String> terminalIdList, String appKey, String originDispatchNo) {
        if (StringUtils.isNotBlank(originDispatchNo)) {
            UpdateAlarmConfigDto updateAlarmConfigDto = new UpdateAlarmConfigDto();
            updateAlarmConfigDto.setDispatchNo(originDispatchNo);
            updateAlarmConfigDto.setTerminalNo(terminalIdList.get(0));
            updateAlarmConfigDto.setEndTime(DateTimeUtils.getCurrentDateTimeStr());
            alarmConfigMapper.updateAlarmConfigByDispatchNo(updateAlarmConfigDto);
        } else {
            alarmConfigMapper.deleteAlarmConfig(terminalIdList, DateTimeUtils.getCurrentDateTimeStr(), appKey);
        }
        for (String terminalId : terminalIdList) {
            //然后将该终端的缓存中存在的报警結束并推送报警终止信息
            Map<String, List<TerminalAlarmInfoDto>> terminalAlarmCache = Constant.terminalAlarmInfoCache;
            List<TerminalAlarmInfoDto> terminalAlarmInfos = terminalAlarmCache.get(terminalId) == null ? Lists.newArrayList() : terminalAlarmCache.get(terminalId);
            List<TerminalAlarmInfoDto> collect = originDispatchNo != null ? terminalAlarmInfos.stream().filter(s -> s.getAppKey().equals(appKey)
                    && s.getAlarmResType() == AlarmResTypeEnum.start && StringUtils.isNotBlank(s.getDispatchNo())).collect(Collectors.toList()) : terminalAlarmInfos.stream().filter(s -> s.getAppKey().equals(appKey)
                    && s.getAlarmResType() == AlarmResTypeEnum.start && StringUtils.isBlank(s.getDispatchNo())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
                for (TerminalAlarmInfoDto terminalAlarm : collect) {
                    //设置报警值
                    if (terminalAlarm.getAlarmType().equals(AlarmTypeEnum.VIOLATION_AREA) || terminalAlarm.getAlarmType().equals(AlarmTypeEnum.OFF_LINE)
                            || terminalAlarm.getAlarmType().equals(AlarmTypeEnum.LINE_OFFSET)) {
                        double durationMinutes = DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(terminalAlarm.getAlarmTime()), DateTimeUtils.getCurrentLocalDateTime());
                        terminalAlarm.setAlarmValue(durationMinutes);
                    }
                    terminalAlarm.setAlarmInfo("系统时间已经大于该次报警配置结束时间，于【" + DateTimeUtils.getCurrentDateTimeStr() + "】结束报警,");
                    terminalAlarm.setAlarmTime(DateTimeUtils.getCurrentDateTimeStr());
                    terminalAlarm.setAlarmResType(AlarmResTypeEnum.end);
                    terminalAlarm.setLongitude(baseGPSPositionDto == null ? 0.0 : baseGPSPositionDto.getPoint() == null ? 0.0 : baseGPSPositionDto.getPoint().getLongitude());
                    terminalAlarm.setLatitude(baseGPSPositionDto == null ? 0.0 : baseGPSPositionDto.getPoint() == null ? 0.0 : baseGPSPositionDto.getPoint().getLatitude());
                    EventBus.getDefault().post(new AlarmInfoEvent(terminalAlarm.getAppKey(), terminalAlarm));
                    terminalAlarmInfos.remove(terminalAlarm);
                    logger.info("报警配置结束时间超过当前系统时间，产生终止为结束的报警发送通知,appKey===============" + terminalAlarm.getAppKey() + ";alarmInfo=" + terminalAlarm);
                }
            }
            //清除缓存中该终端对应appkey下所有运单的报警配置
            clearAlarmConfigByDispatch(terminalId, appKey, originDispatchNo);
        }
    }

    private void clearAlarmConfigByDispatch(String terminalId, String appKey, String originDispatchNo) {
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigDTOS = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig().get(terminalId);
        if (CollectionUtils.isNotEmpty(appKeyAlarmConfigDTOS)) {
            List<AppKeyAlarmConfigDto> collect = originDispatchNo != null ? appKeyAlarmConfigDTOS
                    .stream()
                    .filter(s -> (StringUtils.isNotBlank(s.getAlarmConfig().getDispatchNo())) && appKey.equals(s.getAppKey()))
                    .collect(Collectors.toList()) : appKeyAlarmConfigDTOS
                    .stream()
                    .filter(s -> (StringUtils.isBlank(s.getAlarmConfig().getDispatchNo())) && appKey.equals(s.getAppKey()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                appKeyAlarmConfigDTOS.removeAll(collect);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAlarmConfig1(AlarmConfigOutDto alarmConfigOutDto) throws Exception {
        CarAlarmConfigDto carAlarmConfigDTO = iGenerator.convert(alarmConfigOutDto, CarAlarmConfigDto.class);
        OutAlarmConfigDto alarmConfigDto = new OutAlarmConfigDto();
        alarmConfigDto.setDispatchNo("");
        alarmConfigDto.setAppKey(alarmConfigOutDto.getAppKey());
        alarmConfigDto.setCarAlarmConfigs(Lists.newArrayList(carAlarmConfigDTO));
        updateAlarmConfig(alarmConfigDto);
    }

    @Override
    public void updateDispatchInfoByDispatchNo(UpdateDispatchInfoDto updateDispatchInfoDto) {
        String oldTerminalNo = updateDispatchInfoDto.getOldTerminalNo();
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig().get(oldTerminalNo);
        if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs)) {
            List<AppKeyAlarmConfigDto> newAlarmConfigs = Lists.newArrayList(appKeyAlarmConfigs);
            clearUnEndAlarmInfo(Lists.newArrayList(oldTerminalNo), newAlarmConfigs.get(0).getAppKey(), updateDispatchInfoDto.getDispatchNo());
            converseAppKeyAlarmConfigs(newAlarmConfigs, updateDispatchInfoDto);
        }

    }


    /**
     * 根据appkey+terminalId+dispachNo 获取对应的报警配置信息
     *
     * @param baseUpdateDispatchInfoDto
     * @return
     */
    @Override
    public List<AlarmConfigCache> getAlarmConfigList(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) {
        return alarmConfigMapper.getAlarmConfigList(baseUpdateDispatchInfoDto);
    }

    private void converseAppKeyAlarmConfigs(List<AppKeyAlarmConfigDto> newAlarmConfigs, UpdateDispatchInfoDto updateDispatchInfoDto) {
        OutAlarmConfigDto outAlarmConfigDto = new OutAlarmConfigDto();
        outAlarmConfigDto.setAppKey(newAlarmConfigs.get(0).getAppKey());
        outAlarmConfigDto.setDispatchNo(updateDispatchInfoDto.getDispatchNo());
        String carNum = CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(updateDispatchInfoDto.getOldTerminalNo());
        List<CarNumTerminalIdDto> cars = Lists.newArrayList(new CarNumTerminalIdDto(carNum, updateDispatchInfoDto.getTerminalNo()));
        List<AlarmConfigDto> collect = newAlarmConfigs.stream().filter(s -> updateDispatchInfoDto.getDispatchNo().equals(s.getAlarmConfig().getDispatchNo())).map(AppKeyAlarmConfigDto::getAlarmConfig).collect(Collectors.toList());
        List<AlarmConfigDto> alarmConfigs = Lists.newArrayList(collect);
        List<CarAlarmConfigDto> carAlarmConfigs = Lists.newArrayList(new CarAlarmConfigDto(cars, alarmConfigs));
        outAlarmConfigDto.setCarAlarmConfigs(carAlarmConfigs);
        dealAlarmConfig(outAlarmConfigDto);

    }


    private void persistentAlarmConfig(List<AlarmConfigDbDto> newAlarmConfigList, List<AreaDbDto> areaList, List<RouteDbDto> routeList) {
        if (CollectionUtils.isNotEmpty(newAlarmConfigList)) {
            alarmConfigMapper.batchSaveAlarmConfig(newAlarmConfigList);
        }
        if (CollectionUtils.isNotEmpty(areaList)) {
            alarmConfigMapper.batchSaveArea(areaList);
        }
        if (CollectionUtils.isNotEmpty(routeList)) {
            alarmConfigMapper.batchSaveRoute(routeList);
        }
    }

    /**
     * 获取所有的配置信息
     *
     * @return
     */
    @Override
    public List<AlarmConfigCache> getAllAlarmConfigs() {

        return alarmConfigMapper.getAllAlarmConfigs();
    }


    /**
     * 更新配置结束时间
     * 1.更新数据库
     * 2.更新缓存
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAlarmConfigEndTime(EndAlarmConfigDto endAlarmConfigDto) throws Exception {
        alarmConfigMapper.updateAlarmConfigEndTime(endAlarmConfigDto);
        updateCacheAlarmConfig(endAlarmConfigDto);
    }

    /**
     * 根据终端id和appKey清除缓存中的报警配置信息
     *
     * @param terminalId
     * @param appKey
     * @param customConfigIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearAlarmConfigCache(String terminalId, String appKey, List<String> customConfigIdList) throws Exception {
        // TODO: 2019-06-03  修改数据库中的结束报警时间，定时任务会自动清除缓存中的报警信息，并且为结束的报警信息数据库会新增报警结束信息以及推送一条报警
        //1.更新报警配置数据库
        String currentDateTimeStr = DateTimeUtils.getCurrentDateTimeStr();
        EndAlarmConfigDto endAlarmConfigDto = new EndAlarmConfigDto(terminalId, appKey, currentDateTimeStr);
        alarmConfigMapper.updateAlarmConfigEndTimeList(endAlarmConfigDto, customConfigIdList);
        terminalMonitorController.clearAlarmConfigCache(terminalId, appKey, currentDateTimeStr, customConfigIdList);
    }

    /**
     * 更新报警配置信息
     *
     * @param endAlarmConfigDto
     */
    private void updateCacheAlarmConfig(EndAlarmConfigDto endAlarmConfigDto) {
        List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig().get(endAlarmConfigDto.getTerminalId());
        if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs)) {
            for (AppKeyAlarmConfigDto appKeyAlarmConfig : appKeyAlarmConfigs) {
                AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
                if (appKeyAlarmConfig.getAppKey().equals(endAlarmConfigDto.getAppKey())
                        && alarmConfig.getCustomAlarmConfigId().equals(endAlarmConfigDto.getCustomConfigId())) {
                    alarmConfig.setEndTime(endAlarmConfigDto.getEndTime());
                }
            }
        }
    }

    /**
     * 配置信息转化成数据库保存的对象集合
     *
     * @param alarmConfigDto
     * @return
     */
    private void transformAlarmConfig(OutAlarmConfigDto alarmConfigDto, List<AlarmConfigDbDto> alarmConfigList, List<AreaDbDto> areaList, List<RouteDbDto> routeList) {
        String appKey = alarmConfigDto.getAppKey();
        List<CarAlarmConfigDto> carAlarmConfigs = alarmConfigDto.getCarAlarmConfigs();
        for (CarAlarmConfigDto carAlarmConfigDTO : carAlarmConfigs) {
            List<AlarmConfigDto> alarmConfigs = carAlarmConfigDTO.getAlarmConfig();
            List<CarNumTerminalIdDto> cars = carAlarmConfigDTO.getCars();
            if (CollectionUtils.isNotEmpty(alarmConfigs) && CollectionUtils.isNotEmpty(cars)) {
                for (CarNumTerminalIdDto carNumTerminalIdDTO : cars) {
                    String terminalId = carNumTerminalIdDTO.getTerminalId();
                    String carNum = carNumTerminalIdDTO.getCarNum();
                    for (AlarmConfigDto alarmConfigDTO : alarmConfigs) {
                        if (StringUtils.isBlank(alarmConfigDTO.getEndTime())) {
                            LocalDateTime localDateTime = DateTimeUtils.parseLocalDateTime(alarmConfigDTO.getStartTime());
                            alarmConfigDTO.setEndTime(DateTimeUtils.formatLocalDateTime(localDateTime.plusDays(alarmEndTimeConfig)));
                        }
                        if (!alarmConfigDTO.validate().isSuccess()) {
                            throw new ParamException(alarmConfigDTO.validate().getMsg());
                        }

                        String alarmConfigId = UUID.randomUUID().toString();
                        if (alarmConfigDTO.getAlarmTypeEnum() == AlarmTypeEnum.VIOLATION_AREA ||
                                alarmConfigDTO.getAlarmTypeEnum() == AlarmTypeEnum.OVER_SPEED ||
                                alarmConfigDTO.getAlarmTypeEnum() == AlarmTypeEnum.STOP_OVER_TIME) {
                            AreaDto area = alarmConfigDTO.getArea();
                            if (area != null) {
                                AreaDbDto areaDbDto = new AreaDbDto();
                                areaDbDto.setId(UUID.randomUUID().toString());
                                areaDbDto.setAddress(area.getAddress());
                                areaDbDto.setAlarmConfigId(alarmConfigId);
                                areaDbDto.setAppKey(alarmConfigDto.getAppKey());
                                areaDbDto.setAreaName(area.getAreaName());
                                //设置存区域点
                                StringBuffer areaPoints = new StringBuffer();
                                AreaPointsDto[] areaPointArr = area.getAreaPoints();
                                if (areaPointArr != null && areaPointArr.length > 0) {
                                    Arrays.sort(areaPointArr, new Comparator<AreaPointsDto>() {
                                        @Override
                                        public int compare(AreaPointsDto o1, AreaPointsDto o2) {
                                            return o1.getSequence() - o2.getSequence();
                                        }
                                    });
                                    for (AreaPointsDto areaPoint : areaPointArr) {
                                        areaPoints.append(areaPoint.getLat()).append(",").append(areaPoint.getLng()).append(";");
                                    }
                                    areaDbDto.setAreaPoints(areaPoints.toString());
                                }
                                areaDbDto.setCenterLat(area.getCenterLat());
                                areaDbDto.setCenterLng(area.getCenterLng());
                                areaDbDto.setCreateBy(alarmConfigDto.getAppKey());
                                areaDbDto.setCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                                areaDbDto.setCustomAreaId(area.getCustomAreaId());
                                areaDbDto.setGraphType(area.getGraphTypeEnum().getSeq());
                                areaDbDto.setRadius(area.getRadius());
                                areaList.add(areaDbDto);
                            }
                        }

                        if (alarmConfigDTO.getAlarmTypeEnum() == AlarmTypeEnum.LINE_OFFSET) {
                            LineStringDto[] lineStrings = alarmConfigDTO.getLineStrings();
                            for (LineStringDto lineStringDTO : lineStrings) {
                                RouteDbDto routeDbDto = new RouteDbDto();
                                routeDbDto.setId(UUID.randomUUID().toString());
                                routeDbDto.setAlarmConfigId(alarmConfigId);
                                routeDbDto.setCustomRouteId(lineStringDTO.getCustomLineId());
                                routeDbDto.setPointSequence(lineStringDTO.getPointSequence());
                                routeDbDto.setRouteName(lineStringDTO.getName());
                                routeDbDto.setWidth(lineStringDTO.getWidth());
                                routeDbDto.setCreateBy(alarmConfigDto.getAppKey());
                                routeDbDto.setCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                                routeDbDto.setAppKey(alarmConfigDto.getAppKey());
                                routeList.add(routeDbDto);
                            }
                        }
                        AlarmConfigDbDto alarmConfigDbDto = new AlarmConfigDbDto();
                        alarmConfigDbDto.setAppKey(appKey);
                        alarmConfigDbDto.setDispatchNo(alarmConfigDto.getDispatchNo());
                        alarmConfigDbDto.setCarNum(carNum);
                        alarmConfigDbDto.setAlarmType(alarmConfigDTO.getAlarmTypeEnum().getSeq());
                        alarmConfigDbDto.setIdentity(alarmConfigDTO.getCorpIdentity());
                        alarmConfigDbDto.setCorpName(alarmConfigDTO.getCorpName());
                        alarmConfigDbDto.setCustomConfigId(alarmConfigDTO.getCustomAlarmConfigId());
                        alarmConfigDbDto.setStartTime(alarmConfigDTO.getStartTime());
                        alarmConfigDbDto.setEndTime(alarmConfigDTO.getEndTime());
                        alarmConfigDbDto.setTerminalId(terminalId);
                        alarmConfigDbDto.setCreateBy(appKey);
                        alarmConfigDbDto.setConfigValue(alarmConfigDTO.getConfigValue());
                        alarmConfigDbDto.setCreateTime(DateTimeUtils.getCurrentDateTimeStr());
                        alarmConfigDbDto.setId(alarmConfigId);
                        alarmConfigDbDto.setIsDeliveryArea(alarmConfigDTO.getIsDeliveryArea());
                        alarmConfigList.add(alarmConfigDbDto);
                    }
                }
            }
        }
    }

}
