package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-20 下午 6:62
 */

import com.zkkj.gps.gateway.protocol.source.P_0316;

/**
 * 批量读取区域及刷卡的使用规则协议构建器
 */
public class P_0316Build extends ProBuildBase {
    /**
     * 存储地址序号
     */
    private int[] addressNumber;

    /**
     * 获取协议参数
     *
     * @param terminalId
     */
    public P_0316Build(String terminalId, int[] addressNumber) {
        super(terminalId);
        this.addressNumber = addressNumber;
    }

    /**
     * 组装P_0316对象
     *
     * @return
     */
    public P_0316 getP_0316() throws Exception{
        //int[] addressNumber = {0X0000, 0X0001};
        P_0316 p_0316 = new P_0316(getTerminalId(), getReqSerialnumber(), addressNumber);
        return p_0316;
    }

}
