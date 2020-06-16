package com.zkkj.gps.gateway.ccs.monitorconfig.subscribemonitorconfig;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.amqp.RcvMonitorDto;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchEventDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchStatusDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.warn.AmountWarnDto;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.mq.producer.ProducerSubscribe;
import com.zkkj.gps.gateway.ccs.service.AlarmConfigService;
import com.zkkj.gps.gateway.ccs.service.DispatchService;
import com.zkkj.gps.gateway.ccs.service.GpsWebApiService;
import com.zkkj.gps.gateway.ccs.service.WarnWebApiService;
import com.zkkj.gps.gateway.common.constant.BaseConstant;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.HhdBusinessDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;

@Component
public class AlarmInfoMonitorSubscribe {

    private Logger logger = LoggerFactory.getLogger(AlarmInfoMonitorSubscribe.class);

    public AlarmInfoMonitorSubscribe() {
        EventBus.getDefault().register(this);
    }

    //@Autowired
    //private ProducerSubscribe producerSubscribe;

    @Autowired
    private GpsWebApiService gpsWebApiService;
    @Autowired
    private DispatchService dispatchService;
    @Autowired
    private AlarmConfigService alarmConfigService;

    @Autowired
    private ProducerSubscribe producerSubscribe;

    @Autowired
    private WarnWebApiService warnWebApiService;

    @Autowired
    private RedisDao redisDao;

    @Value("${fgmgz.appkey}")
    private String fgmgzAppKey;

    //private ExecutorService poolExecutor = Executors.newFixedThreadPool(12);
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(6, 12, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.DiscardOldestPolicy());

