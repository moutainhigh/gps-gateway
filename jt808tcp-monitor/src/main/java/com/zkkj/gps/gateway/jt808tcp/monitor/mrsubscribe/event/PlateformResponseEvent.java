package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event;

import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 平台统一响应事件
 * @author suibozhuliu
 */
@Data
@Component
public class PlateformResponseEvent {

    private AbstractMessage msgRequest;

    private ChannelHandlerContext ctx;

    private SessionManager sessionManager;

}
