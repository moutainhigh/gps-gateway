package com.zkkj.gps.gateway.jt808tcp.monitor.test.demo01;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test05 {
    public static void main(String[] args) {
        /*byte[]bytes = {-15,31,-1,-1,-127,-125,65,-66,-87,65,48,48,50,49,53,0,0,0,0,0,0,0,29,-14,47};
        //f11fffff818341bea9413030323135000000000000001df22f
        String bytesToHex = EncoderUtils.bytesToHex(bytes);
        System.out.println(bytesToHex.toString());
        String str = "bea9413030323135";
        byte[] toByteArray = EncoderUtils.hexToByteArray(str);
        System.out.println();
        for (byte b : toByteArray) {
            System.out.print(b + ",");
        }
        System.out.println();
        byte[] bs = {-66,-87,65,48,48,50,49,53};
        try {
            String gbk = HexStringUtils.parseString(bs, "GBK");
            System.out.println(gbk);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String str1 = "7e0200010c064875374944456e00000000000c00020216d027067c678c00000000009e19092914403301040018280f02020000030200002504000000002a02000030011ee10209c733cff33fffff8183c501313335343335313930393239303030350000000057323037693776303036c9c2c3bab9a9cffa00000000000000000000000000000000000000000000d5d5bdf0c3bab5e700000000000000000000000000000000000000000000c7e0b8dac6ba5fd5d5bdf0c3bab5e75f3139303900000000000000000000c9c24233393731340000000000000000000000c4adc3ba000000000000c4adc3ba00000000000033322e3034302e300000302e300000302e30000032302e300000303030c9c2c3bab9a9cffa32f44fc07e";
        System.out.println(str1);

        byte[] bytes1 = {-13,63,-1,-1,-127,-125,-59,1,49,53,48,49,50,49,57,48,50,49,52,48,48,48,49,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-47,-35,-54,-66,-71,-85,-53,-66,52,50,50,52,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-47,-35,-54,-66,-71,-85,-53,-66,52,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-78,-30,-54,-44,-41,-88,-45,-61,-74,-87,-75,-91,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-42,-48,67,48,48,48,51,53,0,0,0,0,0,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,-60,-83,-61,-70,0,0,0,0,0,0,51,50,46,53,56,48,46,48,0,0,48,46,48,0,0,48,46,48,0,0,53,53,46,48,0,0,49,48,49,-47,-35,-54,-66,-71,-85,-53,-66,35,-12,79};
        System.out.println(bytes1.length);
        System.out.println("得到的十六进制：【" + HexStringUtils.toHexString(bytes1) + "】");

        //String str2 = "7e:89:00:00:d0:01:37:29:79:54:74:e8:a7:f1:f1:1f:ff:ff:81:82:c5:41:31:30:33:31:31:39:30:39:32:39:30:30:36:36:00:00:00:00:00:00:57:32:30:37:69:37:76:30:30:34:ce:f7:b4:a8:d2:bb:bf:f3:5f:d5:d5:bd:f0:c3:ba:b5:e7:5f:31:39:30:39:00:00:00:00:00:00:00:00:d5:d5:bd:f0:c3:ba:b5:e7:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:d6:da:b0:ee:d4:cb:ca:e4:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:c9:c2:45:36:35:39:33:39:00:00:00:00:00:00:00:00:00:00:00:c4:ad:c3:ba:00:00:00:00:00:00:c4:ad:c3:ba:00:00:00:00:00:00:31:39:2e:30:32:30:00:00:00:00:30:00:00:00:00:30:00:00:00:00:03:00:00:00:00:00:00:00:00:d6:d0:bf:f3:bf:c6:bc:bc:86:f2:2f:dc:7e".replaceAll(":","");
        String str2 = "7e890000d0013729795474e8a7f1f11fffff8182c541313033313139303932393030363600000000000057323037693776303034cef7b4a8d2bbbff35fd5d5bdf0c3bab5e75f313930390000000000000000d5d5bdf0c3bab5e700000000000000000000000000000000000000000000d6dab0eed4cbcae400000000000000000000000000000000000000000000c9c24536353933390000000000000000000000c4adc3ba000000000000c4adc3ba00000000000031392e3032300000000030000000003000000000030000000000000000d6d0bff3bfc6bcbc86f22fdc7e";
        System.out.println(str2);
        System.out.println("得到的十六进制：【" + HexStringUtils.toHexString(bytes1) + "】");

        byte[] bytes2 = {-13,63,-1,-1,-127,-126,1,0,-18,-12,79};
        byte[] bytes3 = {-13,63,-1,-1,-127,-126,1,0,-18,-12,79};
        //F33FFFFF81820100EEF44F
        System.out.println("222222222222【" + HexStringUtils.toHexString(bytes2) + "】");
        System.out.println("333333333333【" + HexStringUtils.toHexString(bytes3) + "】");

        System.out.println(HexStringUtils.toHexString(HexStringUtils.stringToBytes("W207i7v004", 20, "GBK")));*/

        /*byte [] bytes4 = {-13,63,-1,-1,17,-18,9,8,-55,-62,65,48,48,48,48,57,45,-12,79};
        String str3 = HexStringUtils.toHexString(bytes4);
        System.out.println(str3);
        String str4 = "C9C2413030303039";
        byte[] bytes5 = EncoderUtils.hexToByteArray(str4);
        try {
            System.out.println(HexStringUtils.parseString(bytes5,"GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        //byte [] bytes6 = {-13,63,-1,-1,17,-35,1,1,18,-12,79};
        /*byte[] bytes7 = {-13,63,-1,-1,17,-35,0,20,-12,79};
        String str5 = HexStringUtils.toHexString(bytes7);
        System.out.println(str5);*/

        /*int i1 = 108873891;
        double i2 = i1 * 0.000001;
        System.out.println(i2);
        double i = 0.000001 * 1000000;
        System.out.println(i);*/

        /*String str = "190930092840";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime.toString());*/

        String datetime =  "20190930092840";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ldt = LocalDateTime.parse(datetime,dtf);
        System.out.println(ldt);//2014-02-12T11:10:12

        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime2 = ldt.format(fa);
        System.out.println(datetime2);

        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime);
        /*String text = "20190930092840";
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // 解析date+time
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                // 解析毫秒数
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .toFormatter();
        formatter.parse(text);
        System.out.println(formatter.toString());*/
    }
}
