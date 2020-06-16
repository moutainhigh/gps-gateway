package com.zkkj.gps.gateway.tcp.monitor.test.md5;

import com.zkkj.gps.gateway.protocol.util.DataUtils;

import java.io.IOException;
import java.util.UUID;

public class TestMain {
    public static void main(String[] args) throws IOException {
        //String str = "PTMS190712163407563";
        //System.out.println("获取的短字节的数据：" + getShortUrl(str));
        calculate();
    }

    private static void calculate() {
        double d1 = 12.5;
        double d2 = 12.3;
        double result = d1 - d2;
        System.out.println(DataUtils.getDoublePoint(result));
        double result1 = Double.parseDouble(DataUtils.getDoublePoint(Math.abs(d2 - d1)));
        System.out.println(result1);
    }

    /**
     * 短字符串生成方法
     * 这个方法会,生成四个短字符串,每一个字符串的长度为5
     * @param str
     * @return
     */
    private static String[] shortUrl(String str) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = UUID.randomUUID().toString();
        //混淆key,加上当前时间,并且取一个随机字符串
        key = System.currentTimeMillis() + key;
        // 要使用生成 加密 的字符
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
        };

        // 对传入网址进行 MD5 加密
        String sMD5EncryptResult = MD5Util.encode(key + str);
        System.out.println("加密后的字符串：" + sMD5EncryptResult);
        System.out.println("加密前的字符串：" + key + str);
        String hex = sMD5EncryptResult;
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 5; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    /**
     * 获取我想要的字符串,将生成的两个相加,得到我想要的12位字符
     * @param str
     * @return
     */
    public static String getShortUrl(String str){
        String[] aResult = shortUrl(str);
        return aResult[0] + aResult[1];
    }

}
