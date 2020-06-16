package com.zkkj.gps.gateway.ccs.monitorconfig.runner;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchAddDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.service.DispatchService;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;

@Component
@Order(value = 5)
public class MonitorConfigRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(MonitorConfigRunner.class);

    @Autowired
    private DispatchService dispatchService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
        List<DispatchAddDto> dispatchAddDtos = dispatchService.getDispatchCacheListInfo();
        for (DispatchAddDto dispatchAddDto : dispatchAddDtos) {
            dispatchInfoDtoMap.put(dispatchAddDto.getTerminalNo(), dispatchAddDtoToDispatchInfoDto(dispatchAddDto));
        }
    }

    public static DispatchInfoDto dispatchAddDtoToDispatchInfoDto(DispatchAddDto dispatchAddDto) {
        DispatchInfoDto dispatchInfoDto = new DispatchInfoDto();
        dispatchInfoDto.setAppkey(dispatchAddDto.getAppkey());
        dispatchInfoDto.setCreateTime(dispatchAddDto.getCreateTime());
        dispatchInfoDto.setCarNumber(dispatchAddDto.getCarNumber());
        dispatchInfoDto.setConsignerCorpName(dispatchAddDto.getConsignerCorpName());
        dispatchInfoDto.setDispatchNo(dispatchAddDto.getDispatchNo());
        dispatchInfoDto.setDriverMobile(dispatchAddDto.getDriverMobile());
        dispatchInfoDto.setDriverName(dispatchAddDto.getDriverName());
        dispatchInfoDto.setIdentity(dispatchAddDto.getIdentity());
        dispatchInfoDto.setProductName(dispatchAddDto.getProductName());
        dispatchInfoDto.setReceiverCorpName(dispatchAddDto.getReceiverCorpName());
        dispatchInfoDto.setShipperCorpName(dispatchAddDto.getShipperCorpName());
        dispatchInfoDto.setTerminalNo(dispatchAddDto.getTerminalNo());
        dispatchInfoDto.setStatus(dispatchAddDto.getStatus() + "");
        dispatchInfoDto.setDispatchType(dispatchAddDto.getDispatchType());
        AreaDto receiverArea = new AreaDto();
        receiverArea.setAreaName(dispatchAddDto.getReceiverAreaName());
        receiverArea.setCustomAreaId(dispatchAddDto.getReceiverAreaId());
        dispatchInfoDto.setReceiverArea(receiverArea);
        AreaDto consignerArea = new AreaDto();
        consignerArea.setAreaName(dispatchAddDto.getConsignerAreaName());
        consignerArea.setCustomAreaId(dispatchAddDto.getConsignerAreaId());
        dispatchInfoDto.setConsignerArea(consignerArea);
        return dispatchInfoDto;
    }
}
