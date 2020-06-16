package com.zkkj.gps.gateway.datatransfer.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UDPserver端
 * @author suibozhuliu
 * @Description:
 * @Date: 2019-05-26 下午 1:45
 */
@Slf4j
public class UdpServer {

    // 定义一些常量
    private final int MAX_LENGTH = 1024 * 32; // 最大接收字节长度
    private final int PORT_NUM   = 12345;   // port号
    // 用以存放接收数据的字节数组
    private byte[] receMsgs = new byte[MAX_LENGTH];
    // 数据报套接字
    private DatagramSocket datagramSocket;
    // 用以接收数据报
    private DatagramPacket datagramPacket;

    public static void main(String[] args) {
        new UdpServer();
    }

    public UdpServer(){
        while (true) {
            try {
                /******* 接收数据流程**/
                // 创建一个数据报套接字，并将其绑定到指定port上
                datagramSocket = new DatagramSocket(PORT_NUM);
                // DatagramPacket(byte buf[], int length),建立一个字节数组来接收UDP包
                datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
                // receive()来等待接收UDP数据报
                System.out.println("接收阻塞.......");
                datagramSocket.receive(datagramPacket);

                /****** 解析数据报****/
                String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength(),"UTF-8");
                System.out.println("server Rece:" + receStr + "；接收时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                System.out.println("server Port:" + datagramPacket.getPort());

                /***** 返回ACK消息数据报*/
                // 组装数据报
                byte[] buf = "I receive the message".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(), datagramPacket.getPort());
                // 发送消息
                datagramSocket.send(sendPacket);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭socket
                if (datagramSocket != null) {
                    datagramSocket.close();
                }
            }
        }
    }

}