    /*
     * @Author lx
     * @Description 监控报警配置进行判断当前报警是否为进出区域报警
     * @Date 17:52 2019/5/28
     * @Param
     * @return
     **/
    @Subscribe
    public void subscribe(AlarmInfoEvent alarmInfoEvent) {
        //从缓存中获取对应的运单信息
        System.out.println("进出区域报警事件线程名称......." + Thread.currentThread().getName());
        poolExecutor.execute(() -> {
            try {
                logger.info("进出收发货区域报警时下发电子运单运单状态传入的对象数据：" + "【" + FastJsonUtils.toJSONString(alarmInfoEvent) + "】 \n");
                Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
                if (alarmInfoEvent != null && alarmInfoEvent.getAlarmInfo() != null && alarmInfoEvent.getAlarmInfo().getTerminalId() != null
                        && alarmInfoEvent.getAlarmInfo().getTerminalId().length() > 0 && dispatchInfoDtoMap != null && dispatchInfoDtoMap.size() > 0) {
                    DispatchInfoDto dispatchInfoDto = dispatchInfoDtoMap.get(alarmInfoEvent.getAlarmInfo().getTerminalId());
                    if (dispatchInfoDto != null) {
                        TerminalAlarmInfoDto terminalAlarmInfoDTO = alarmInfoEvent.getAlarmInfo();
                        DispatchStatusDto dispatchStatusDto = new DispatchStatusDto();
                        if (dispatchInfoDto.getDispatchNo().equals(terminalAlarmInfoDTO.getDispatchNo())
                                && terminalAlarmInfoDTO.getAreaId() != null && terminalAlarmInfoDTO.getAreaId().length() > 0) {
                            //发货区域报警
                            if (dispatchInfoDto.getConsignerArea() != null && dispatchInfoDto.getConsignerArea().getCustomAreaId() != null && terminalAlarmInfoDTO.getAreaId().equals(
                                    dispatchInfoDto.getConsignerArea().getCustomAreaId()) && terminalAlarmInfoDTO.getAlarmType().equals(AlarmTypeEnum.VIOLATION_AREA)) {
                                //进发货区域
                                if (terminalAlarmInfoDTO.getAlarmResType() == AlarmResTypeEnum.start) {
                                    dispatchStatusDto = new DispatchStatusDto(1, 20, 1, "车辆进入发货区域",
                                            dispatchInfoDto.getConsignerArea().getAreaName());
                                }
                                //出发货区域
                                if (terminalAlarmInfoDTO.getAlarmResType() == AlarmResTypeEnum.end) {
                                    dispatchStatusDto = new DispatchStatusDto(3, 200, 3, "车辆离开发货区域,开始运输",
                                            dispatchInfoDto.getConsignerArea().getAreaName());
                                }
                                //修改运单状态
                                dispatchService.updateDispatchInfo(new DispatchUpdateDto(dispatchInfoDto.getDispatchNo(), dispatchStatusDto.getDispatchStatus(), ""));
                                RcvMonitorDto<DispatchEventDto> result = new RcvMonitorDto<>();
                                DispatchEventDto dispatchEventDto = transformToDispatchEventDto(alarmInfoEvent, dispatchInfoDto, dispatchStatusDto);
                                result.setData(dispatchEventDto);
                                result.setFlag(1);
                                result.setAppKey(dispatchEventDto.getAppkey());
                                producerSubscribe.produceMessage(dispatchEventDto.getAppkey(), result);
                                // logger.info("事件消息发布结果：【" + pushMessage + "】；appKey：【" + dispatchEventDto.getAppkey() + "】；result：【" + result.toString() + "】 \n");
                            }
                            //收货区域报警
                            String deductReason = "";
                            double sendGrossWeight = 0.0;
                            double sendTareWeight = 0.0;
                            if (dispatchInfoDto.getReceiverArea() != null && dispatchInfoDto.getReceiverArea().getCustomAreaId() != null && terminalAlarmInfoDTO.getAreaId().equals(
                                    dispatchInfoDto.getReceiverArea().getCustomAreaId()) && terminalAlarmInfoDTO.getAlarmType().equals(AlarmTypeEnum.VIOLATION_AREA)) {
                                //进收货区域
                                if (terminalAlarmInfoDTO.getAlarmResType() == AlarmResTypeEnum.start) {
                                    dispatchStatusDto = new DispatchStatusDto(4, 210, 4, "车辆进入收货区域",
                                            dispatchInfoDto.getReceiverArea().getAreaName());
                                }
                                //出收货区域
                                if (terminalAlarmInfoDTO.getAlarmResType() == AlarmResTypeEnum.end) {
                                    dispatchStatusDto = new DispatchStatusDto(7, 270, 8, "车辆离开收货区域,运单完成",
                                            dispatchInfoDto.getReceiverArea().getAreaName());
                                    //获取内存中数据
                                    BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(dispatchInfoDto.getTerminalNo());
                                    HhdBusinessDto hhdBusinessDto = (HhdBusinessDto) baseGPSPositionDto.getEleDispatch();
                                    if (baseGPSPositionDto != null && (baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3) && baseGPSPositionDto.getEleDispatch() != null
                                            && StringUtils.isNotBlank(hhdBusinessDto.getRcvGrossWeight()) && Double.parseDouble(hhdBusinessDto.getRcvGrossWeight()) > 0) {
                                        deductReason = hhdBusinessDto.getDeductReason();
                                        sendGrossWeight = StringUtils.isNotBlank(hhdBusinessDto.getSendGrossWeight()) ? Double.valueOf(hhdBusinessDto.getSendGrossWeight()) : 0.0;
                                        sendTareWeight = StringUtils.isNotBlank(hhdBusinessDto.getSendTareWeight()) ? Double.valueOf(hhdBusinessDto.getSendTareWeight()) : 0.0;
                                        dispatchStatusDto.setRcvTareWeight(StringUtils.isNotBlank(hhdBusinessDto.getRcvTareWeight()) ? Double.parseDouble(hhdBusinessDto.getRcvTareWeight()) : 0.0);
                                        dispatchStatusDto.setDeductWeight(StringUtils.isNotBlank(hhdBusinessDto.getDeductWeight()) ? Double.parseDouble(hhdBusinessDto.getDeductWeight()) : 0.0);
                                        dispatchStatusDto.setRcvGrossWeight(StringUtils.isNotBlank(hhdBusinessDto.getRcvGrossWeight()) ? Double.parseDouble(hhdBusinessDto.getRcvGrossWeight()) : 0.0);
                                        if (!dispatchInfoDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
                                            clearElectDispatch(dispatchInfoDto.getTerminalNo());
                                        }
                                    } else {
                                        int repeat = 0;
                                        if (baseGPSPositionDto != null && (baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3)) {
                                            while (repeat < 6) {
                                                BaseGPSPositionDto baseGPSPositionDto1 = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(dispatchInfoDto.getTerminalNo());
                                                HhdBusinessDto hhdBusinessDto1 = (HhdBusinessDto) baseGPSPositionDto1.getEleDispatch();
                                                if (baseGPSPositionDto1 != null && (baseGPSPositionDto1.getFlag() == 1 || baseGPSPositionDto1.getFlag() == 3) && baseGPSPositionDto1.getEleDispatch() != null
                                                        && StringUtils.isNotBlank(hhdBusinessDto1.getRcvGrossWeight()) && Double.parseDouble(hhdBusinessDto1.getRcvGrossWeight()) > 0) {
                                                    deductReason = hhdBusinessDto1.getDeductReason();
                                                    sendGrossWeight = StringUtils.isNotBlank(hhdBusinessDto1.getSendGrossWeight()) ? Double.valueOf(hhdBusinessDto1.getSendGrossWeight()) : 0.0;
                                                    sendTareWeight = StringUtils.isNotBlank(hhdBusinessDto1.getSendTareWeight()) ? Double.valueOf(hhdBusinessDto1.getSendTareWeight()) : 0.0;
                                                    dispatchStatusDto.setRcvTareWeight(StringUtils.isNotBlank(hhdBusinessDto1.getRcvTareWeight()) ? Double.parseDouble(hhdBusinessDto1.getRcvTareWeight()) : 0.0);
                                                    dispatchStatusDto.setDeductWeight(StringUtils.isNotBlank(hhdBusinessDto1.getDeductWeight()) ? Double.parseDouble(hhdBusinessDto1.getDeductWeight()) : 0.0);
                                                    dispatchStatusDto.setRcvGrossWeight(StringUtils.isNotBlank(hhdBusinessDto1.getRcvGrossWeight()) ? Double.parseDouble(hhdBusinessDto1.getRcvGrossWeight()) : 0.0);
                                                    if (!dispatchInfoDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
                                                        clearElectDispatch(dispatchInfoDto.getTerminalNo());
                                                    }
                                                    break;
                                                }
                                                //读取电子运单
                                                ResultVo<GpsBusinessDto> gpsDto = gpsWebApiService.readBusiness(baseGPSPositionDto.getFlag(), dispatchInfoDto.getTerminalNo());
                                                if (!ObjectUtils.isEmpty(gpsDto) && gpsDto.isSuccess() && !ObjectUtils.isEmpty(gpsDto.getData())) {
                                                    if (StringUtils.isNotBlank(gpsDto.getData().getRcvGrossWeight()) && Double.parseDouble(gpsDto.getData().getRcvGrossWeight()) > 0) {
                                                        //清除电子运单
                                                        deductReason = gpsDto.getData().getDeductReason();
                                                        sendGrossWeight = StringUtils.isNotBlank(gpsDto.getData().getSendGrossWeight()) ? Double.valueOf(gpsDto.getData().getSendGrossWeight()) : 0.0;
                                                        sendTareWeight = StringUtils.isNotBlank(gpsDto.getData().getSendTareWeight()) ? Double.valueOf(gpsDto.getData().getSendTareWeight()) : 0.0;
                                                        dispatchStatusDto.setRcvGrossWeight(StringUtils.isNotBlank(gpsDto.getData().getRcvGrossWeight()) ? Double.parseDouble(gpsDto.getData().getRcvGrossWeight()) : 0.0);
                                                        dispatchStatusDto.setRcvTareWeight(StringUtils.isNotBlank(gpsDto.getData().getRcvTareWeight()) ? Double.parseDouble(gpsDto.getData().getRcvTareWeight()) : 0.0);
                                                        dispatchStatusDto.setDeductWeight(StringUtils.isNotBlank(gpsDto.getData().getDeductWeight()) ? Double.parseDouble(gpsDto.getData().getDeductWeight()) : 0.0);
                                                        if (!dispatchInfoDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
                                                            clearElectDispatch(dispatchInfoDto.getTerminalNo());
                                                        }
                                                        break;
                                                    }
                                                }
                                                repeat++;
                                                Thread.sleep(1000);
                                                if (repeat == 6) {
                                                    logger.info("出收货区域读取电子运单失败：终端号：【" + dispatchInfoDto.getTerminalNo() +
                                                            "】；运单编号：【" + dispatchInfoDto.getDispatchNo() + "】；读取结果：【" + gpsDto.toString() + "】;【第六次读取】 \n");
                                                }
                                            }
                                        }
                                    }
                                    alarmConfigService.clearUnEndAlarmInfo(Lists.newArrayList(dispatchInfoDto.getTerminalNo()), dispatchInfoDto.getAppkey(), dispatchInfoDto.getDispatchNo());
                                    //MonitorConfigCache.getInstance().getDispatchInfoDtoMap().remove(dispatchInfoDto.getTerminalNo());
                                    MonitorConfigCache.removeMonitorConfigCache(dispatchInfoDto.getTerminalNo());
                                    //添加去除府谷有运单的车辆
                                    removeZjxlTerminalNo(dispatchInfoDto.getTerminalNo());
                                }
                                //修改运单状态
                                dispatchService.updateDispatchInfo(new DispatchUpdateDto(dispatchInfoDto.getDispatchNo(), dispatchStatusDto.getDispatchStatus(), ""));
                                RcvMonitorDto<DispatchEventDto> result = new RcvMonitorDto<>();
                                DispatchEventDto dispatchEventDto = transformToDispatchEventDto(alarmInfoEvent, dispatchInfoDto, dispatchStatusDto);
                                dispatchEventDto.setDeductReason(deductReason);
                                dispatchEventDto.setSendGrossWeight(sendGrossWeight);
                                dispatchEventDto.setSendTareWeight(sendTareWeight);
                                result.setData(dispatchEventDto);
                                result.setFlag(1);
                                result.setAppKey(dispatchEventDto.getAppkey());
                                producerSubscribe.produceMessage(dispatchEventDto.getAppkey(), result);
                                //logger.info("事件消息发布结果：【" + pushMessage + "】；appKey：【" + dispatchEventDto.getAppkey() + "】；result：【" + result.toString() + "】 \n");
                                //实收量矿发量钉钉提醒服务
                                try {
                                    //针对府谷项目没有实收量不提示
                                    if (dispatchEventDto.getStatus() == 7 && !dispatchEventDto.getDispatchNo().startsWith(BaseConstant.FGMGZ_STARTWITH)) {
                                        AmountWarnDto amountWarnDto = new AmountWarnDto();
                                        BeanUtils.copyProperties(dispatchEventDto, amountWarnDto);
                                        amountWarnDto.setDisPatchNo(dispatchEventDto.getDispatchNo());
                                        warnWebApiService.amountAlarm(amountWarnDto);
                                    }
                                } catch (Exception e) {
                                    logger.error("钉钉预警消息推送失败", e);
                                }
                            }
                        } else {
                            //logger.error("进出收发货区域报警时下发电子运单内存中运单数据有误：【dispatchInfoDto or terminalAlarmInfoDTO is null】" + JSON.toJSON(dispatchInfoDto) + "-------------" + JSON.toJSON(terminalAlarmInfoDTO) + " \n");
                        }
                    } else {
                        //logger.error("进出收发货区域报警时下发电子运单运单设备号为空：【alarmInfoEvent.getAlarmInfo().getTerminalId() is null】 \n");
                    }
                } else {
                    //logger.error("进出收发货区域报警时下发电子运单内存中运单不存在：【dispatchInfoDtoMap size is 0】 \n");
                }
            } catch (Exception e) {
                logger.error("AlarmInfoMonitorSubscribe.subscribe is error", e);
            }
        });

    }

