package com.zkkj.gps.gateway.tcp.monitor.app.proforward.response;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;

/**
 * 数据处理层数据对接接口
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-07 下午 6:15
 */
public interface ResponseInterface {

    /**
     * 更新经纬度信息
     */
    void positionChange(DestinationBaseCompose compose, String terminalId, String megIdStr);

}
