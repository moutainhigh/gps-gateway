package com.zkkj.gps.gateway.ccs.utils;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisBaseGpsPositionInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.text.ParseException;
import java.util.List;

import static com.zkkj.gps.gateway.common.utils.DateTimeUtils.*;

/**
 * 点位补缺算法
 * @author suibozhuliu
 */
@Slf4j
public class GpsArithmetic {

    /**
     * 点位遗漏时间区间验证
     * @param lists
     * @param startTime
     * @param endTime
     * @param intervalMinutes
     * @return
     */
    public static List<TimeBean> pointArithmetic(List<HisBaseGpsPositionInfo> lists, String startTime, String endTime, int intervalMinutes){
        /*double minutes = 0;
        try {
            minutes = calculateTimeMinutes(startTime, endTime);
        } catch (ParseException e) {
            log.error("开始时间和结束时间的时间差计算异常：【startTime：" + startTime + "；endTime：" + endTime + "】" + "；异常：【" + e + "】");
        }
        if (minutes < intervalMinutes){
            return null;
        }*/
        List<TimeBean> timeBeans = Lists.newArrayList();
        try {
            if (!CollectionUtils.isEmpty(lists)) {
                double timeMinutesStart = calculateTimeMinutes(startTime, lists.get(0).getGpsTimeStr());
                if (timeMinutesStart >= 2) {
                    // todo 需要从中交兴路查询该时间范围内的点位数据
                    try {
                        TimeBean timeBean = new TimeBean();
                        timeBean.setStartTime(datePostpone(startTime,30));
                        timeBean.setEndTime(dateForward(lists.get(0).getGpsTimeStr(),30));
                        timeBeans.add(timeBean);
                    } catch (Exception e){
                        log.error("日期时间向后或向后推移30s处理异常：【" + e + "】");
                    }
                }
                if (lists.size() >= 2){
                    int index = 0;
                    while (index < lists.size() - 1) {
                        String startTemp = lists.get(index).getGpsTimeStr();
                        String endTemp = lists.get(++index).getGpsTimeStr();
                        double timeMinutes = calculateTimeMinutes(startTemp, endTemp);
                        if (timeMinutes >= intervalMinutes) {
                            //todo 这个时间范围内没有点位信息   点位相差10分钟以上
                            try {
                                TimeBean timeBean = new TimeBean();
                                timeBean.setStartTime(datePostpone(startTemp,30));
                                timeBean.setEndTime(dateForward(endTemp,30));
                                timeBeans.add(timeBean);
                            } catch (Exception e){
                                log.error("日期时间向后或向后推移30s处理异常：【" + e + "】");
                            }
                        }
                    }
                }
                double timeMinutesEnd = calculateTimeMinutes(lists.get(lists.size() - 1).getGpsTimeStr(), endTime);
                if (timeMinutesEnd >= 2) {
                    //数据库中不存在结束时间至时间范围内的最后一个点的时间跨度的点位信息
                    //todo 需要从中交兴路查询该时间范围内的点位信息
                    try {
                        TimeBean timeBean = new TimeBean();
                        timeBean.setStartTime(datePostpone(lists.get(lists.size() - 1).getGpsTimeStr(),30));
                        timeBean.setEndTime(dateForward(endTime,30));
                        timeBeans.add(timeBean);
                    } catch (Exception e){
                        log.error("日期时间向后或向后推移30s处理异常：【" + e + "】");
                    }
                }
            } else {
                //todo 从数据库没查到点位信息，需要从中交兴路查询，开始时间为：startTime，结束时间为：endTime
                TimeBean timeBean = new TimeBean();
                timeBean.setStartTime(startTime);
                timeBean.setEndTime(endTime);
                timeBeans.add(timeBean);
            }
            if (!CollectionUtils.isEmpty(timeBeans)){
                for (TimeBean timeBean : timeBeans) {
                    System.out.println("所找出的时间范围：【" + timeBean.toString() + "】");
                }
            }
        } catch (ParseException e) {
            log.error("计算日期时间差异常：【" + e + "】");
        }
        return timeBeans;
    }

    /**
     * 存储请求中交兴路开始和结束时间内部类
     */
    @Data
    public static class TimeBean{
        private String startTime;
        private String endTime;
    }

}
