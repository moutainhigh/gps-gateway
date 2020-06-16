package com.zkkj.gps.gateway.tcp.monitor.socket.udp;

import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;

/**
 * udp客户端
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-04 上午 9:25
 */
@Slf4j
@Component
public class UdpClient {
    /**
     * 发送和接收数据包的套接字
     */
    private DatagramSocket datagramSocket;
    /**
     * 数据包
     */
    private DatagramPacket datagramPacket;
    /**
     * 获取IP地址对象
     */
    private InetAddress inetAddress;
    /**
     * Udp服务端地址
     */
    @Value("${udp_host}")
    private String udpAddress;
    /**
     * Udp服务端端口
     */
    @Value("${udp_port}")
    private int udpPort;

    @PostConstruct
    public void initConstruct() {
        try {
            datagramSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName(udpAddress);
        } catch (Exception e) {
            LoggerUtils.error(log,e.toString());
        }
    }

    /**
     * udp消息发送
     * @param dataPackage
     */
    public void sendMessage(String dataPackage){
        byte[] buf = dataPackage.getBytes(CharsetUtil.UTF_8);
        datagramPacket = new DatagramPacket(buf, buf.length, inetAddress, udpPort);
        // 发送数据
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            LoggerUtils.error(log,e.getMessage());
        }
    }

}
