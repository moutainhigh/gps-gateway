package com.zkkj.gps.gateway.jt808tcp.monitor.service;

import com.zkkj.gps.gateway.jt808tcp.monitor.entity.response.LocationBean;

/**
 * 定位信息转发接口
 * @author suibozhuliu
 */
public interface PointTransmitService {

    /**
     * 定位信息转发
     * @param location
     */
    void pointTransmit(LocationBean location);

}
