package com.zkkj.gps.gateway.common.utils;

import java.text.DecimalFormat;

/**
 * 公共数据处理工具类
 * @author suibozhuliu
 */
public class DataCommonUtils {

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static Double getTwoDecimals(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(d));
    }

}
