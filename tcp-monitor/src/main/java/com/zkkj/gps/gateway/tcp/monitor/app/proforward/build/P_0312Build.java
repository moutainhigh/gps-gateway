package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 4:42
 */

import com.zkkj.gps.gateway.protocol.source.P_0312;

/**
 * 读取设备业务协议构建器
 */
public class P_0312Build extends ProBuildBase {
    /**
     * 参数ID集合
     */
    private int[] paramId;
    /**
     * 获取协议参数
     * @param terminalId
     */
    public P_0312Build(String terminalId,int[] paramId) {
        super(terminalId);
        this.paramId = paramId;
    }

    /**
     * 组装P_0312对象
     * @return
     */
    public P_0312 getP_0312() throws Exception{
        //int parmas[] = {0x23};
        P_0312 p_0312 = new P_0312(getTerminalId(),getReqSerialnumber(), paramId);
        return p_0312;
    }

}
