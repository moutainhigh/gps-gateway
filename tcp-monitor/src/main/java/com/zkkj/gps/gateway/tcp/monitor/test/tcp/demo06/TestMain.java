package com.zkkj.gps.gateway.tcp.monitor.test.tcp.demo06;

public class TestMain {
    public static void main(String[] args) {

        /*String str = "0152514405";
        int a ,b,c ,d,f;
        a = 99; b=2;c=3;d=4;f=5;
        byte[] bytes1 = BitOperator.integerTo2Bytes(a);
        for (byte b1 : bytes1) {
            System.out.print(b1 + ",");
        }
        System.out.println();
        String charset = "GBK";
        byte[] bytes = new byte[0];
        try {
            bytes = HexStringUtils.parseByte(str.getBytes(charset), 10);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (byte aByte : bytes) {
            System.out.print(aByte + ",");
        }
        System.out.println();
        try {
            String exceptionStr = HexStringUtils.parseString(Arrays.copyOfRange(bytes, 0, 10), charset);
            System.out.println(exceptionStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        String hexStr = "7e02000137057047126435028f00000000000000010209cd54067d0141c00000000000000101010000010104000000e336c44e3132353435000000000000000000000000000054313539353238000000cef7b4a8bff3000000000000000000000000000000000000000000000000cdadb4a8d5d5bdf0b5e7b3a7000000000000000000000000000000000000ccd9d4adcdd8baa300000000000000000000000000000000000000000000bea941303030303000cdf5c2e9d7d300000000beabc3ba000000000000c3baccbf00000000000031342e323131352e323331322e320031322e353634302e320000313131d6d0bff3bfc6bcbc352501cc330000283390f3320000438c90f332000061329182320000199490f33200005b4190f333262a4d30302c32392c3130343137303030303030303030302630303030303030303030303026235f7e";
        System.out.println(hexStr.length());
    }
}
