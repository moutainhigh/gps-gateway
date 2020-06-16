package com.zkkj.gps.gateway.tcp.monitor.test.tcp.demo05;

import com.zkkj.gps.gateway.protocol.destination.P_0200;
import com.zkkj.gps.gateway.tcp.monitor.utils.EncoderUtils;

public class TestMain {
    public static void main(String[] args) {
        //String hexStr = "7e0200002d026043456182bf92000000000000000102173224068034c80000000000001908301134190104000078bf33262a4d30302c32392c164d7e";
        String hexStr = "7e021000ca0270364654334f8b600000010000000000020b4db006800ee000000000000019082903463601040000027233262a4d30302c32392c31303333333030303030475053434e303030303030303030302d2f3231303030475053434e263030303030303030303030302623220000010000000000020b4db006800ee0000000000000190829034643010400000272220000010000000000020b4db006800ee0000000000000190829034650010400000272220000010000000000020b4db006800ee0000000000000190829034657010400000272207e";
        byte[] bytes = EncoderUtils.hexToByteArray(hexStr);
        System.out.println();
        for (byte b:bytes) {
            System.out.print(b + " ");
        }
        System.out.println();
        try {
            P_0200 p_0200 = new P_0200(bytes);
            System.out.println(p_0200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
