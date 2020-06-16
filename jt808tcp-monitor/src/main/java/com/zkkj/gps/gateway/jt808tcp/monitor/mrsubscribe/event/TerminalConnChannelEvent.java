package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 终端Tcp通道连接事件
 * @author suibozhuliu
 */
@Data
@Component
public class TerminalConnChannelEvent {

    private ChannelHandlerContext ctx;
    private String terminalId;

}
