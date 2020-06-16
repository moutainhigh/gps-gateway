package com.zkkj.gps.gateway.terminal.monitor.dto;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * author : cyc
 * Date : 2019-04-11
 * 区域模型
 */
public class AreaDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域类型 1：圆，2：矩形，3：多边形
     */
    private Integer graphType;

    /**
     * 区域中心经度
     */
    private Double centerLat;

    /**
     * 区域中心纬度
     */
    private Double centerLng;

    /**
     * 半径
     */
    private Double radius;

    /**
     * 坐标点
     */
    private Set<AreaPointDTO> areaPoint = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getGraphType() {
        return graphType;
    }

    public void setGraphType(Integer graphType) {
        this.graphType = graphType;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(Double centerLng) {
        this.centerLng = centerLng;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Set<AreaPointDTO> getAreaPoint() {
        return areaPoint;
    }

    public void setAreaPoint(Set<AreaPointDTO> areaPoint) {
        this.areaPoint = areaPoint;
    }

    @Override
    public String toString() {
        return "AreaDto{" +
                "id=" + id +
                ", areaName='" + areaName + '\'' +
                ", graphType=" + graphType +
                ", centerLat=" + centerLat +
                ", centerLng=" + centerLng +
                ", radius=" + radius +
                ", areaPoint=" + areaPoint +
                '}';
    }
}
