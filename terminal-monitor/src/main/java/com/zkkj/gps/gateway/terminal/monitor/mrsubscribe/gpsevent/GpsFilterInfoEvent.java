package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import lombok.Data;

/**
 * 过滤后的经纬度信息
 */

@Data
public class GpsFilterInfoEvent extends GpsInfoEvent {

    public GpsFilterInfoEvent(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        super(terminalId, baseGPSPositionDto);
    }

    public GpsFilterInfoEvent() {
    }
}
