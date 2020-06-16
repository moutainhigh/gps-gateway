package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import lombok.Data;

/**
 * websocket中gps任务定位
 */

@Data
public class GpsMonitorWebSocketEvent extends GpsInfoEvent {

    public GpsMonitorWebSocketEvent(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        super(terminalId, baseGPSPositionDto);
    }

    public GpsMonitorWebSocketEvent() {
    }
}
