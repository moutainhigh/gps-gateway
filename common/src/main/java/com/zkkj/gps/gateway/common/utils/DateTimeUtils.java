package com.zkkj.gps.gateway.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * author : cyc
 * Date : 2019-04-18
 * 日期工具类
 */
public class DateTimeUtils {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 返回当前的日期
     *
     * @return
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 返回当前时间
     *
     * @return
     */
    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }

    /**
     * 返回当前日期时间
     *
     * @return
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * yyyyMMdd
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * yyMMdd
     *
     * @return
     */
    public static String getCurrentShortDateStr() {
        return LocalDate.now().format(SHORT_DATE_FORMATTER);
    }

    public static String getCurrentMonthStr() {
        return LocalDate.now().format(MONTH_FORMATTER);
    }

    /**
     * yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * yyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentShortDateTimeStr() {
        return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER);
    }

    /**
     * HHmmss
     *
     * @return
     */
    public static String getCurrentTimeStr() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    public static String getCurrentDateStr(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDateTimeStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentTimeStr(String pattern) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    public static LocalTime parseLocalTime(String timeStr) {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public static String formatLocalDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime datetime) {
        return datetime.format(DATETIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    /**
     * 日期相隔天数
     *
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return Period.between(startDateInclusive, endDateExclusive).getDays();
    }

    /**
     * 日期相隔小时
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 日期相隔分钟
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    /**
     * 日期相隔毫秒数
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 是否当天
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return getCurrentLocalDate().equals(date);
    }

    public static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyyMMddHHmmss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /*
     * @Author lx
     * @Description 计算两个时间之间相隔多少分钟
     * @Date 8:24 2019/5/28
     * @Param
     * @return
     **/
    public static double durationMinutes(Date date1, Date date2) {
        double minutes = Math.abs(((date2.getTime() - date1.getTime()) / (60.000 * 1000)));
        return minutes;
    }

    /*
     * @Author lx
     * @Description 根据字符串获取时间
     * @Date 8:27 2019/5/28
     * @Param
     * @return
     **/
    public static Date getDateByString(String dateStr) {
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(dateStr);
        } catch (Exception ex) {
            ex.toString();
            ex.printStackTrace();
        }
        return date;
    }

    /*
        将LocalDateTime为Date
     */
    public static Date getDateByLocalDateTime(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /*
     * @Author lx
     * @Description 获取当前时间多少天后的时间字符串
     * @Date 17:27 2019/5/28
     * @Param
     * @return
     **/
    public static String getAfterDayToString(int days) {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DATE, curr.get(Calendar.DATE) + days);
        Date date = curr.getTime();
        return dateToStrLong(date);
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
     * 计算两个日期时间的小时差
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static double calculateTimeHour(String startTime,String endTime) throws ParseException {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*小时差*/
        Date fromDate2 = simpleFormat.parse(startTime);
        Date toDate2 = simpleFormat.parse(endTime);
        long from2 = fromDate2.getTime();
        long to2 = toDate2.getTime();
        double hours = (to2 - from2) / (1000.0 * 60.0 * 60.0);
        return hours;
    }

    /**
     * 计算两个日期时间的分钟差
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static double calculateTimeMinutes(String startTime,String endTime) throws ParseException {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*小时差*/
        Date fromDate2 = simpleFormat.parse(startTime);
        Date toDate2 = simpleFormat.parse(endTime);
        long from2 = fromDate2.getTime();
        long to2 = toDate2.getTime();
        double minutes = (to2 - from2) / (1000.0 * 60.0);
        return minutes;
    }

    /**
     * 获取某个日期时间的后一天
     * @param time  格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getLastDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //此处修改为-1则是获取前一天
        calendar.set(Calendar.DATE, day + 1);
        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * 获取某个日期时间的前一天
     * @param time  格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getAfterDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //此处修改为-1则是获取前一天
        calendar.set(Calendar.DATE, day - 1);
        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * 验证某个日期时间是否在某个时间范围内
     * @param startRange：时间范围的开始时间
     * @param endRange：时间范围的结束时间
     * @param verifyTime：需要验证的日期时间
     * @return
     */
    public static boolean isEffectiveDate(String startRange,String endRange,String verifyTime){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTimeDate = sdf.parse(startRange);
            Date endTimeDate = sdf.parse(endRange);
            Date verifyTimeDate = sdf.parse(verifyTime);
            if (verifyTimeDate.getTime() == startTimeDate.getTime()
                    || verifyTimeDate.getTime() == endTimeDate.getTime()) {
                return true;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(verifyTimeDate);

            Calendar begin = Calendar.getInstance();
            begin.setTime(startTimeDate);

            Calendar end = Calendar.getInstance();
            end.setTime(endTimeDate);
            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e){}
        return false;
    }

    /**
     * 日期时间排序
     * @param dateList
     * @return
     */
    public static List<String> dateSort(List<String> dateList){
        dateList.sort(Comparator.naturalOrder());
        return dateList;
    }

    /**
     * 日期时间向后推移N秒后的日期时间
     * @param dateTime
     * @param seconds
     * @return
     * @throws ParseException
     */
    public static String datePostpone(String dateTime,int seconds) throws ParseException {
        Date date = sdf.parse(dateTime);
        date.setTime(date.getTime() + seconds * 1000);
        String formatDate = sdf.format(date);
        return formatDate;
    }

    /**
     * 日期时间向前推移N秒后的日期时间
     * @param dateTime
     * @param seconds
     * @return
     * @throws ParseException
     */
    public static String dateForward(String dateTime,int seconds) throws ParseException {
        Date date = sdf.parse(dateTime);
        date.setTime(date.getTime() - seconds * 1000);
        String formatDate = sdf.format(date);
        return formatDate;
    }

    /*public static void main(String[] args) {
        String s = null;
        try {
            s = datePostpone("2019-12-12 12:02:03", 30);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(s);

        String ss = null;
        try {
            ss = dateForward("2019-12-12 12:02:03", 30);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(ss);
    }*/


}
