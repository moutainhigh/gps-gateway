package com.zkkj.gps.gateway.ccs.dto.amqp;

/**
 * 事件、报警内部处理模型
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-23 上午 11:19
 */
public class RcvMonitorDto<T>{

    /**
     * 事件、报警数据模型
     */
    private T data;

    /**
     * 事件、报警标记；1：事件；2：报警
     */
    private int flag;

    public T getData() {
        return data;
    }

    /**
     * 第三方appKey
     */
    private String appKey;
    /**
     * 第三方appSecret
     */
    private String appSecret;

    public void setData(T data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "RcvMonitorDto{" +
                "data=" + data +
                ", flag=" + flag +
                ", appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                '}';
    }
}
