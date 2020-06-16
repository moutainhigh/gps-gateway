package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

import com.zkkj.gps.gateway.protocol.component.messagebody.parameter.BaseParameter;
import com.zkkj.gps.gateway.protocol.source.P_0310;

import java.util.List;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-21 下午 3:55
 */

/**
 * 终端参数设置协议构建器
 */
public class P_0310Build extends ProBuildBase {
    /**
     * 参数列表
     */
    private List<BaseParameter> list;
    /**
     * 获取协议参数
     * @param terminalId
     */
    public P_0310Build(String terminalId, List<BaseParameter> list) {
        super(terminalId);
        this.list = list;
    }

    /**
     * 组装P_0310对象
     *
     * @return
     */
    public P_0310 getP_0310() throws Exception {
        //List<BaseParameter> list = new ArrayList<>();
        //Param4Int param4Int = new Param4Int(0x23, 6);
        //list.add(param4Int);
        P_0310 p_0310 = new P_0310(getTerminalId(), getReqSerialnumber(), list);
        return p_0310;
    }

}


