package com.zkkj.gps.gateway.jt808tcp.monitor.server.tcp;

import com.zkkj.gps.gateway.jt808tcp.monitor.handler.TCPServerHandler;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.codec.JT808MessageDecoder;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.codec.JT808MessageEncoder;
import com.zkkj.gps.gateway.jt808tcp.monitor.mapping.HandlerMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Tcp服务启动类
 * @author suibozhuliu
 */
@Slf4j
@Component
public class TCPServer {

    //1 创建2个线程，一个是负责接收客户端的连接。一个是负责进行数据传输的
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    //工作线程组
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    //2 创建服务器辅助类
    private static final ServerBootstrap serverBootstrap = new ServerBootstrap();
    //Tcp地址
    @Value("${gps_host}")
    private String gpsHost;
    //Tcp端口
    @Value("${gps_port}")
    private int gpsPort;
    //分隔符
    private byte delimiter;

    private HandlerMapper handlerMapper;

    @Autowired
    private TCPServerHandler tcpServerHandler;

    //Socket套接字对象封装
    private InetSocketAddress address = null;

    /**
     * 参数设置
     * @param delimiter
     * @param handlerMapper
     */
    public void setParames(byte delimiter, HandlerMapper handlerMapper){
        this.delimiter = delimiter;
        this.handlerMapper = handlerMapper;
        this.address = new InetSocketAddress(gpsHost, gpsPort);
    }

    /**
     * 开启服务
     */
    public void startService() {
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 10240)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//维持链接的活跃，清除死链接
                    .childOption(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline.addLast("idleStateHandler", new IdleStateHandler(30, 0, 0, TimeUnit.MINUTES));//心跳机制
                            // 1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
                            pipeline.addLast(new DelimiterBasedFrameDecoder(2 * 1024, Unpooled.wrappedBuffer(new byte[]{delimiter}), Unpooled.wrappedBuffer(new byte[]{delimiter, delimiter})));
                            pipeline.addLast("decoder", new JT808MessageDecoder(handlerMapper));
                            pipeline.addLast("encoder", new JT808MessageEncoder());
                            pipeline.addLast(tcpServerHandler);
                        }
                    });
            //绑定连接
            ChannelFuture channelFuture = serverBootstrap.bind(this.address).sync();
            log.info("TCP服务启动完毕,port={}", this.gpsPort);
            //等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("TCP服务出现异常,e={}", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
