package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 协议处理工具类
 * @author suibozhuliu
 */
public class ElecProtocolUtil {

    /**
     * Convert a string of hex data into a byte array.
     * Original author is:
     *
     * @param s The hex string to convert
     * @return An array of bytes with the values of the string.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String byte2hex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String temp = Integer.toHexString(((int) data[i]) & 0xFF);
            for (int t = temp.length(); t < 2; t++) {
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] src, int offset, int length) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = offset; i < length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(List<Byte> btsList, int offset,int size) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (btsList == null || btsList.size() <= 0) {
            return null;
        }
        for (int i = offset; i < btsList.size(); i++) {
            int v = btsList.get(i) & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Byte 2 byte
     *
     * @param oBytes
     * @return
     */
    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static double string2Double(String value) {
        try {
            double xx = Double.parseDouble(value);
            return xx;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 字符串转int
     *
     * @param value
     * @return
     */
    public static int string2Int(String value) {
        if (value == "")
            return 0;
        try {
            int xx = Integer.parseInt(value);
            return xx;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 将字节列表转换为汉字
     *
     * @param btsList
     * @param offset
     * @param length
     * @return
     */
    public static String btsList2GBKString(List<Byte> btsList, int offset, int length) {
        if (btsList == null || btsList.size() < length)
            return "";


        Byte[] tmpByte = (Byte[]) btsList.subList(offset, offset + length).toArray(new Byte[length]);
        byte[] bts = new byte[length];
        bts = ElecProtocolUtil.toPrimitives(tmpByte);
        String strBack = "";
        try {
			/* String sendString="测陕B4489";
			byte[] sendBytes= sendString .getBytes("GBK");
			String recString=new String( sendBytes ,"GBK");
			*/
            strBack = new String(bts, "GBK");
        } catch (Exception e1) {
            e1.printStackTrace();
            strBack = "";
        }
        return strBack.replaceAll("\\s", "").trim();
    }

    /**
     * 判断输入的字符串参数是否为空
     *
     * @return boolean 空则返回true,非空则flase
     */
    public static boolean isEmptyString(String input) {
        return null == input || 0 == input.length() || 0 == input.replaceAll("\\s", "").length();
    }

    /**
     * 字符串转为字节数组
     * 该方法默认以GBK转码
     * 若想自己指定字符集,可以使用<code>getBytes(String str, String charset)</code>方法
     */
    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }

    /**
     * 字符串转为字节数组
     * 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
     */
    public static byte[] getBytes(String data, String charset) {
        data = (data == null ? "" : data);
        if (isEmptyString(charset)) {
            return data.getBytes();
        }
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            // LogUtil.getLogger().error("将字符串[" + data + "]转为byte[]时发生异常:系统不支持该字符集[" + charset + "]");
            return data.getBytes();
        }
    }

    /**
     * gbk字符串转字节码
     *
     * @param s
     * @return
     */
    public static byte[] gbk2Bytes(String s) {
        try {
            return s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public static List<Byte> gbk2BytesWithFixLength(String s, int fixLength) {
        try {
            byte[] xx = s.getBytes("GBK");
            List<Byte> finalByte = new ArrayList<>();
            for (int i = 0; i < fixLength; i++) {
                if (i < xx.length)
                    finalByte.add(xx[i]);
                else
                    finalByte.add((byte) 0x00);
            }
            return finalByte;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public static String hex2GBKString(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                return "";
            }
        }
        try {
            s = new String(baKeyword, "GBK");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 合并数组
     *
     * @param al
     * @param bl
     * @return
     */
    public String[] getMergeArray(String[] al, String[] bl) {
        String[] a = al;
        String[] b = bl;
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] getMergeArray(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

}