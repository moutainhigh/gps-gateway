package com.zkkj.gps.gateway.jt808tcp.monitor.commons.transform;

import io.netty.buffer.ByteBuf;

/**
 * 字节缓冲工具类
 * @author suibozhuliu
 */
public class ByteBufUtils {

    /**
     * BCC校验(异或校验)
     */
    public static byte bcc(ByteBuf byteBuf) {
        byte cs = 0;
        while (byteBuf.isReadable())
            cs ^= byteBuf.readByte();
        byteBuf.resetReaderIndex();
        return cs;
    }
}