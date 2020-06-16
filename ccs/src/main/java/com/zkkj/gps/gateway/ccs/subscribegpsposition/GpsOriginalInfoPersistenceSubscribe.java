package com.zkkj.gps.gateway.ccs.subscribegpsposition;

import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsOriginalInfoEvent;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * author : cyc
 * Date : 2019-05-19
 * 原始gps点位信息获取订阅信息进行数据持久化
 */
@Component
public class GpsOriginalInfoPersistenceSubscribe {

    private static Logger logger = LoggerFactory.getLogger(GpsOriginalInfoPersistenceSubscribe.class);

    public GpsOriginalInfoPersistenceSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Value("${batch.limit.count}")
    private int batchLimitCount;

    @Autowired
    private GpsInternalService gpsInternalService;

    @Subscribe
    public void subscribe(GpsOriginalInfoEvent gpsInfoEvent) {
        insertGpsPointBatch(gpsInfoEvent);
    }

    /**
     * 批量插入定位基础数据
     *
     * @param gpsInfoEvent
     */
    protected void insertGpsPointBatch(GpsOriginalInfoEvent gpsInfoEvent) {
        // 用于回调通知各个系统实时产生的原始点位，进行数据的持久化
        try {
            VehicleLocationHisDto vehicleLocationHisDto = converseVehicleLocationHisDto(gpsInfoEvent);
            if (vehicleLocationHisDto != null) {
                gpsInternalService.saveOriginalGpsInfo(vehicleLocationHisDto);
            }
        } catch (Exception e) {
            logger.error("GpsOriginalInfoPersistenceSubscribe.subscribe is error", e);
        }
    }

    /**
     * 历史数据处理并进行持久化
     *
     * @param gpsInfoEvent
     */
    public static VehicleLocationHisDto converseVehicleLocationHisDto(GpsInfoEvent gpsInfoEvent) {
        VehicleLocationHisDto vehicleLocationHisDto = null;
        if (gpsInfoEvent != null && gpsInfoEvent instanceof GpsInfoEvent) {
            vehicleLocationHisDto = new VehicleLocationHisDto();
            String terminalId = gpsInfoEvent.getTerminalId();
            BaseGPSPositionDto baseGPSPositionDto = gpsInfoEvent.getBaseGPSPositionDto();
            BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
            BeanUtils.copyProperties(basicPositionDto, vehicleLocationHisDto);
            vehicleLocationHisDto.setTerminalId(terminalId);
            vehicleLocationHisDto.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
            vehicleLocationHisDto.setRcvTime(DateTimeUtils.formatLocalDateTime(baseGPSPositionDto.getRcvTime()));
            Integer power;
            try {
                String originPower = basicPositionDto.getPower().replace(" ", "");
                power = StringUtils.isNotBlank(originPower) ? Integer.valueOf(originPower) : null;
            } catch (Exception e) {
                power = null;
                //logger.error("电量转化异常:" + FastJsonUtils.toJSONString(basicPositionDto));
            }

            Integer antiDismantle;
            try {
                String originAntiDismantle = basicPositionDto.getAntiDismantle().replace(" ", "");
                antiDismantle = StringUtils.isNotBlank(originAntiDismantle) ? Integer.valueOf(originAntiDismantle) : 0;
            } catch (Exception e) {
                antiDismantle = 0;
                //logger.error("防拆转化异常:" + FastJsonUtils.toJSONString(basicPositionDto));
            }
            vehicleLocationHisDto.setPower(power);
            vehicleLocationHisDto.setAntiDismantle(antiDismantle);
            vehicleLocationHisDto.setFlag(baseGPSPositionDto.getFlag());
            vehicleLocationHisDto.setEleDispatch(FastJsonUtils.toJSONString(baseGPSPositionDto.getEleDispatch()));
        }
        return vehicleLocationHisDto;
    }

}
