package com.zkkj.gps.gateway.protocol.component.messagebody.parameter;

import com.zkkj.gps.gateway.protocol.util.HexStringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author chailixing
 * 2019/4/16 9:41
 * 参数类型为字节数组
 */
public final class Param4IntArr extends BaseParameter {
    public Param4IntArr(byte[] bytes) throws Exception {
        super(bytes);
    }

    /*public Param4IntArr(int id, int[] paramValue) throws Exception {
        super(id, paramValue.length);
        StringBuilder sb = new StringBuilder();
        for (int aParamValue : paramValue) {
            sb.append(aParamValue);
        }
        this.paramValue = sb.toString();
    }*/

    public Param4IntArr(int id, String paramValue) throws Exception {
        super(id, paramValue.length());
        this.paramValue = paramValue;
    }

    public Param4IntArr() {
    }

    /**
     * 参数值
     */
    private String paramValue;

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
        String charset = "UTF-8";
        try {
            return paramValue.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("参数字符集异常！");
        }
    }

    @Override
    public String getParamValue() {
        return paramValue;
    }
}