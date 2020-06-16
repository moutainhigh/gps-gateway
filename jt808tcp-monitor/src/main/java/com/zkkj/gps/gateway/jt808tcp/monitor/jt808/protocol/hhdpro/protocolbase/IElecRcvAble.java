package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase;

import java.util.List;

/**
 * 串口接收数据，并进行数据处理接口
 * @author suibozhuliu
 */
public interface IElecRcvAble {

    /**
     * 将返回的byte数组解析为对应数据模型对象
     * @param bts
     */
    void resloveBackBts(List<Byte> bts);
}
