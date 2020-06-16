package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 协议转发事件类
 */
@Data
@Component
public class ProtocoleForwardEvent {

    private String msgIdStr;
    private byte[] resByte;

}
