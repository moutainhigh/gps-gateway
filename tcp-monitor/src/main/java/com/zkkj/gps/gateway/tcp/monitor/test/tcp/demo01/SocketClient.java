package com.zkkj.gps.gateway.tcp.monitor.test.tcp.demo01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        try {
            // 建立连接
            Socket s = new Socket("39.105.207.6", 10001);

            // 读取键盘输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // 端口输出
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            String line = null;

            // 输入886结束输出
            while ((line = br.readLine()) != null) {
                if ("886".equals(line)) {
                    break;
                }
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
