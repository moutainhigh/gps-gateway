package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import lombok.Data;

/**
 * websocket中gps定位
 */

@Data
public class GpsWebSocketEvent extends GpsInfoEvent {

    public GpsWebSocketEvent(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        super(terminalId, baseGPSPositionDto);
    }

    public GpsWebSocketEvent() {
    }
}
