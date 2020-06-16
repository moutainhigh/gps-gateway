package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import lombok.Data;

/**
 * 未经过任何处理的原始经纬度信息
 */
@Data
public class GpsOriginalInfoEvent extends GpsInfoEvent {

    public GpsOriginalInfoEvent(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        super(terminalId, baseGPSPositionDto);
    }

    public GpsOriginalInfoEvent() {
    }
}
