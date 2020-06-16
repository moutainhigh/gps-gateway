package com.zkkj.gps.gateway.ccs.subscribegpsposition;

import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsFilterInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * author : cyc
 * Date : 2019-05-19
 * 过滤后的gps点位信息获取订阅信息进行数据持久化
 */
@Component
public class GpsFilterInfoPersistenceSubscribe {

    private Logger logger = LoggerFactory.getLogger(GpsFilterInfoPersistenceSubscribe.class);

    public GpsFilterInfoPersistenceSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Autowired
    private GpsInternalService gpsInternalService;

    @Subscribe
    public void subscribe(GpsFilterInfoEvent gpsFilterInfoEvent) {
        try {
            VehicleLocationHisDto vehicleLocationDto = GpsOriginalInfoPersistenceSubscribe.converseVehicleLocationHisDto(gpsFilterInfoEvent);
            //1.从数据库中获取到是否存在terminalId存在的点位信息
            if (vehicleLocationDto != null) {
                synchronized (this){
                    int count = gpsInternalService.getVehicleLocationCount(vehicleLocationDto.getTerminalId());
                    //更新操作
                    if (count > 0) {
                        gpsInternalService.updateFilterGpsInfo(vehicleLocationDto);
                    } else {
                        //新增操作
                        gpsInternalService.saveFilterGpsInfo(vehicleLocationDto);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("GpsFilterInfoPersistenceSubscribe.subscribe is error", e);
        }
    }



}
