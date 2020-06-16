package com.zkkj.gps.gateway.jt808tcp.monitor.codec;

import com.zkkj.gps.gateway.jt808tcp.monitor.annotation.Property;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.PropertyUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.bean.BeanUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.commons.transform.BcdUtils;
import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.Handler;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.HandlerMapper;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractBody;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType.*;

/**
 * 基础消息解码
 * @author suibozhuliu
 */
@Slf4j
public abstract class MessageDecoder extends ByteToMessageDecoder {

    private HandlerMapper handlerMapper;

    public MessageDecoder() {
    }

    public MessageDecoder(HandlerMapper handlerMapper) {
        this.handlerMapper = handlerMapper;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        /*String hexDump = ByteBufUtil.hexDump(in);
        System.out.println("接收到的十六进制报文：【" + hexDump + "】");*/
        //批量定位：1796
        int type = getType(in);
        Handler handler = handlerMapper.getHandler(type);
        if (handler == null) {
            return;
        }
        Type[] types = handler.getTargetParameterTypes();
        if (types[0] instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl clazz = (ParameterizedTypeImpl) types[0];
            Class<? extends AbstractBody> bodyClass = (Class<? extends AbstractBody>) clazz.getActualTypeArguments()[0];
            Class<? extends AbstractMessage> messageClass = (Class<? extends AbstractMessage>) clazz.getRawType();
            AbstractMessage<? extends AbstractBody> msg = decode(in, messageClass, bodyClass, type);
            out.add(msg);
        } else {
            AbstractMessage<? extends AbstractBody> msg = decode(in, (Class) types[0], null, type);
            out.add(msg);
        }
        in.skipBytes(in.readableBytes());
    }

    /** 解析 */
    public <T extends AbstractBody> AbstractMessage<T> decode(ByteBuf buf, Class<? extends AbstractMessage> clazz, Class<T> bodyClass , int type) {
        buf = unEscape(buf);
        if (check(buf)){
            log.error("校验码错误：【" + ByteBufUtil.hexDump(buf) + "】");
        }
        AbstractMessage message = decode(buf, clazz);
        if (type == 1796){
            return message;
        }
        if (bodyClass != null) {
            Integer headerLength = message.getHeaderLength();
            buf.setIndex(headerLength, headerLength + message.getBodyLength());
            T body = decode(buf, bodyClass);
            message.setBody(body);
        }
        return message;
    }

    public <T> T decode(ByteBuf buf, Class<T> targetClass) {
        try {
            /*T result ;
            if (targetClass.getName().equals(JT_0704.class.getName()) ||
                    targetClass.getName().equals(JT_0704.Item.class.getName())){
                if (ObjectUtils.isEmpty(objCacheMap.get(targetClass.getName()))){
                    result = BeanUtils.newInstance(targetClass);
                    objCacheMap.put(targetClass.getName(),result);
                } else {
                    result = (T) objCacheMap.get(targetClass.getName());
                }
            } else {
                result = BeanUtils.newInstance(targetClass);
            }*/

            T result = BeanUtils.newInstance(targetClass);
            PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptor(targetClass);
            for (PropertyDescriptor pd : pds) {
                Method readMethod = pd.getReadMethod();
                Property prop = readMethod.getDeclaredAnnotation(Property.class);
                int length = PropertyUtils.getLength(result, prop);
                if (!buf.isReadable(length)){
                    break;
                }
                if (length == -1){
                    length = buf.readableBytes();
                }
                Object value = null;
                try {
                    value = read(buf, prop, length, pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BeanUtils.setValue(result, pd.getWriteMethod(), value);
            }
            return result;
        } catch (Exception e) {
            log.error("解码异常：【" + e + "】");
            return null;
        }
    }

    public Object read(ByteBuf buf, Property prop, int length, PropertyDescriptor pd) {
        DataType type = prop.type();

        if (type == BYTE) {
            return (int) buf.readUnsignedByte();
        } else if (type == WORD) {
            return buf.readUnsignedShort();
        } else if (type == DWORD) {
            if (pd.getPropertyType().isAssignableFrom(Long.class))
                return buf.readUnsignedInt();
            return (int) buf.readUnsignedInt();
        } else if (type == STRING) {
            return buf.readCharSequence(length, Charset.forName(prop.charset())).toString().trim();
        } else if (type == OBJ) {
            return decode(buf.readSlice(length), pd.getPropertyType());
        } else if (type == LIST) {
            List list = new ArrayList();
            try {
                Type clazz = ((ParameterizedType) pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
                ByteBuf slice = buf.readSlice(length);
                while (slice.isReadable())
                    list.add(decode(slice, (Class) clazz));
            } catch (Exception e){
                log.error("List数据结构解码异常：【" + e + "】");
            }
            return list;
        }

        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        if (type == BCD8421)
            return BcdUtils.encode8421String(bytes).trim();
        return bytes;
    }

    /** 获取消息类型 */
    public abstract int getType(ByteBuf buf);

    /** 反转义 */
    public abstract ByteBuf unEscape(ByteBuf buf);

    /** 校验 */
    public abstract boolean check(ByteBuf buf);

}