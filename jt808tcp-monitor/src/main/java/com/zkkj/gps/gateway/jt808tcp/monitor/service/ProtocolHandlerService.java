package com.zkkj.gps.gateway.jt808tcp.monitor.service;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ProtocolId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;

/**
 * 协议处理业务接口
 * @author suibozhuliu
 */
public interface ProtocolHandlerService {

    /**
     * 报文回传处理
     * @param protocolId
     * @param msgRequest
     */
    void terminalResponse(ProtocolId protocolId,AbstractMessage msgRequest);

}
