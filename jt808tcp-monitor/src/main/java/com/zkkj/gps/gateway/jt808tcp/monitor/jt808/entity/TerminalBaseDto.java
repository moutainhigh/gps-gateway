package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity;

import lombok.Data;

/**
 * 终端响应基类
 * @param <T>
 * @author suibozhuliu
 */
@Data
public class TerminalBaseDto<T> {

    /**
     * 请求协议ID
     */
    private String reqProtocolId;
    /**
     * 响应协议ID
     */
    private String resProtocolId;
    /**
     * 响应Key,由终端号 + 响应序列号 + 协议ID组成,顺序一定不能变
     */
    private String responseKey;
    /**
     * 终端通用应答是否成功（0：成功/确认；1：失败；2：消息有误；3：不支持）
     */
    private Integer resultCode;
    /**
     * 数据详情
     */
    private T data;
    /**
     * 响应结果说明
     */
    private String resMsg;

}
