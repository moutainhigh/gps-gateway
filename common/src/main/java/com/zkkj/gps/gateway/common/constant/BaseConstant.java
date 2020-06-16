package com.zkkj.gps.gateway.common.constant;

/**
 * author : cyc
 * Date : 2019-04-24
 * 常量
 */
public final class BaseConstant {

    /**
     * 经度正则表达式
     */
    public static final String LONGITUDE_REGEX = "[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})";

    /**
     * 纬度正则表达式
     */
    public static final String LATITUDE_REGEX = "[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})";

    /**
     * 编码格式
     */
    public static final String CHAR_SET = "GBK";

    /**
     * 车牌号的正则表达式(包含新能源车辆)
     */
    public static final String PLATE_NUMBER = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";

    /**
     * 针对府谷煤管站的运单编号开头处理
     */
    public static final String FGMGZ_STARTWITH = "FGMGZ";
}
