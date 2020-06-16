package com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01;

import com.zkkj.gps.gateway.jt808tcp.monitor.utils.BitOperator;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.HexStringUtils;

import java.io.UnsupportedEncodingException;

public class Test04 {
    public static void main(String[] args) {
        getCarNumber();
    }

    private static void getCarNumber() {

        byte[] bytes = {50, 53, 53, 46, 50, 53, 53, 46, 50, 53, 53, 46, 50, 53, 53};
        try {
            String paramValue = HexStringUtils.parseString(bytes, "GBK");
            System.out.println(paramValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytes1 = HexStringUtils.stringToBytes("255.255.255.255", 15, "GBK");
        System.out.println();
        for (byte b : bytes1) {
            System.out.print(b + ", ");
        }
        System.out.println();
        byte[] bytePort = {0,0,0,59};
        int port = BitOperator.byteToInteger(bytePort);
        System.out.println(port);
    }
}
