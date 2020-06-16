package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.Handler;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.HandlerMapper;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.PlateformResponseEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.Session;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * 平台统一回复订阅服务
 * @author suibozhuliu
 */
@Slf4j
@Component
public class PlateformResponseSubscribe {

    @Autowired
    private HandlerMapper handlerMapper;

    public PlateformResponseSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(PlateformResponseEvent arg) {
        try {
            AbstractMessage msgRequest = arg.getMsgRequest();
            ChannelHandlerContext ctx = arg.getCtx();
            SessionManager sessionManager = arg.getSessionManager();
            Channel channel = ctx.channel();
            Handler handler = handlerMapper.getHandler(msgRequest.getType());
            Type[] types = handler.getTargetParameterTypes();
            Session session = sessionManager.getBySessionId(Session.buildId(channel));
            AbstractMessage msgResponse;
            if (types.length == 1) {
                msgResponse = handler.invoke(msgRequest);
            } else {
                msgResponse = handler.invoke(msgRequest, session);
            }
            if (msgResponse != null) {
                //channel.writeAndFlush(msgResponse).sync();
                channel.writeAndFlush(msgResponse);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
