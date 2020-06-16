package com.zkkj.gps.gateway.terminal.monitor.dto;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : cyc
 * Date : 2019-04-15
 * 线路模型
 */
public class RoutePointsDTO implements Serializable {

    /**
     * 线路id
     */
    private int routeId;

    /**
     * 线路上的点集合
     */
    private List<Point2D> pointList = new ArrayList<>();

    /**
     * 线路名称
     */
    private String routeName;

    /**
     * 是否在线路上
     */
    private boolean onRouteLine;

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public List<Point2D> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point2D> pointList) {
        this.pointList = pointList;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public boolean getOnRouteLine() {
        return onRouteLine;
    }

    public void setOnRouteLine(boolean onRouteLine) {
        this.onRouteLine = onRouteLine;
    }

    @Override
    public String toString() {
        return "RoutePointsDTO{" +
                "routeId=" + routeId +
                ", pointList=" + pointList +
                ", routeName='" + routeName + '\'' +
                ", onRouteLine=" + onRouteLine +
                '}';
    }
}
