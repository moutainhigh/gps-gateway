package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.position;

import lombok.Data;

/**
 * 终端位置基础信息模型
 * @author suibozhuliu
 */
@Data
public class PositionBaseDto {
    /**
     * 纬度
     */
    private Integer latitude;
    /**
     * 经度
     */
    private Integer longitude;
    /**
     * 海拔
     */
    private Integer elevation;
    /**
     * 速度1/10km/h
     */
    private Integer speed;
    /**
     * 方向
     */
    private Integer direction;
    /**
     * 时间
     */
    private String date;
    /**
     * 里程，1/10km
     */
    private Integer mileage;

    @Override
    public String toString() {
        return "PositionBaseDto{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", direction=" + direction +
                ", date='" + date + '\'' +
                ", mileage=" + mileage +
                '}';
    }
}
