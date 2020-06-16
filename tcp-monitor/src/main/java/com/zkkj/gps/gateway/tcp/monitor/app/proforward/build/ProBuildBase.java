package com.zkkj.gps.gateway.tcp.monitor.app.proforward.build;

import com.zkkj.gps.gateway.tcp.monitor.Constants;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 4:46
 */
public abstract class ProBuildBase {
    /**
     * 请求流水号
     */
    private int reqSerialnumber ;
    /**
     * 响应流水号
     */
    private int resSerialnumber ;
    /**
     * 終端设备序列号
     */
    private String terminalId;

    public ProBuildBase(String terminalId) {
        this.terminalId = terminalId;
        reqSerialnumber = Constants.getInstance().getReqSerialnumber();
        resSerialnumber = Constants.getInstance().getResSerialnumber();
    }


    /**
     * 获取请求流水号
     * @return
     */
    public int getReqSerialnumber() {
        return reqSerialnumber;
    }

    /**
     * 获取响应流水号
     * @return
     */
    public int getResSerialnumber() {
        return resSerialnumber;
    }

    /**
     * 获取设备终端号
     * @return
     */
    public String getTerminalId() {
        return terminalId;
    }

}
