package com.zkkj.gps.gateway.jt808tcp.monitor.codec;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.PropertyUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.bean.BeanUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.transform.BcdUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 基础消息编码
 * @author suibozhuliu
 */
public abstract class MessageEncoder<T extends AbstractBody> extends MessageToByteEncoder<AbstractMessage<T>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) {
        ByteBuf buf = encode(msg);
        out.writeByte(0x7e);
        out.writeBytes(buf);
        out.writeByte(0x7e);
        /*String hexDump = ByteBufUtil.hexDump(out);
        System.out.println("发送的十六进制报文：【" + hexDump + "】");*/
    }

    public ByteBuf encode(AbstractMessage<T> message) {
        AbstractBody body = message.getBody();

        ByteBuf bodyBuf = encode(Unpooled.buffer(256), body);

        message.setBodyLength(bodyBuf.readableBytes());

        ByteBuf headerBuf = encode(Unpooled.buffer(16), message);

        ByteBuf buf = Unpooled.wrappedBuffer(headerBuf, bodyBuf);

        buf = sign(buf);
        buf = escape(buf);

        return buf;
    }

    /** 转义 */
    public abstract ByteBuf escape(ByteBuf buf);

    /** 签名 */
    public abstract ByteBuf sign(ByteBuf buf);

    private ByteBuf encode(ByteBuf buf, Object body) {
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptor(body.getClass());

        for (PropertyDescriptor pd : pds) {

            Method readMethod = pd.getReadMethod();
            Object value = BeanUtils.getValue(body, readMethod);
            if (value != null) {
                Property prop = readMethod.getDeclaredAnnotation(Property.class);
                write(buf, prop, value);
            }
        }
        return buf;
    }

    public void write(ByteBuf buf, Property prop, Object value) {
        int length = prop.length();
        byte pad = prop.pad();

        switch (prop.type()) {
            case BYTE:
                buf.writeByte((int) value);
                break;
            case WORD:
                buf.writeShort((int) value);
                break;
            case DWORD:
                if (value instanceof Long) {
                    buf.writeInt(((Long) value).intValue());
                } else {
                    buf.writeInt((int) value);
                }
                break;
            case BYTES:
                buf.writeBytes((byte[]) value);
                break;
            case BCD8421:
                buf.writeBytes(BcdUtils.leftPad(BcdUtils.decode8421((String) value), length, pad));
                break;
            case STRING:
                byte[] strBytes = ((String) value).getBytes(Charset.forName(prop.charset()));
                if (length > 0) {
                    strBytes = BcdUtils.leftPad(strBytes, length, pad);
                }
                buf.writeBytes(strBytes);
                break;
            case OBJ:
                encode(buf, value);
                break;
            case LIST:
                List list = (List) value;
                for (Object o : list) {
                    encode(buf, o);
                }
                break;
        }
    }
}