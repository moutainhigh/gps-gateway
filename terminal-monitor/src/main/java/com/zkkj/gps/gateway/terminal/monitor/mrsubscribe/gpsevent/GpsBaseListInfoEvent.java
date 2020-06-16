package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019/9/10
 */

@Data
public class GpsBaseListInfoEvent {

    /**
     * 设备终端id
     */
    private String terminalId;

    /**
     * 队列基本点位信息
     */
    private QueueList<BasicPositionDto> hisListPosition;

    public GpsBaseListInfoEvent(String terminalId, QueueList<BasicPositionDto> hisListPosition) {
        this.terminalId = terminalId;
        this.hisListPosition = hisListPosition;
    }

    public GpsBaseListInfoEvent() {

    }

}
