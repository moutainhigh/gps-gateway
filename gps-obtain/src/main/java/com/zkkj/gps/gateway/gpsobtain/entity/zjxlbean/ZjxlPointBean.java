package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean;

import lombok.Data;

/**
 * 中交兴路车辆定位模型
 * @author suibozhuliu
 */
@Data
public class ZjxlPointBean {

    /**
     * {"result":{"mil":"","lon":"65207605",
     * "adr":"陕西省铜川市耀州区王家沟,西南方向,347.6米","utc":"1573790383000",
     * "province":"陕西省","spd":"0.0","lat":"21103819","drc":"92",
     * "country":"耀州区","city":"铜川市"},"status":1001}
     */

    /**
     * 车辆定位经度  字符串类型单位：1/600000.0
     */
    private String lon;

    /**
     * 车辆定位纬度 字符串类型单位：1/600000.0
     */
    private String lat;

    /**
     * 里程
     */
    private String mil;

    /**
     * 车辆地理位置名称
     */
    private String adr;

    /**
     * 车辆定位时间
     */
    private String utc;

    /**
     * 省名 例如：陕西省
     */
    private String province;

    /**
     * 县名 例如：府谷县
     */
    private String country;

    /**
     * 城市 例如：榆林市
     */
    private String city;

    /**
     * 速度 字符串类型单位 km/h
     */
    private String spd;

    /**
     * 方向 字符串类型（0 或 360：正北,大于 0 且小于 90：东北，等于 90：正东，大于 90 且小\n" +
     "于 180：东南，等于 180：正南，大于 180 且小于 270：14 西南，等于 270：正西，大于 270 且小于等于 359：西北，其他：未知）
     */
    private String drc;

}
