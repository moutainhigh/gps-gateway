package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019-05-19
 * gps经纬度信息基类
 */

@Data
public class GpsInfoEvent {

    /**
     * 设备终端id
     */
    private String terminalId;

    /**
     * 点位的基础信息
     */
    private BaseGPSPositionDto baseGPSPositionDto;


    public GpsInfoEvent(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        this.terminalId = terminalId;
        this.baseGPSPositionDto = baseGPSPositionDto;
    }

    public GpsInfoEvent() {
    }
}
