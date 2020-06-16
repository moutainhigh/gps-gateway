package com.zkkj.gps.gateway.ccs.service.impl;

import java.util.*;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.*;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.warn.ElecBusinessWarnDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.monitorconfig.subscribemonitorconfig.AlarmInfoMonitorSubscribe;
import com.zkkj.gps.gateway.ccs.service.*;
import com.zkkj.gps.gateway.common.constant.BaseConstant;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.AlarmConfigCache;
import com.zkkj.gps.gateway.ccs.dto.dbDto.EndAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.OutGpsBusinessDto;
import com.zkkj.gps.gateway.ccs.mappper.DispatchMapper;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.monitorconfig.runner.MonitorConfigRunner;
import com.zkkj.gps.gateway.ccs.runner.AlarmConfigInfoRunner;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import org.springframework.util.ObjectUtils;


@Service
public class DispatchServiceImpl implements DispatchService {

    private Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class);

    @Autowired
    private CommonBaseUtil commonBaseUtil;

    @Value("${fgmgz.appkey}")
    private String fgmgzAppkey;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private OutGpsService outGpsService;

    @Autowired
    private AlarmConfigService alarmConfigService;

    @Autowired
    private GpsWebApiService gpsWebApiService;

    @Autowired
    private WarnWebApiService warnApiService;

    @Autowired
    private AlarmInfoMonitorSubscribe alarmInfoMonitorSubscribe;


    @Override
    public int addDispatchInfo(DispatchAddDto dispatchAddDto) {
        return dispatchMapper.addDispatchInfo(dispatchAddDto);
    }

    @Override
    public int updateDispatchInfo(DispatchUpdateDto dispatchUpdateDto) {
        return dispatchMapper.updateDispatchInfo(dispatchUpdateDto);
    }

    @Override
    public List<DispatchAddDto> getDispatchCacheListInfo() {
        return dispatchMapper.getDispatchCacheListInfo();
    }

    @Override
    public List<DispatchAddDto> getDispatchByTerminalId(String terminalId) {
        return dispatchMapper.getDispatchByTerminalId(terminalId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDispatchInfoByDispatchNo(UpdateDispatchInfoDto updateDispatchInfoDto) throws Exception {
        BeanValidate.checkParam(updateDispatchInfoDto);
        String oldTerminalNo = updateDispatchInfoDto.getOldTerminalNo();
        //获取缓存中原先设备终端的运单信息
        DispatchInfoDto dispatchInfoDto = MonitorConfigCache.getInstance().getDispatchInfoDtoMap().get(oldTerminalNo);
        if (dispatchInfoDto == null) {
            throw new ParamException("原先设备运单信息不存在");
        }
        //更新数据库中的该运单信息
        String newTerminalNo = updateDispatchInfoDto.getTerminalNo();
        DispatchUpdateDto dispatchUpdateDto = new DispatchUpdateDto();
        dispatchUpdateDto.setTerminalNo(newTerminalNo);
        dispatchUpdateDto.setDispatchNo(updateDispatchInfoDto.getDispatchNo());
        dispatchMapper.updateDispatchInfo(dispatchUpdateDto);
        alarmConfigService.updateDispatchInfoByDispatchNo(updateDispatchInfoDto);
        //缓存中移除原先设备的运单信息，并且将原先的运单信息添加给新的设备上
//        MonitorConfigCache.getInstance().getDispatchInfoDtoMap().remove(oldTerminalNo);
//        MonitorConfigCache.getInstance().getDispatchInfoDtoMap().put(newTerminalNo, dispatchInfoDto);
        MonitorConfigCache.removeMonitorConfigCache(oldTerminalNo);
        MonitorConfigCache.putMonitorConfigCache(newTerminalNo, dispatchInfoDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelDispatchInfo(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) throws Exception {
        BeanValidate.checkParam(baseUpdateDispatchInfoDto);
        DispatchInfoDto dispatchInfoDto = MonitorConfigCache.getInstance().getDispatchInfoDtoMap().get(baseUpdateDispatchInfoDto.getTerminalNo());
        if (dispatchInfoDto == null || !baseUpdateDispatchInfoDto.getDispatchNo().equals(dispatchInfoDto.getDispatchNo())) {
            throw new ParamException("设备编号或者运单编号有误");
        }
        //去掉当府谷车辆离开收货区域，redis中移除相应的车辆信息，即移除从中交兴路获取该车辆定位的定时任务
        alarmInfoMonitorSubscribe.removeZjxlTerminalNo(baseUpdateDispatchInfoDto.getTerminalNo());
        String terminalNo = baseUpdateDispatchInfoDto.getTerminalNo();
        String appKey = baseUpdateDispatchInfoDto.getAppKey();
        String dispatchNo = baseUpdateDispatchInfoDto.getDispatchNo();
        //改变运单状态
        GpsBusinessDto gpsBusinessDto = GpsBusinessDto.builder().consignerName("").deductReason("")
                .deductWeight("").disPatchNo(baseUpdateDispatchInfoDto.getDispatchNo()).driverName("").goodCategory("").goodsName("").kcal("")
                .lastChangeBy("").plateNumber("").rcvGrossWeight("").rcvTareWeight("").receiverName("")
                .sendGrossWeight("").sendTareWeight("").shipperName("").status("7").taskNumber("").build();
        OutGpsBusinessDto outGpsBusinessDto = new OutGpsBusinessDto();
        outGpsBusinessDto.setTerminalId(terminalNo);
        outGpsBusinessDto.setGpsBusinessDto(gpsBusinessDto);
        outGpsService.setBusinessStatus(outGpsBusinessDto);
        alarmConfigService.clearUnEndAlarmInfo(Lists.newArrayList(terminalNo), appKey, dispatchNo);
        gpsBusinessDto.setStatus("");
        gpsBusinessDto.setDisPatchNo("");
        try {
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalNo);
            if (baseGPSPositionDto != null && !baseUpdateDispatchInfoDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
                new Thread(() -> gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), terminalNo, gpsBusinessDto)).start();
            }
        } catch (Exception e) {
            logger.error("DispatchServiceImpl.cancelDispatchInfo is error", e);
        }
    }

    @Override
    public void addDispatchInfoCache(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) throws Exception {
        BeanValidate.checkParam(baseUpdateDispatchInfoDto);
        Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
        if (dispatchInfoDtoMap.get(baseUpdateDispatchInfoDto.getTerminalNo()) == null) {
            DispatchAddDto dispatchAddDto = dispatchMapper.getDispatchInfo(baseUpdateDispatchInfoDto);
            if (dispatchAddDto != null) {
                DispatchInfoDto dispatchInfoDto = MonitorConfigRunner.dispatchAddDtoToDispatchInfoDto(dispatchAddDto);
                //dispatchInfoDtoMap.put(baseUpdateDispatchInfoDto.getTerminalNo(), dispatchInfoDto);
                MonitorConfigCache.putMonitorConfigCache(baseUpdateDispatchInfoDto.getTerminalNo(), dispatchInfoDto);
                //查询出当前设备的报警配置添加到缓存
                List<AlarmConfigCache> allAlarmConfigList = alarmConfigService.getAlarmConfigList(baseUpdateDispatchInfoDto);
                if (CollectionUtils.isNotEmpty(allAlarmConfigList)) {
                    Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig = new HashMap<>();
                    Map<String, String> carTerminalId = new HashMap<>();
                    AlarmConfigInfoRunner.getAllAlarmConfig(allAlarmConfigList, mapAlarmConfig, carTerminalId);
                    List<AppKeyAlarmConfigDto> appKeyAlarmConfigDtos = mapAlarmConfig.get(baseUpdateDispatchInfoDto.getTerminalNo());
                    Map<String, String> carTerminalIdMap = CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId();
                    carTerminalIdMap.putAll(carTerminalId);
                    Map<String, List<AppKeyAlarmConfigDto>> cacheAlarmConfig = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
                    List<AppKeyAlarmConfigDto> allAppKeyAlarmConfigDtos = cacheAlarmConfig.get(baseUpdateDispatchInfoDto.getTerminalNo());
                    if (CollectionUtils.isNotEmpty(allAppKeyAlarmConfigDtos)) {
                        allAppKeyAlarmConfigDtos.addAll(appKeyAlarmConfigDtos);
                    } else {
                        allAppKeyAlarmConfigDtos = appKeyAlarmConfigDtos;
                    }
                    mapAlarmConfig.put(baseUpdateDispatchInfoDto.getTerminalNo(), allAppKeyAlarmConfigDtos);
                    cacheAlarmConfig.putAll(mapAlarmConfig);
                    for (AlarmConfigCache alarmConfigCache : allAlarmConfigList) {
                        EndAlarmConfigDto endAlarmConfigDto = new EndAlarmConfigDto();
                        endAlarmConfigDto.setDisPatchNo(alarmConfigCache.getDispatchNo());
                        endAlarmConfigDto.setEndTime(alarmConfigCache.getEndTime());
                        endAlarmConfigDto.setAppKey(alarmConfigCache.getAppKey());
                        //只有收发货区域的，因此没必要写成批量的
                        alarmConfigService.updateAlarmConfigEndTime(endAlarmConfigDto);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clearDispatchInfo(List<BaseUpdateDispatchInfoDto> baseUpdateDispatchInfoDtoList) throws Exception {
        BeanValidate.checkParam(baseUpdateDispatchInfoDtoList);
        for (BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto : baseUpdateDispatchInfoDtoList) {
            String terminalNo = baseUpdateDispatchInfoDto.getTerminalNo();
            String appKey = baseUpdateDispatchInfoDto.getAppKey();
            String dispatchNo = baseUpdateDispatchInfoDto.getDispatchNo();
            GpsBusinessDto gpsBusinessDto = GpsBusinessDto.builder().consignerName("").deductReason("")
                    .deductWeight("").disPatchNo(baseUpdateDispatchInfoDto.getDispatchNo()).driverName("").goodCategory("").goodsName("").kcal("")
                    .lastChangeBy("").plateNumber("").rcvGrossWeight("").rcvTareWeight("").receiverName("")
                    .sendGrossWeight("").sendTareWeight("").shipperName("").status("7").taskNumber("").build();
            OutGpsBusinessDto outGpsBusinessDto = new OutGpsBusinessDto();
            outGpsBusinessDto.setTerminalId(terminalNo);
            outGpsBusinessDto.setGpsBusinessDto(gpsBusinessDto);
            outGpsService.setBusinessStatus(outGpsBusinessDto);
            alarmConfigService.clearUnEndAlarmInfo(Lists.newArrayList(terminalNo), appKey, dispatchNo);
        }
    }

    /**
     * 添加运单接口
     *
     * @param dispatchInfoDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMonitorConfigInfo(DispatchInfoDto dispatchInfoDto) throws Exception {
        dispatchInfoDto.setCreateTime(new Date());
        //处理全局配置(传入参数不规范，将参数规范化)
        if (CollectionUtils.isNotEmpty(dispatchInfoDto.getDispatchAreaMonitorList())) {
            List<DispatchAreaMonitor> tempDispatchAreaMonitorList = new ArrayList<>();
            for (int i = 0; i < dispatchInfoDto.getDispatchAreaMonitorList().size(); i++) {
                if (dispatchInfoDto.getDispatchAreaMonitorList().get(i).getArea() != null) {
                    if (dispatchInfoDto.getDispatchAreaMonitorList().get(i).getArea().getCenterLat() <= 0) {
                        dispatchInfoDto.getDispatchAreaMonitorList().get(i).setArea(null);
                    }
                }
                tempDispatchAreaMonitorList.add(dispatchInfoDto.getDispatchAreaMonitorList().get(i));
            }
            dispatchInfoDto.setDispatchAreaMonitorList(tempDispatchAreaMonitorList);
        }
        //结束数据库中当前已经存在的运单信息
        List<DispatchAddDto> dispatchAddDtoList = getDispatchByTerminalId(dispatchInfoDto.getTerminalNo());
        if (dispatchAddDtoList != null && dispatchAddDtoList.size() > 0) {
            for (DispatchAddDto dispatchAddDto : dispatchAddDtoList) {
                updateDispatchInfo(new DispatchUpdateDto(dispatchAddDto.getDispatchNo(), 270, "新增派车结束以前的运单"));
                MonitorConfigCache.getInstance().getDispatchInfoDtoMap().remove(dispatchAddDto.getTerminalNo());
            }
        }
        //新增当前运单信息(持久化到数据库)
        DispatchAddDto dispatchAddDto = dispatchInfoToDispatchAddDto(dispatchInfoDto);
        addDispatchInfo(dispatchAddDto);
        //下发电子运单信息
        BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(dispatchInfoDto.getTerminalNo());
        if (baseGPSPositionDto != null && !dispatchInfoDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
            new Thread(() -> {
                int repeat = 1;
                while (repeat < 4) {
                    GpsBusinessDto gpsBusinessDto = toGpsBusinessDto(dispatchInfoDto);
                    ResultVo<String> stringResultVo = null;
                    try {
                        stringResultVo = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), dispatchAddDto.getTerminalNo(), gpsBusinessDto);
                    } catch (Exception e) {
                        logger.error("添加运单向设备下发电子运单失败", e);
                    }
                    if (!ObjectUtils.isEmpty(stringResultVo)) {
                        if (stringResultVo.isSuccess() != true) {
                            repeat++;
                        } else {
                            logger.info("派单时下发三次电子运单：终端号：【" + dispatchInfoDto.getTerminalNo() +
                                    "】；电子运单数据：【" + gpsBusinessDto.toString() + "】；下发结果：【" + stringResultVo.toString() + "】，下发次数：【" + repeat + "】 \n");
                            break;
                        }
                    } else {
                        repeat++;
                    }
                    if (repeat == 4) {
                        logger.info("派单时下发三次电子运单均未成功：终端号：【" + dispatchInfoDto.getTerminalNo() +
                                "】；电子运单数据：【" + gpsBusinessDto.toString() + "】；下发结果：【" + stringResultVo.toString() + "】;【第三次下发】 \n");
                        try {
                            if (baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3){
                                ElecBusinessWarnDto elecBusinessWarnDto = new ElecBusinessWarnDto();
                                BeanUtils.copyProperties(gpsBusinessDto,elecBusinessWarnDto);
                                elecBusinessWarnDto.setTerminalNo(dispatchInfoDto.getTerminalNo());
                                elecBusinessWarnDto.setFlag(baseGPSPositionDto.getFlag());
                                warnApiService.sendDispatchAlarm(elecBusinessWarnDto);
                            }
                        } catch (Exception e){
                            logger.error("钉钉电子运单下发预警消息推送异常", e);
                        }
                    }
                }
            }).start();
        }
        BeanValidate.checkParam(dispatchInfoDto);
        MonitorConfigCache.getInstance().addDispatchInfoInfo(dispatchInfoDto, alarmConfigService, this);
        //针对中交兴路添加到缓存中
        addToZjxlCache(dispatchInfoDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMonitorAffiliation(UpdateDispatchIdentityInfoDto updateDispatchIdentityInfoDto) throws Exception {
        BeanValidate.checkParam(updateDispatchIdentityInfoDto);
        DispatchInfoDto dispatchInfoDto = MonitorConfigCache.getInstance().getDispatchInfoDtoMap().get(updateDispatchIdentityInfoDto.getTerminalNo());
        if (dispatchInfoDto == null || !updateDispatchIdentityInfoDto.getDispatchNo().equals(dispatchInfoDto.getDispatchNo())) {
            throw new ParamException("设备编号或者运单编号有误");
        }
        dispatchMapper.updateMonitorAffiliation(updateDispatchIdentityInfoDto);
        //更新运单缓存记录
        dispatchInfoDto.setIdentity(updateDispatchIdentityInfoDto.getIdentity());
    }

    /**
     * 如果是府谷平台的，将所属下的车辆添加到中交兴路的缓存中定时去获取
     *
     * @param dispatchInfoDto
     */
    private void addToZjxlCache(DispatchInfoDto dispatchInfoDto) {
        try {
            if (fgmgzAppkey.equals(commonBaseUtil.getAppkey())) {
                Map<String, TruckAndTerminal> truckAndTerminalMap = FastJsonUtils.parseObject(redisDao.getValue(fgmgzAppkey), new TypeReference<Map<String, TruckAndTerminal>>() {
                }) == null ? Maps.newHashMap() : FastJsonUtils.parseObject(redisDao.getValue(fgmgzAppkey), new TypeReference<Map<String, TruckAndTerminal>>() {
                });
                if (!truckAndTerminalMap.containsKey(dispatchInfoDto.getTerminalNo())) {
                    truckAndTerminalMap.put(dispatchInfoDto.getTerminalNo(), new TruckAndTerminal(dispatchInfoDto.getTerminalNo(), dispatchInfoDto.getCarNumber()));
                    redisDao.setKey(fgmgzAppkey, FastJsonUtils.toJSONString(truckAndTerminalMap));
                }
            }
        } catch (Exception e) {
            logger.error("MonitorConfigController.addToZjxlCache is error", e);
        }

    }


    private DispatchAddDto dispatchInfoToDispatchAddDto(DispatchInfoDto dispatchInfoDto) {
        DispatchAddDto dispatchAddDto = new DispatchAddDto();
        dispatchAddDto.setAppkey(dispatchInfoDto.getAppkey());
        dispatchAddDto.setCarNumber(dispatchInfoDto.getCarNumber());
        dispatchAddDto.setIdentity(dispatchInfoDto.getIdentity());
        dispatchAddDto.setTerminalNo(dispatchInfoDto.getTerminalNo());
        dispatchAddDto.setReceiverCorpName(dispatchInfoDto.getReceiverCorpName());
        dispatchAddDto.setConsignerCorpName(dispatchInfoDto.getConsignerCorpName());
        dispatchAddDto.setShipperCorpName(dispatchInfoDto.getShipperCorpName());
        dispatchAddDto.setDispatchNo(dispatchInfoDto.getDispatchNo());
        dispatchAddDto.setProductName(dispatchAddDto.getProductName());
        dispatchAddDto.setDriverName(dispatchInfoDto.getDriverName());
        dispatchAddDto.setStatus(10);
        dispatchAddDto.setDriverMobile(dispatchInfoDto.getDriverMobile());
        dispatchAddDto.setCreateByUserName(dispatchInfoDto.getCreateUserName());
        dispatchAddDto.setCreateByName(dispatchInfoDto.getAppkey() + "," + dispatchInfoDto.getIdentity());
        dispatchAddDto.setConsignerAreaName((dispatchInfoDto.getConsignerArea() != null && dispatchInfoDto.getConsignerArea().getAreaName() != null &&
                dispatchInfoDto.getConsignerArea().getAreaName().length() > 0) ? dispatchInfoDto.getConsignerArea().getAreaName() : "");
        dispatchAddDto.setConsignerAreaId(dispatchInfoDto.getConsignerArea() != null ? dispatchInfoDto.getConsignerArea().getCustomAreaId() : "");
        dispatchAddDto.setReceiverAreaName((dispatchInfoDto.getReceiverArea() != null && dispatchInfoDto.getReceiverArea().getAreaName() != null &&
                dispatchInfoDto.getReceiverArea().getAreaName().length() > 0) ? dispatchInfoDto.getReceiverArea().getAreaName() : "");
        dispatchAddDto.setReceiverAreaId(dispatchInfoDto.getReceiverArea() != null ? dispatchInfoDto.getReceiverArea().getCustomAreaId() : "");
        dispatchAddDto.setDispatchType(dispatchInfoDto.getDispatchType());
        return dispatchAddDto;
    }

    /*
     * @Author lx
     * @Description 将运单信息转化为电子运单所需信息
     * @Date 19:47 2019/6/25
     * @Param
     * @return
     **/
    private GpsBusinessDto toGpsBusinessDto(DispatchInfoDto dispatchInfoDto) {
        GpsBusinessDto gpsBusinessDto = new GpsBusinessDto();
        if (StringUtils.isEmpty(dispatchInfoDto.getTaskNumber())) {
            dispatchInfoDto.setTaskNumber("");
        }
        gpsBusinessDto.setTaskNumber(dispatchInfoDto.getTaskNumber());
        //gpsBusinessDto.setTaskNumber("0123456789");

        if (StringUtils.isEmpty(dispatchInfoDto.getProductName())) {
            dispatchInfoDto.setProductName("");
        }
        gpsBusinessDto.setGoodsName(dispatchInfoDto.getProductName());
        if (StringUtils.isEmpty(dispatchInfoDto.getGoodCategory())) {
            dispatchInfoDto.setGoodCategory("");
        }
        gpsBusinessDto.setGoodCategory(dispatchInfoDto.getGoodCategory());

        //收货公司名称 30字节
        if (dispatchInfoDto.getReceiverCorpName().length() > 15) {
            gpsBusinessDto.setReceiverName(dispatchInfoDto.getReceiverCorpName().substring(0, 15));
        } else {
            gpsBusinessDto.setReceiverName(dispatchInfoDto.getReceiverCorpName());
        }

        if (dispatchInfoDto.getConsignerCorpName().length() > 15) {
            gpsBusinessDto.setConsignerName(dispatchInfoDto.getConsignerCorpName().substring(0, 15));
        } else {
            gpsBusinessDto.setConsignerName(dispatchInfoDto.getConsignerCorpName());
        }

        if (StringUtils.isEmpty(dispatchInfoDto.getCarNumber())) {
            dispatchInfoDto.setCarNumber("");
        }
        gpsBusinessDto.setPlateNumber(dispatchInfoDto.getCarNumber());

        if (StringUtils.isEmpty(dispatchInfoDto.getDispatchNo())) {
            dispatchInfoDto.setDispatchNo("");
        }
        gpsBusinessDto.setDisPatchNo(dispatchInfoDto.getDispatchNo());

        gpsBusinessDto.setSendGrossWeight("0.0");
        gpsBusinessDto.setSendTareWeight("0.0");
        gpsBusinessDto.setRcvGrossWeight("0.0");
        gpsBusinessDto.setRcvTareWeight("0.0");

        if (StringUtils.isEmpty(dispatchInfoDto.getStatus())) {
            dispatchInfoDto.setStatus("0");
        }
        gpsBusinessDto.setStatus(dispatchInfoDto.getStatus());

        if (StringUtils.isEmpty(dispatchInfoDto.getDeductReason())) {
            dispatchInfoDto.setDeductReason("");
        }
        gpsBusinessDto.setDeductReason(dispatchInfoDto.getDeductReason());

        if (StringUtils.isEmpty(dispatchInfoDto.getDeductWeight())) {
            dispatchInfoDto.setDeductWeight("");
        }
        gpsBusinessDto.setDeductWeight(dispatchInfoDto.getDeductWeight());

        if (dispatchInfoDto.getShipperCorpName().length() > 15) {
            gpsBusinessDto.setShipperName(dispatchInfoDto.getShipperCorpName().substring(0, 15));
        } else {
            gpsBusinessDto.setShipperName(dispatchInfoDto.getShipperCorpName());
        }

        if (StringUtils.isEmpty(dispatchInfoDto.getKcal())) {
            dispatchInfoDto.setKcal("");
        }
        gpsBusinessDto.setKcal(dispatchInfoDto.getKcal());

        if (StringUtils.isEmpty(dispatchInfoDto.getLastChangeBy())) {
            dispatchInfoDto.setLastChangeBy("");
        }
        gpsBusinessDto.setLastChangeBy(dispatchInfoDto.getLastChangeBy());

        if (StringUtils.isEmpty(dispatchInfoDto.getDriverName())) {
            dispatchInfoDto.setDriverName("");
        }
        gpsBusinessDto.setDriverName(dispatchInfoDto.getDriverName());
        return gpsBusinessDto;

    }

}
