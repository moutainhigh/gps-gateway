package com.zkkj.gps.gateway.tcp.monitor.socket.server;

import com.zkkj.gps.gateway.protocol.constant.ProtocolConsts;
import com.zkkj.gps.gateway.protocol.util.BitOperator;
import com.zkkj.gps.gateway.tcp.monitor.utils.EncoderUtils;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class GPSSocketService{

    @Autowired
    private ServerHandler serverHandler;

    @Value("${gps_host}")
    private String gps_host;

    @Value("${gps_port}")
    private int gps_port;

    //1 创建2个线程，一个是负责接收客户端的连接。一个是负责进行数据传输的
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    //2 创建服务器辅助类
    private static final ServerBootstrap serverBootstrap = new ServerBootstrap();
    //开始符
    private static ByteBuf[] byteBufs = new ByteBuf[ProtocolConsts.getProtocolConsts().length];
    // 结束符
    private static final byte[] endBuffs = EncoderUtils.hexToByteArray("7E");

    /**
     * 初始化Tcp监听服务
     */
    public void initTcpService(){
        LoggerUtils.info(log,"Tcp监听","【TCP服务IP：" + gps_host + "】");
        LoggerUtils.info(log,"Tcp监听","【TCP服务端口：" + gps_port + "】");
        if (gps_host == null || gps_host.length() == 0 || gps_port == 0){
            LoggerUtils.error(log,"Tcp监听" , "【"  + gps_host + "；port：" + gps_port + "】");
            return;
        }
        byteBufs = buildDelimiter();
        InetSocketAddress address = new InetSocketAddress(gps_host, gps_port);
        startService(address);
    }

    /**
     * 启动Tcp监听服务
     * @param address
     */
    private void startService(InetSocketAddress address) {
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 10240)//线程全满时，临时存放连接队列数（连接缓冲池的大小）
                    /*.option(ChannelOption.SO_SNDBUF, 32 * 1024)//发送的系统缓冲buf大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)//接收的系统缓冲buf大小
                    .option(ChannelOption.SO_KEEPALIVE, true)    //保持连接*/
                    //.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//维持链接的活跃，清除死链接
                    .childOption(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ByteBuf delimiter = Unpooled.copiedBuffer("7E".getBytes());
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new ByteArrayEncoder());//byte数组接收数据类型
                            pipeline.addLast(new ByteArrayDecoder());
                            //pipeline.addLast(new DelimiterBasedFrameDecoder(1024 * 32,delimiter));
                            //pipeline.addLast(buildTDelimiterBasedFrameDecoder());
                            pipeline.addLast(new TDelimiterBasedFrameDecoder(1024, byteBufs, Unpooled.copiedBuffer(endBuffs)));
                            pipeline.addLast(serverHandler); // 客户端触发操作
                        }
                    });
            //4 绑定连接
            ChannelFuture channelFuture = serverBootstrap.bind(address).sync();
            System.out.println("tcpserver start listen at " + address.getPort());
            //等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LoggerUtils.error(log, e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private TDelimiterBasedFrameDecoder buildTDelimiterBasedFrameDecoder() {
        ByteBuf[] bbs = new ByteBuf[ProtocolConsts.getProtocolConsts().length];
        byte[] endBuffs = EncoderUtils.hexToByteArray("7E");
        for (int i = 0; i < bbs.length; i++) {
            bbs[i] = Unpooled.copiedBuffer(BitOperator.integerTo3Bytes(0x7E0000 + ProtocolConsts.getProtocolConsts()[i]));
        }
        return new TDelimiterBasedFrameDecoder(1024, bbs, Unpooled.copiedBuffer(endBuffs));
    }

    /**
     * 构建分隔符
     * @return
     */
    private ByteBuf[] buildDelimiter(){
        for (int i = 0; i < byteBufs.length; i++) {
            byteBufs[i] = Unpooled.copiedBuffer(BitOperator.integerTo3Bytes(0x7E0000 + ProtocolConsts.getProtocolConsts()[i]));
        }
        return byteBufs;
    }

}
