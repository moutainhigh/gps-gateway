package com.zkkj.gps.gateway.tcp.monitor.test.tcp.demo01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) {
        try {
            System.out.println("---------------------服务开启-----------------------");
            // 建立端口，监听新的请求
            ServerSocket ss = new ServerSocket(10001);
            Socket s = ss.accept();

            // 读取客户端输入的信息
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            /*String line;
            // 进行输出
            while ((line = br.readLine()) != null) {
                System.out.println("-----客户端输入的信息是-----");
                System.out.println(line);
            }*/
            int count = 0;
            while (count == 0){
                System.out.println("-----客户端输入的信息是-----");
                count = br.read();
                System.out.println(count);
            }
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
