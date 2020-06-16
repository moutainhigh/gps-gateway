package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TerminalConnChannelEvent {

    private ChannelHandlerContext ctx;
    private String terminalId;

}
