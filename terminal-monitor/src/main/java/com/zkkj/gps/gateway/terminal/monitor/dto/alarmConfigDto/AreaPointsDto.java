package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/29
 */
@Data
public class AreaPointsDto implements Serializable {

    /**
     * 序号
     */
    private int sequence;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 经度
     */
    private double lng;

    public AreaPointsDto(int sequence, double lat, double lng) {
        this.sequence = sequence;
        this.lat = lat;
        this.lng = lng;
    }

    public AreaPointsDto() {
    }
}
