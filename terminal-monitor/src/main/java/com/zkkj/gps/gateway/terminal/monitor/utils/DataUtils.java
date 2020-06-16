package com.zkkj.gps.gateway.terminal.monitor.utils;

import java.text.DecimalFormat;

/**
 * 数据转换处理类
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-12 下午 4:28
 */
public class DataUtils {

    /**
     * 保留两位有效数字
     * @param point
     * @return
     */
    public static String getDoublePoint(Double point){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(point);
    }

    /**
     * 判断输入的字符串参数是否为空
     * @return boolean 空则返回true,非空则flase
     */
    public static boolean isEmptyString(String input) {
        return null==input || 0==input.length() || 0==input.replaceAll("\\s", "").length();
    }

    /**
     * 判断数组是否为空
     * @param arr
     * @return
     */
    public static boolean isEmptyArr(byte[] arr){
        return arr == null || arr.length == 0;
    }

}
