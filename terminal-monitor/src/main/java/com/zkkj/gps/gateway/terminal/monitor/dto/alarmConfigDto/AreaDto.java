package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class AreaDto {


    /**
     * 区域类型
     */
    private GraphTypeEnum graphTypeEnum;

    /**
     * 区域ID ，由外部数据源传入，非主键
     */
    private String customAreaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域详细地址，备用
     */
    private String address;

    /**
     * 中心点，经度
     */
    private double centerLng;

    /**
     * 中心点，纬度
     */
    private double centerLat;

    /**
     * 半径
     */
    private double radius;

    //多边形区域点
    private AreaPointsDto[] areaPoints;
}
