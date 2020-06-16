package com.zkkj.gps.gateway.celltrack.celltrackmonitor.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 * @author suibozhuliu
 */
public class DateTimeUtils {

    /**
     * 日期字符串格式化为LocalDateTime
     */
    public static LocalDateTime strToLocalDateTime(String times) throws Exception{
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(times)){
            if (times.contains("T")){
                times = times.replaceAll("T"," ");
            }
            if (times.contains(".")){
                times = times.substring(0,times.indexOf("."));
            }
            LocalDateTime ldt = LocalDateTime.parse(times,df);
            return ldt;
        }
        return null;
    }

}
