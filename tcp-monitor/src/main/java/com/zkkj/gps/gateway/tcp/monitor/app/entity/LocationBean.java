package com.zkkj.gps.gateway.tcp.monitor.app.entity;

import lombok.Data;

/**
 * 定位发送模型
 */
@Data
public class LocationBean<T> {

    /**
     * 终端编号
     */
    private String terminalId;

    /**
     * 电子运单标记；携带的电子运单（0：电子运单不存在；1：航宏达；2：自定义...）
     */
    private int flag;

    /**
     * 点位数据
     */
    private PositionDto point;

    /**
     * 电子运单数据
     */
    private T eleDispatch;


}

