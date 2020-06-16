package com.zkkj.gps.gateway.tcp.monitor.socket.server;

import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TDelimiterBasedFrameDecoder extends ByteToMessageDecoder {

    private final int maxFrameLength;
    private boolean discardingTooLongFrame;
    private int tooLongFrameLength;
    private final ByteBuf[] beginDelimiters;
    private final ByteBuf endDelimiter;
    /**
     * Set only when decoding with "\n" and "\r\n" as the delimiter.
     */
//    private final LineBasedFrameDecoder lineBasedDecoder;


    public TDelimiterBasedFrameDecoder(int maxFrameLength,ByteBuf[] beginDelimiters, ByteBuf endDelimiter) {
        validateMaxFrameLength(maxFrameLength);
        if (beginDelimiters == null) {
            throw new NullPointerException("beginDelimiter");
        }
        if(endDelimiter==null)
        {throw new NullPointerException("endDelimiter");}
        for (ByteBuf beginDelimiter : beginDelimiters) {
            validateDelimiter(beginDelimiter);
        }

        validateDelimiter(endDelimiter);
        this.beginDelimiters=beginDelimiters;
        this.endDelimiter=endDelimiter;
        this.maxFrameLength = maxFrameLength;
    }

    private static void validateDelimiter(ByteBuf delimiter) {
        if (delimiter == null) {
            throw new NullPointerException("delimiter");
        }
        if (!delimiter.isReadable()) {
            throw new IllegalArgumentException("empty delimiter");
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        try {
            ByteBuf decoded = decode(in);
            if (decoded != null) {
                out.add(decoded);
                decoded.release();
            }
        } catch (Exception e) {
            LoggerUtils.error(log,e.toString());
        }
    }

    /**
     * 根据帧头帧尾将二进数据进行截取处理需要的协议内容部分
     * @param buffs 接收到的所有数据内容
     * @return 根据帧头帧尾截取后的内容
     * @throws Exception
     */
    public  byte[] matchByteBuf(byte[] buffs) throws Exception {
        ByteBuf byteBuf= Unpooled.copiedBuffer(buffs);//转换为bytebuf
        ByteBuf byteBufRes= this.decode(byteBuf);//进行解析
        byte[] bytes=new byte[byteBufRes.writerIndex()];
        byteBufRes.getBytes(0,bytes);
        return  bytes;
    }

    /**
     * 根据帧头帧尾将二进数据进行截取处理需要的协议内容部分
     * 主要的截取算法
     * @param buffer 接收到的所有数据内容
     * @return 根据帧头帧尾截取后的内容
     * @throws Exception
     */
    protected ByteBuf decode(ByteBuf buffer) throws Exception {
        if (buffer instanceof EmptyByteBuf || buffer.readableBytes() < 0) {
            return null;
        }
        // Try all delimiters and choose the delimiter which yields the shortest frame.
//        int beginMinFrameLength = Integer.MAX_VALUE;
        int minFrameLength = Integer.MAX_VALUE;
        //获取帧头的开始索引以及帧尾的结束索引
        BeginEndIndex beginEndIndex= indexOf(buffer, this.beginDelimiters,this.endDelimiter);
        ByteBuf minDelim=null;
        if(beginEndIndex!=null&& beginEndIndex.getFrameLength() < minFrameLength) {
            minDelim = this.endDelimiter;
            minFrameLength=beginEndIndex.getFrameLength();
        }

        if (minDelim != null) {
            int minDelimLength = minDelim.capacity();
            //从已读位置到结束帧位置的长度
            int skipReadFrameLen= beginEndIndex.getEndIndex()+minDelimLength-buffer.readerIndex();

            ByteBuf frame;

            if (minFrameLength > maxFrameLength) {
                // Discard read frame.
                buffer.skipBytes(skipReadFrameLen);
//                fail(endMinFrameLength);
                return null;
            }

//                frame = buffer.readSlice(minFrameLength);


            frame = buffer.copy(beginEndIndex.getBeginIndex(), beginEndIndex.getFrameLength());

            buffer.skipBytes(skipReadFrameLen);


            return frame.retain();
        } else {

            //如果传入数据大于指定最大长度则丢弃此数据
            if (buffer.readableBytes() > maxFrameLength) {
                // Discard the content of the buffer until a delimiter is found.

                buffer.skipBytes(buffer.readableBytes());

            }

            return null;
        }
    }

    private static void validateMaxFrameLength(int maxFrameLength) {
        if (maxFrameLength <= 0) {
            throw new IllegalArgumentException(
                    "maxFrameLength must be a positive integer: " +
                            maxFrameLength);
        }
    }

    private boolean isSubclass() {
        return !getClass().equals(DelimiterBasedFrameDecoder.class);
    }

    /***
     * 根据多个帧头，及帧尾，获取数据内有效的协议内容
     * @param haystack 需要处理的二进制数据
     * @param beginDelimiters 多个帧头
     * @param endDelimiter 单个帧尾
     * @return
     */
    private static BeginEndIndex indexOf(ByteBuf haystack, ByteBuf[] beginDelimiters,ByteBuf endDelimiter) {
        byte[] bytes = new byte[haystack.writerIndex()];
        haystack.getBytes(0, bytes);
        int beginIndex = -1;
        int endIndex = -1;

        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {

            if (beginIndex == -1) {
                for (ByteBuf beginDelimiter : beginDelimiters) {
                    ByteBuf needle = beginDelimiter;
                    beginIndex=   match(needle,haystack,i);
                    if(beginIndex!=-1) {
                        break;
                    }
                }
            }
            else if (endIndex == -1&&beginIndex!=-1) {
                ByteBuf needle = endDelimiter;
                endIndex = match(needle, haystack, i);
                if(endIndex!=-1) {
                    return new BeginEndIndex(beginIndex, endIndex, endDelimiter.capacity());
                }
            }

//                return i - haystack.readerIndex();


        }
        return null;
    }

    /**
     * 匹配帧头帧尾
     * @param needle
     * @param haystack
     * @param i
     * @return
     */
    private   static int  match(ByteBuf needle,ByteBuf haystack,int i)
    {
        int index=-1;
        int haystackIndex = i;
        int needleIndex;
        //匹配第一个针头
        for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex++) {
            if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                break;
            } else {
                haystackIndex++;
                if (haystackIndex == haystack.writerIndex() &&
                        needleIndex != needle.capacity() - 1) {
                    return index;
                }
            }

        }

        if (needleIndex == needle.capacity()) {
            // Found the needle from the haystack!
            if (index == -1) {
                index = i;
                return index;
            }
        }

        return index;
    }

    private void fail(long frameLength) {
        if (frameLength > 0) {
            throw new TooLongFrameException(
                    "frame length exceeds " + maxFrameLength +
                            ": " + frameLength + " - discarded");
        } else {
            throw new TooLongFrameException(
                    "frame length exceeds " + maxFrameLength +
                            " - discarding");
        }
    }
}

