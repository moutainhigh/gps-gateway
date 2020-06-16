package com.zkkj.gps.gateway.jt808tcp.monitor.handler;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ProtocolId;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.Message;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.GpsDataHandlerEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.PlateformResponseEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.TerminalConnChannelEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.Session;
import com.zkkj.gps.gateway.jt808tcp.monitor.session.SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static com.zkkj.gps.gateway.jt808tcp.monitor.commons.Constants.terminalCacheList;

/**
 * 消息处理类
 * @author suibozhuliu
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class TCPServerHandler extends SimpleChannelInboundHandler {
    //SimpleChannelInboundHandler
    //ChannelInboundHandlerAdapter

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${terminal_service}")
    private String terminalService;

    private final SessionManager sessionManager = SessionManager.getInstance();

    /**
     * 终端Tcp存储通道
     */
    @Autowired
    private TerminalConnChannelEvent terminalConnChannelEvent;

    /**
     * Gps定位数据处理通道
     */
    @Autowired
    private GpsDataHandlerEvent gpsDataHandlerEvent;

    /**
     * 平台统一响应处理通道
     */
    @Autowired
    private PlateformResponseEvent plateformResponseEvent;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Session session = Session.buildSession(ctx.channel());
        if (!ObjectUtils.isEmpty(session)){
            sessionManager.put(session.getId(), session);
            //log.info("终端连接【channelActive】；session：【" + session + "】");
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            AbstractMessage msgRequest = (AbstractMessage) msg;
            ProtocolId protocolId = ProtocolId.toEnum(msgRequest.getType());
            if (!ObjectUtils.isEmpty(msgRequest) && !ObjectUtils.isEmpty(protocolId)) {

                Message message = (Message) msgRequest;
                if (ObjectUtils.isEmpty(message) && StringUtils.isEmpty(message.getMobileNumber())) {
                    return;
                }
                String terminalId = message.getMobileNumber();
                if (!terminalCacheList.contains(terminalId)){
                    terminalCacheList.add(terminalId);
                    System.out.println("有新设备上线，设备号：【" + terminalId + "】，当前在线设备数：【" + terminalCacheList.size() + "】");
                }
                if (!StringUtils.isEmpty(terminalId)){
                    redisTemplate.opsForValue().set(terminalId, terminalService);
                }
                //记录终端TCP连接
                terminalConnChannelEvent.setCtx(ctx);
                terminalConnChannelEvent.setTerminalId(message.getMobileNumber());
                EventBus.getDefault().post(terminalConnChannelEvent);

                //Gps数据处理
                gpsDataHandlerEvent.setProtocolId(protocolId);
                gpsDataHandlerEvent.setMsgRequest(msgRequest);
                EventBus.getDefault().post(gpsDataHandlerEvent);

                //平台统一回复
                plateformResponseEvent.setCtx(ctx);
                plateformResponseEvent.setMsgRequest(msgRequest);
                plateformResponseEvent.setSessionManager(sessionManager);
                EventBus.getDefault().post(plateformResponseEvent);

            } else {
                return;
            }
        } finally {
            //ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String sessionId = Session.buildId(ctx.channel());
        sessionManager.removeBySessionId(sessionId);
        //log.info("断开连接【channelInactive】；session：【" + session + "】");
        ctx.close();
    }

    //服务器超时报错
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        //String sessionId = Session.buildId(ctx.channel());
        //Session session = sessionManager.getBySessionId(sessionId);
        //log.error("发生异常【exceptionCaught】；session：【" + session + "】；异常：【" + e + "】");
        ctx.close();
    }

    /*@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Session session = this.sessionManager.removeBySessionId(Session.buildId(ctx.channel()));
                log.info("服务器主动断开连接【userEventTriggered】；session：【" +  session + "】");
                ctx.channel().close();
                ctx.close();
            }
        }
    }*/
}