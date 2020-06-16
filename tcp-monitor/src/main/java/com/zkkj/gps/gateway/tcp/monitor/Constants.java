package com.zkkj.gps.gateway.tcp.monitor;

/**
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-20 下午 4:17
 */

/**
 * 数据保存
 */
public class Constants {

    /**
     * 请求流水号
     */
    private int reqSerialnumber;

    /**
     * 响应流水号
     */
    private int resSerialnumber = 0;


    private static Constants instance;

    private Constants() {
    }

    public static Constants getInstance(){
        if (instance == null){
            synchronized (Constants.class){
                if (instance == null){
                    instance = new Constants();
                }
            }
        }
        return instance;
    }

    /**
     * 获取请求流水号
     * @return
     */
    public int getReqSerialnumber() {
        return reqSerialnumber ++;
    }

    /**
     * 获取响应流水号
     * @return
     */
    public int getResSerialnumber() {
        return resSerialnumber;
    }

    /**
     * 设置响应流水号
     * @param resSerialnumber
     */
    public void setResSerialnumber(int resSerialnumber) {
        this.resSerialnumber = resSerialnumber;
    }


}
