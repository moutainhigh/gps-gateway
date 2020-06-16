package com.zkkj.gps.gateway.tcp.monitor.app.service;

import com.zkkj.gps.gateway.tcp.monitor.app.entity.LocationBean;

/**
 * 定位接口
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-17 上午 11:11
 */
public interface PositionService {

    /**
     * 定位信息转发业务（重构后）
     * @param locationBean
     * @return
     */
    void positionChange(LocationBean locationBean);

}
