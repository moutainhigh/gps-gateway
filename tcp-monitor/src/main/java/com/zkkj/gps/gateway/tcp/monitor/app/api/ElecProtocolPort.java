package com.zkkj.gps.gateway.tcp.monitor.app.api;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 5:42
 */

import com.zkkj.gps.gateway.protocol.component.common.BaseCompose;
import com.zkkj.gps.gateway.tcp.monitor.app.CommandInterface;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zkkjgs
 * 命令发送对象
 */
@Slf4j
@Component
public class ElecProtocolPort extends ElecProtocolBase implements CommandInterface {

    @Override
    public boolean sendCommandImpl(BaseCompose compose) {
        boolean isSend = false;
        String terminalId = compose.getMessageHeader().getTerminalId();
        try {
            ChannelHandlerContext ctx = getChannelContext(terminalId);
            if (ctx != null && ctx.channel().isActive() && ctx.channel().isOpen()) {
                /*LoggerUtils.info(log,terminalId,"[终端序号]" + terminalId +
                        "\t[时间]" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                        "\t[类型][下发 -> 终端数据]");
                LoggerUtils.info(log,terminalId,"[十六进制]" + EncoderUtils.bytesToHex(compose.encoder()) + "\n");*/
                isSend = true;
                ctx.channel().writeAndFlush(compose.encoder());
                ReferenceCountUtil.release(compose.encoder());
            }
        } catch (Exception e){
            LoggerUtils.error(log,terminalId, e.toString());
        }
        return isSend;
    }

}
