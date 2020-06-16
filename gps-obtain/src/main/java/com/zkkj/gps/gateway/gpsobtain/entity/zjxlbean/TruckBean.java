package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean;

import lombok.Data;

/**
 * 中交兴路接口响应轨迹模型
 * @author suibozhuliu
 */
@Data
public class TruckBean {

    /**
     * 经度：1/600000.0
     */
    private String lon;
    /**
     * 纬度：1/600000.0
     */
    private String lat;
    /**
     * GPS 时间：字符串类型格式：yyyyMMdd/HHmmss
     */
    private String gtm;
    /**
     * GPS 速度:字符串类型单位 1/10.0km/h 保留 1 位小数
     */
    private String spd;
    /**
     * 里程：字符串类型当大于 0 时，需乘以 0.1 换算成实际里程，单位为 km，其他按实际值显示
     */
    private String mlg;
    /**
     * 海拔：字符串类型单位为 m
     */
    private String hgt;
    /**
     * 正北方向夹角：字符串类型（0--359 ，正北为 0，顺时针）
     */
    private String agl;

}
