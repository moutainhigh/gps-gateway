package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author zkkjgs
 * 2019/6/30 9:41
 * 参数类型为字节数组，长度不足补0
 */
public final class Param4IntUtf8Arr extends BaseParameter {
    public Param4IntUtf8Arr(byte[] bytes) throws Exception {
        super(bytes);
    }

    public Param4IntUtf8Arr(int id, String paramValue,int len) throws Exception {
        super(id, paramValue.length());
        this.paramValue = paramValue;
        this.length = len;
    }

    public Param4IntUtf8Arr() {
    }

    /**
     * 参数值
     */
    private String paramValue;
    /**
     * 参数长度
     */
    private int length;

    @Override
    protected String subToString() {
        return " 参数值:" + paramValue + "}";
    }

    @Override
    protected void subDecoder(byte[] bytes) throws Exception {
        paramValue = HexStringUtils.parseString(bytes, "UTF-8");
    }

    @Override
    protected byte[] subEncoder() {
        String encodeType = "UTF-8";
        try {
            byte[] xx= paramValue.getBytes(encodeType);
            byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                if (i < xx.length){
                    bytes[i] = xx[i];
                } else {
                    bytes[i] = (byte)0x00;
                }
            }
            return bytes;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("参数字符集异常！");
        }
    }

    @Override
    public String getParamValue() {
        return paramValue;
    }
}