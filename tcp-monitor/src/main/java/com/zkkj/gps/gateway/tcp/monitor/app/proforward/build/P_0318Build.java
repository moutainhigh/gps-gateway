package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-08-20 下午 8:82
 */

import com.zkkj.gps.gateway.protocol.source.P_0318;

/**
 * 读取终端区域及刷卡使用规则检验码及存储数量协议构建器
 */
public class P_0318Build extends ProBuildBase {

    /**
     * 参数总数校验值 2
     */
    private int paramTotalCrc;

    /**
     * 校验卡号参数存储区域的起始地址及结束地址 [2] 4
     */
    private int[] addressNumber ;

    public P_0318Build(String terminalId,int paramTotalCrc,int[] addressNumber) {
        super(terminalId);
        this.paramTotalCrc = paramTotalCrc;
        this.addressNumber = addressNumber;
    }

    /**
     * 组装P_0318对象
     *
     * @return
     */
    public P_0318 getP_0318() throws Exception{
        //int paramTotalCrc = 1;
        //int[] addressNumber = {0X0000, 0X0001};
        P_0318 p_0318 = new P_0318(getTerminalId(), getReqSerialnumber(), paramTotalCrc, addressNumber);
        return p_0318;
    }

}
