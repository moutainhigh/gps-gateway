package com.zkkj.gps.gateway.terminal.monitor.dto;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-04-11
 * 坐标点
 */
public class AreaPointDTO implements Serializable {

    private Integer id;

    /**
     * 经度
     */
    private Double lat;

    /**
     * 纬度
     */
    private Double lng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "AreaPointDTO{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