    /**
     * 当府谷车辆离开收货区域，redis中移除相应的车辆信息，即移除从中交兴路获取该车辆定位的定时任务
     *
     * @param terminalNo
     */
    public void removeZjxlTerminalNo(String terminalNo) {
        try {
            Map<String, TruckAndTerminal> truckAndTerminalMap = FastJsonUtils.parseObject(redisDao.getValue(fgmgzAppKey), new TypeReference<Map<String, TruckAndTerminal>>() {
            });
            if (MapUtils.isNotEmpty(truckAndTerminalMap) && null != truckAndTerminalMap.get(terminalNo)) {
                truckAndTerminalMap.remove(terminalNo);
                redisDao.setKey(fgmgzAppKey, FastJsonUtils.toJSONString(truckAndTerminalMap));
            }
        } catch (Exception e) {
            logger.error("AlarmInfoMonitorSubscribe.removeZjxlTerminalNo is error", e);
        }
    }

    private DispatchEventDto transformToDispatchEventDto(AlarmInfoEvent alarmInfoEvent, DispatchInfoDto dispatchInfoDto, DispatchStatusDto dispatchStatusDto) {
        DispatchEventDto dispatchEventDto = new DispatchEventDto();
        dispatchEventDto.setAppkey(alarmInfoEvent.getAppKey());
        dispatchEventDto.setEventType(dispatchStatusDto.getEventType());
        dispatchEventDto.setEventInfo(dispatchStatusDto.getEventInfo());
        dispatchEventDto.setId(UUID.randomUUID().toString());
        dispatchEventDto.setDispatchNo(dispatchInfoDto.getDispatchNo());
        dispatchEventDto.setCarNum(dispatchInfoDto.getCarNumber());
        dispatchEventDto.setAreaName(dispatchStatusDto.getAreaName());
        dispatchEventDto.setConsignerName(dispatchInfoDto.getConsignerCorpName());
        dispatchEventDto.setDeductWeight(dispatchStatusDto.getDeductWeight() > 0.0 ? dispatchStatusDto.getDeductWeight() : 0.0);
        dispatchEventDto.setCarNum(dispatchInfoDto.getCarNumber());
        dispatchEventDto.setRcvGrossWeight(dispatchStatusDto.getRcvGrossWeight() > 0 ? dispatchStatusDto.getRcvGrossWeight() : 0.0);
        dispatchEventDto.setRcvTareWeight(dispatchStatusDto.getRcvTareWeight() > 0.0 ? dispatchStatusDto.getRcvTareWeight() : 0.0);
        dispatchEventDto.setReceiverName(dispatchInfoDto.getReceiverCorpName());
        Long mileage;
        try {
            mileage = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(alarmInfoEvent.getAlarmInfo().getTerminalId()).getPoint().getMileage();
        } catch (Exception e) {
            mileage = 0L;
        }
        dispatchEventDto.setMileage(mileage);
        dispatchEventDto.setShipperName(dispatchInfoDto.getShipperCorpName());
        dispatchEventDto.setRemark("");
        dispatchEventDto.setStatus(dispatchStatusDto.getStatus());
        if (alarmInfoEvent.getAlarmInfo() != null) {
            dispatchEventDto.setEventCreateTime(alarmInfoEvent.getAlarmInfo().getAlarmTime());
            dispatchEventDto.setTerminalId(alarmInfoEvent.getAlarmInfo().getTerminalId());
            dispatchEventDto.setLongitude(alarmInfoEvent.getAlarmInfo().getLongitude());
            dispatchEventDto.setLatitude(alarmInfoEvent.getAlarmInfo().getLatitude());
            dispatchEventDto.setIdentity(alarmInfoEvent.getAlarmInfo().getIdentity());
            dispatchEventDto.setAreaId(alarmInfoEvent.getAlarmInfo().getAreaId());
            dispatchEventDto.setCorpName(alarmInfoEvent.getAlarmInfo().getCorpName());
        }
        return dispatchEventDto;
    }

