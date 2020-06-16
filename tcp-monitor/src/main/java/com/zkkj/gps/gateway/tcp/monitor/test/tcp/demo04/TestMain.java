package com.zkkj.gps.gateway.tcp.monitor.test.tcp.demo04;

import com.zkkj.gps.gateway.protocol.destination.P_0200;
import com.zkkj.gps.gateway.tcp.monitor.utils.EncoderUtils;

public class TestMain {
    public static void main(String[] args) {
        /*String hexStr = "d4c1423030303031";
        byte[] bytess = {
                (byte) 0xd4,(byte) 0xc1,0x42,0x30,0x30,0x30,0x30,0x31,0x00
                //0x01
                //0x74 ,0x65 ,0x73 ,0x74 ,0x5F ,0x74 ,0x6F ,0x6B ,0x65 ,0x6E
        };
        byte[] bytes = HexStringUtils.parseByte(bytess,9);
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + "\t");
        }
        //String parseString = BCD8421Operator.bcd2ToString(bytes);
        String parseString = null;
        try {
            parseString = HexStringUtils.parseString(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(parseString);*/
        /**
         * 7e020000710270364654330007000000000000000202180b6406866df00000000100001908152059260104000001b533262a4d30302c32392c31303431333030303030475053434e2f33382f34302f33392f32392f33342f32362f32342d2f31392f34302f3431303030475053434e263030303030303030303030302623d27e
         * 7e0200004a027036487965d365000000000000000202160a2c068d7a2800000000000019081500003301040008b84133262a4d30302c32392c313034313530303030303030303030263030303030303030303030302623af7e
         * 7e0200007a0270364654330005000000000000000202180bd406866e200000000100001908152112360104000001b533262a4d30302c32392c31303431352303030475053434e263030303030303030303030302623c17e
         */
        String str = "7e0200004a027036487965d365000000000000000202160a2c068d7a2800000000000019081500003301040008b84133262a4d30302c32392c313034313530303030303030303030263030303030303030303030302623af7e";
        byte[] resByte = EncoderUtils.hexToByteArray(str);
        try {
            new P_0200(resByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
