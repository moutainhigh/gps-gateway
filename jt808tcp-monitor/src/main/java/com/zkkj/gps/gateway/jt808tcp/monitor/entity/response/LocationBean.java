package com.zkkj.gps.gateway.jt808tcp.monitor.entity.response;

import lombok.Data;

/**
 * 定位发送模型
 * @author suibozhuliu
 */
@Data
public class LocationBean<T> {

    /**
     * 终端编号
     */
    private String terminalId;

    /**
     * 电子运单标记；携带的电子运单（0：电子运单不存在；1：航宏达；2：自定义；3：甲天行）
     */
    private int flag;

    /**
     * 点位数据
     */
    private PositionBean point;

    /**
     * 电子运单数据
     */
    private T eleDispatch;


}

