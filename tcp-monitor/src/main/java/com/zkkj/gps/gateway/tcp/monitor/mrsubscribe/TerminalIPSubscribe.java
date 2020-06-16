package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.TerminalConnChannelEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

/**
 * @author zkkjgs
 * 记录终端IP端口订阅服务
 * 线程安全对象可能导致的性能问题
 */
@Slf4j
@Component
public class TerminalIPSubscribe {

    public static Hashtable<String, ChannelHandlerContext> terminalCtxMap = new Hashtable<>();

    public TerminalIPSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(TerminalConnChannelEvent arg) {
        String terminalId = arg.getTerminalId();
        if (terminalCtxMap.containsKey(terminalId)){
            ChannelHandlerContext ctx = terminalCtxMap.get(terminalId);
            if (ctx != arg.getCtx()) {
                ctx.close();
                terminalCtxMap.put(terminalId, arg.getCtx());
            }
        } else {
            terminalCtxMap.put(terminalId, arg.getCtx());
        }
    }
}
