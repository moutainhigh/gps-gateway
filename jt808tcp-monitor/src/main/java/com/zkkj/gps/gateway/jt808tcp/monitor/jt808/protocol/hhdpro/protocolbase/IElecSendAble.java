package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.protocolbase;

/**
 * 协议发送接口
 * @author suibozhuliu
 */
public interface IElecSendAble {
    /**
     * 将协议数据转换为byte数组，以供接口向设备发送指令
     * @return
     */
    byte[] getSendBytes();
}
