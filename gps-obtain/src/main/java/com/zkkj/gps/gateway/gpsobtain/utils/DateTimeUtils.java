package com.zkkj.gps.gateway.gpsobtain.utils;

import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    /*
     * @Author lx
     * @Description 将时间格式utc1572243041000转为LocalDateTime
     * @Date 15:24 2019/10/29
     * @Param
     * @return
     **/
    public static LocalDateTime timeTrans(String time) {
        Instant instant = Instant.ofEpochMilli(Long.parseLong(time));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 日期时间字符串格式化成LocalDateTime
     * 例子：20191111/225845----->LocalDateTime
     * @param timeStr
     * @return
     */
    public static LocalDateTime timeStrToLocalDateTime(String timeStr){
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            if (!StringUtils.isEmpty(timeStr)){
                if (timeStr.contains("/")){
                    timeStr = timeStr.replaceAll("/","");
                }
                LocalDateTime ldt = LocalDateTime.parse(timeStr,df);
                return ldt;
            }
        } catch (Exception e){}
        return null;
    }

}
