package com.zkkj.gps.gateway.ccs.runner;

import com.google.common.collect.Maps;
import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.HhdBusinessDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(value = 2)
public class TerminalPositionRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(TerminalPositionRunner.class);

    @Autowired
    private GpsInternalService gpsInternalService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            //TODO 程序启动是，读取数据所有设备最新的定位信息从数据库读取，并暂存至GpsListInfoEvent缓存中
            Map<String, BaseGPSPositionDto> mapLatestAvailablePosition = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            Map<String, BaseGPSPositionDto> latestAvailablePositionMap = latestAvailablePositionMap();
            mapLatestAvailablePosition.putAll(latestAvailablePositionMap);
        } catch (Exception e) {
            logger.error("TerminalPositionRunner.run is error", e);
        }
    }

    private Map<String, BaseGPSPositionDto> latestAvailablePositionMap() {
        Map<String, BaseGPSPositionDto> gpsPositionMap = Maps.newHashMap();
        List<VehicleLocationHisDto> latestPositionList = gpsInternalService.getLatestPositionList();
        if (CollectionUtils.isNotEmpty(latestPositionList)) {
            for (int i = 0; i < latestPositionList.size(); i++) {
                VehicleLocationHisDto vehicleLocationHisDto = latestPositionList.get(i);
                BasicPositionDto basicPositionDto = new BasicPositionDto();
                BeanUtils.copyProperties(vehicleLocationHisDto, basicPositionDto);
                basicPositionDto.setDate(DateTimeUtils.parseLocalDateTime(vehicleLocationHisDto.getGpsTime()));
                BaseGPSPositionDto baseGPSPositionDto = new BaseGPSPositionDto();
                baseGPSPositionDto.setPoint(basicPositionDto);
                baseGPSPositionDto.setTerminalId(vehicleLocationHisDto.getTerminalId());
                baseGPSPositionDto.setFlag(vehicleLocationHisDto.getFlag());
                baseGPSPositionDto.setRcvTime(DateTimeUtils.parseLocalDateTime(vehicleLocationHisDto.getRcvTime()));
                if (vehicleLocationHisDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3) {
                    String eleDispatch = vehicleLocationHisDto.getEleDispatch();
                    HhdBusinessDto hhdBusinessDto = FastJsonUtils.toBean(eleDispatch, HhdBusinessDto.class);
                    baseGPSPositionDto.setEleDispatch(hhdBusinessDto);
                }
                gpsPositionMap.put(vehicleLocationHisDto.getTerminalId(), baseGPSPositionDto);
            }
        }
        return gpsPositionMap;
    }
}
