package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-08-20 下午 8:82
 */

import com.zkkj.gps.gateway.protocol.source.P_8001;

/**
 * 平台应答协议构建器
 */
public class P_8001Build extends ProBuildBase {
    /**
     * 消息ID
     */
    private int msgID;

    public P_8001Build(String terminalId, int msgID) {
        super(terminalId);
        this.msgID = msgID;
    }

    /**
     * 组装P_8001对象
     * @return
     */
    public P_8001 getP_8001() throws Exception {
        //String terminalId, int serialNumber, int responseSerialNumber, int responseId, int result
        P_8001 p_8001 = new P_8001(getTerminalId(),getReqSerialnumber(),getResSerialnumber(),msgID,00);
        return p_8001;
    }

}
