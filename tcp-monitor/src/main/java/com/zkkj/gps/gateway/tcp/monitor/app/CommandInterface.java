package com.zkkj.gps.gateway.tcp.monitor.app;

import com.zkkj.gps.gateway.protocol.component.common.BaseCompose;

/**
 * 指令下发接口
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 5:56
 */
public interface CommandInterface {

    /**
     * 指令下发
     * @param compose
     * @return
     */
    boolean sendCommandImpl(BaseCompose compose) throws Exception;

}
