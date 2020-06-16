package com.zkkj.gps.gateway.celltrack.celltrackmonitor.test;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestMain01 {
    public static void main(String[] args) {
        /*try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.parse("2019-11-11 11:21:54",dtf);
            System.out.println(ldt.toString());
        } catch (Exception e){
            e.printStackTrace();
        }*/

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String times = "2019-11-11T11:21:54.969433";
        if (!StringUtils.isEmpty(times)){
            if (times.contains("T")){
                times = times.replaceAll("T"," ");
            }
            if (times.contains(".")){
                int index = times.indexOf(".");
                times = times.substring(0,times.indexOf("."));
            }
        }
        LocalDateTime ldt = LocalDateTime.parse(times,df);
        System.out.println("String类型的时间转成LocalDateTime："+ldt);

    }
}
