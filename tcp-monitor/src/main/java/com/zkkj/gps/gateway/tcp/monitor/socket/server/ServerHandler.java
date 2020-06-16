package com.zkkj.gps.gateway.tcp.monitor.socket.server;

import com.zkkj.gps.gateway.protocol.P_BaseTerminalId;
import com.zkkj.gps.gateway.tcp.monitor.Constants;
import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.P8001ResForwardEvent;
import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.ProtocoleForwardEvent;
import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.TerminalConnChannelEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class ServerHandler extends SimpleChannelInboundHandler {
    //ChannelInboundHandlerAdapter

    //心跳回复
    @Autowired
    private P8001ResForwardEvent p8001ResForwardEvent;

    //协议转发
    @Autowired
    private ProtocoleForwardEvent protocoleForwardEvent;

    //终端Tcp通道存储
    @Autowired
    private TerminalConnChannelEvent terminalConnChannelEvent;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        try {
            byte[] resBytes = (byte[]) msg;
            //todo:协议解析，根据协议ID，进行反射得到协议模型进行解析
            P_BaseTerminalId terminalPotocol = analysisProtocol(resBytes);
            String terminalId = terminalPotocol.getMessageHeader().getTerminalId();
            int serialNumber = terminalPotocol.getMessageHeader().getSerialNumber();
            Constants.getInstance().setResSerialnumber(serialNumber);
            String msgIdStr = terminalPotocol.getMessageHeader().getMessageIdStr();
            /*LoggerUtils.info(log,terminalId,"[终端序号]" + terminalId +
                    "\t[时间]" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                    "\t[类型][接收 <- 终端数据] + \n");
            LoggerUtils.info(log,terminalId,"[十六进制]" + EncoderUtils.bytesToHex(resBytes) + "\n");*/
            //TODO: 记录终端TCP连接
            terminalConnChannelEvent.setCtx(ctx);
            terminalConnChannelEvent.setTerminalId(terminalId);
            EventBus.getDefault().post(terminalConnChannelEvent);
            //TODO: 协议转发
            protocoleForwardEvent.setMsgIdStr(msgIdStr);
            protocoleForwardEvent.setResByte(resBytes);
            EventBus.getDefault().post(protocoleForwardEvent);
            //TODO: 平台统一回复
            if (msgIdStr.equals("0200") || msgIdStr.equals("0210")) {
                p8001ResForwardEvent.setTerminalId(terminalId);
                p8001ResForwardEvent.setMsgID(512);
                EventBus.getDefault().post(p8001ResForwardEvent);
            }
        } catch (Exception e) {
            //LoggerUtils.error(log, e);
        } /*finally {
            ReferenceCountUtil.release(msg);
        }*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    /**
     * 协议解析处理
     * @param requestBytes 已匹配的二进制协议内容
     * @return 设备终端ID
     * @throws Exception
     */
    private P_BaseTerminalId analysisProtocol(byte[] requestBytes) throws Exception {
        /*P_BaseTerminalId p_baseTerminalId = new P_BaseTerminalId(requestBytes);
        String terminalId = p_baseTerminalId.getMessageHeader().getTerminalId();
        String msgId = p_baseTerminalId.getMessageHeader().getMessageIdStr();
        String reqStr = EncoderUtils.bytesToHex(requestBytes);
        LoggerUtils.info(log,terminalId,"【protocol :" + reqStr + "】\n");
        LoggerUtils.info(log,terminalId,"【终端返回的协议前期处理 :" + p_baseTerminalId.toString() + "】\n");*/
        return  new P_BaseTerminalId(requestBytes);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
        ctx.close();
    }

}
