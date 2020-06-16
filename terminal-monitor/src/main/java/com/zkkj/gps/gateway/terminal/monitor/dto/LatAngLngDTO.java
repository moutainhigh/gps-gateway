package com.zkkj.gps.gateway.terminal.monitor.dto;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-04-18
 * 经纬度对象
 */
public class LatAngLngDTO implements Serializable {

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 经度
     */
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatAngLngDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatAngLngDTO() {
    }

    @Override
    public String toString() {
        return "LatAngLngDTO{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