    /**
     * 移除电子运单信息
     */
    private void clearElectDispatch(String terminalID) {
        GpsBusinessDto gpsBusinessDto = toGpsBusinessDto();
        BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalID);
        if (!ObjectUtils.isEmpty(gpsBusinessDto) && baseGPSPositionDto != null) {
            if (gpsWebApiService != null) {
                //logger.info("车辆出收货区域后电子运单清除：终端号【" + terminalID + "】；下发参数：【" + gpsBusinessDto.toString() + "】 \n");
                int repeat = 1;
                while (repeat < 4) {
                    ResultVo<String> result = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), terminalID, gpsBusinessDto);
                    if (!ObjectUtils.isEmpty(result)) {
                        if (result.isSuccess() != true) {
                            repeat++;
                        } else {
                            logger.info("车辆出收货区域后电子运单清除时下发三次电子运单：终端号：【" + terminalID +
                                    "】；电子运单数据：【" + gpsBusinessDto.toString() + "】；下发结果：【" + result.toString() + "】，下发次数：【" + repeat + "】 \n");
                            break;
                        }
                    } else {
                        repeat++;
                    }
                    if (repeat == 4) {
                        logger.info("车辆出收货区域后电子运单清除时下发三次电子运单：终端号：【" + terminalID +
                                "】；电子运单数据：【" + gpsBusinessDto.toString() + "】；下发结果：【" + result.toString() + "】;【第三次下发】 \n");
                    }
                }
            } else {
                logger.error("车辆出收货区域后电子运单清除时下发电子运单服务为空：【gpsWebApiService is null】" + gpsWebApiService + "\n");
            }
        } else {
            logger.error("车辆出收货区域后电子运单清除时下发电子运单数据为空：【gpsBusinessDto is null】" + gpsBusinessDto + "\n");
        }
    }

    /**
     * 构建下发电子运单数据
     *
     * @return
     */
    private GpsBusinessDto toGpsBusinessDto() {
        GpsBusinessDto gpsBusinessDto = new GpsBusinessDto();
        gpsBusinessDto.setGoodCategory("");
        gpsBusinessDto.setGoodsName("");
        gpsBusinessDto.setDriverName("");
        gpsBusinessDto.setKcal("");
        gpsBusinessDto.setDeductWeight("0.00");
        gpsBusinessDto.setDeductReason("000");
        gpsBusinessDto.setReceiverName("");
        gpsBusinessDto.setConsignerName("");
        gpsBusinessDto.setPlateNumber("");
        gpsBusinessDto.setDisPatchNo("");
        gpsBusinessDto.setSendGrossWeight("0.00");
        gpsBusinessDto.setSendTareWeight("0.00");
        gpsBusinessDto.setRcvGrossWeight("0.00");
        gpsBusinessDto.setRcvTareWeight("0.00");
        gpsBusinessDto.setTaskNumber("");
        gpsBusinessDto.setShipperName("");
        gpsBusinessDto.setStatus("0");
        gpsBusinessDto.setLastChangeBy("");
        return gpsBusinessDto;

    }

}
