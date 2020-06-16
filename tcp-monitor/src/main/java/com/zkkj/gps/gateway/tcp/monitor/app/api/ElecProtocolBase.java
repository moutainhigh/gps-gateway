package com.zkkj.gps.gateway.tcp.monitor.app.api;

import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.TerminalIPSubscribe;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Hashtable;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 11:35
 */
@Component
public class ElecProtocolBase {

    @Autowired
    private TerminalIPSubscribe terminalIPSubscribe;

    /**
     * 获取消息通道对象
     */
    public ChannelHandlerContext getChannelContext(String terminalId){
        ChannelHandlerContext ctx = null;
        Hashtable<String, ChannelHandlerContext> terminalContext = terminalIPSubscribe.terminalCtxMap;
        if (!CollectionUtils.isEmpty(terminalContext) && terminalContext.containsKey(terminalId)){
            ctx = terminalContext.get(terminalId);
        }
        return ctx;
    }

}
