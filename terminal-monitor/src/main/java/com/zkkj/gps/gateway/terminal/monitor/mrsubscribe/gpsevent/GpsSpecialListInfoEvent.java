package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import lombok.Data;

/**
 * 进出区域使用该队列
 */

@Data
public class GpsSpecialListInfoEvent extends GpsBaseListInfoEvent {

    public GpsSpecialListInfoEvent(String terminalId, QueueList<BasicPositionDto> hisListPosition) {
        super(terminalId, hisListPosition);
    }

    public GpsSpecialListInfoEvent() {
    }
}
