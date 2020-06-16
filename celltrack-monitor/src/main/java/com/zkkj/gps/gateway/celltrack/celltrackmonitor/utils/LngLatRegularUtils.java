package com.zkkj.gps.gateway.celltrack.celltrackmonitor.utils;

import java.util.regex.Pattern;

/**
 * 经纬度工具类
 * @author suibozhuliu
 */
public class LngLatRegularUtils {

    /**
     * 经度正则式
     */
    private static String lonMatch = "[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})";
    /**
     * 纬度正则式
     */
    private static String latMatch = "[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})";

    /**
     * 验证经纬度是否符合法
     * @param lng
     * @param lat
     * @return
     */
    public static boolean getLngLatVerify(double lng,double lat){
        if (Pattern.matches(lonMatch,lng + "") && Pattern.matches(latMatch,lat + "")){
            return true;
        }
        return false;
    }

}
