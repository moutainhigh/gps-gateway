package com.zkkj.gps.gateway.datatransfer.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * udp客户端
 * @author suibozhuliu
 * @Description:
 * @Date: 2019-06-04 上午 9:25
 */
@Slf4j
public class UdpClient {
    /**
     * 发送和接收数据包的套接字
     */
    private static DatagramSocket datagramSocket;
    /**
     * 获取IP地址对象
     */
    private static InetAddress inetAddress;
    /**
     * 数据包
     */
    private DatagramPacket datagramPacket;
    /**
     * Udp服务端地址
     */
    private static String udpAddress;
    /**
     * Udp服务端端口
     */
    private static int udpPort;

    private static UdpClient instance = null;

    private UdpClient() {}

    public static synchronized UdpClient getInstance(String address,int port){
        if (instance == null){
            instance = new UdpClient();
        }
        udpAddress = address;
        udpPort = port;
        initConstruct();
        return instance;
    }

    private static void initConstruct() {
        try {
            System.out.println("UdpClientdatagramSocket的值：" + datagramSocket + "inetAddress的值：" + inetAddress);
            if (datagramSocket == null || inetAddress == null){
                System.out.println("UdpClient需要初始化......");
                datagramSocket = new DatagramSocket();
                inetAddress = InetAddress.getByName(udpAddress);
            } else {
                System.out.println("UdpClient无需初始化......");
            }
        } catch (Exception e) {
            log.error("UdpClient初始化异常：【" + e + "】");
        }
    }

    /**
     * udp消息发送
     * @param dataPackage
     */
    public void sendMessage(String dataPackage){
        // 发送数据
        try {
            byte[] buf = dataPackage.getBytes("utf-8");
            datagramPacket = new DatagramPacket(buf, buf.length, inetAddress, udpPort);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            log.error("UdpClient数据发送异常：【" + e + "】");
        }
    }

}
