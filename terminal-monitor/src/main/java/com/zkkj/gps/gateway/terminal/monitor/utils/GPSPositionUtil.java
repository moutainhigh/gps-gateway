package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaPointsDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.LineStringDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import org.apache.commons.lang3.StringUtils;

import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-05-09
 * 关于点位的工具类
 */
public class GPSPositionUtil {


    /**
     * 校验历史点位是否在报警时间段内
     * 如果连续的点都在时间范围内则返回true，
     *
     * @param hisListPosition
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean validateHisListPositionTime(QueueList<BasicPositionDto> hisListPosition, String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            LocalDateTime startDateTime = DateTimeUtils.parseLocalDateTime(startTime);
            return hisListPosition.stream().allMatch(s -> (DateTimeUtils.durationMillis(startDateTime, s.getDate()) >= 0)) ? true : false;
        }
        //判断所有的点的接受时间都大于报警配置结束时间则返回true
        if (StringUtils.isNotBlank(endTime)) {
            LocalDateTime endDateTime = DateTimeUtils.parseLocalDateTime(endTime);
            return hisListPosition.stream().allMatch(s -> (DateTimeUtils.durationMillis(endDateTime, s.getDate()) > 0)) ? true : false;
        }
        return false;
    }


    /**
     * 校验最新点位是否在报警时间段内
     *
     * @param basicPositionDto
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean validateLastedPositionTime(BasicPositionDto basicPositionDto, String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            LocalDateTime startDateTime = DateTimeUtils.parseLocalDateTime(startTime);
            return DateTimeUtils.durationMillis(startDateTime, basicPositionDto.getDate()) >= 0;
        }
        //判断所有的点的接受时间都大于报警配置结束时间则返回true
        if (StringUtils.isNotBlank(endTime)) {
            LocalDateTime endDateTime = DateTimeUtils.parseLocalDateTime(endTime);
            return DateTimeUtils.durationMillis(endDateTime, basicPositionDto.getDate()) > 0;
        }
        return false;
    }


    /**
     * 判断点全在区域外，则说明已经离开区域
     *
     * @param areaDTO
     * @param hisListPosition
     * @return
     */
    public static boolean isOutArea(AreaDto areaDTO, QueueList<BasicPositionDto> hisListPosition) {
        for (int i = 0; i < hisListPosition.size(); i++) {
            if (checkSingleInArea(hisListPosition.get(i), areaDTO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否进入该区域
     * 即判断连续多个点在区域内就说明进入该区域了
     *
     * @param areaDTO
     * @param hisListPosition
     * @return
     */
    public static boolean isInArea(AreaDto areaDTO, QueueList<BasicPositionDto> hisListPosition) {
        boolean isInArea = true;
        for (BasicPositionDto basicPositionDto : hisListPosition) {
            //判断单个点是否在区域中
            if (!checkSingleInArea(basicPositionDto, areaDTO)) {
                isInArea = false;
                break;
            }
        }
        return isInArea;
    }

    /**
     * 判断单个点是否在区域中
     *
     * @param basicPositionDto
     * @param areaDTO
     * @return
     */
    public static boolean checkSingleInArea(BasicPositionDto basicPositionDto, AreaDto areaDTO) {
        //获取坐标中的经纬度信息
        double longitude = basicPositionDto.getLongitude();//经度
        double latitude = basicPositionDto.getLatitude();//纬度
        //获取区域类型，CIRCLE(1, "圆"), POLYGON(2, "多边形"), LINESTRING(3, "线");
        if (areaDTO != null && areaDTO.getGraphTypeEnum() != null) {
            int seq = areaDTO.getGraphTypeEnum().getSeq();
            //如果是圆
            if (seq == GraphTypeEnum.CIRCLE.getSeq()) {
                return CoordinateUtil.isInCircle(areaDTO.getRadius(), areaDTO.getCenterLat(), areaDTO.getCenterLng(), latitude, longitude);
            }
            //如果是多边形
            if (seq == GraphTypeEnum.POLYGON.getSeq()) {
                AreaPointsDto[] areaPoints = areaDTO.getAreaPoints();
                if (areaPoints != null && areaPoints.length > 0) {
                    //将点信息转成point.double集合并且只需要经纬度两列
                    List<Point2D.Double> polygon = Arrays.asList(areaPoints).stream().map(s -> new Point2D.Double(s.getLng(), s.getLat())).collect(Collectors.toList());
                    return CoordinateUtil.isPtInPoly(polygon, new Point2D.Double(longitude, latitude));
                }
                return false;
            }
        }
        return false;
    }


    /**
     * 判断给定的坐标点是否在区域内
     *
     * @param point2D
     * @param areaDTO
     * @return
     */
    public static boolean checkSingleInArea(Point2D point2D, AreaDto areaDTO) {
        //获取区域类型，CIRCLE(1, "圆"), POLYGON(2, "多边形"), LINESTRING(3, "线");
        if (areaDTO != null && areaDTO.getGraphTypeEnum() != null) {
            int seq = areaDTO.getGraphTypeEnum().getSeq();
            //如果是圆
            if (seq == GraphTypeEnum.CIRCLE.getSeq()) {
                return CoordinateUtil.isInCircle(areaDTO.getRadius(), areaDTO.getCenterLat(), areaDTO.getCenterLng(), point2D.getY(), point2D.getX());
            }
            //如果是多边形
            if (seq == GraphTypeEnum.POLYGON.getSeq()) {
                AreaPointsDto[] areaPoints = areaDTO.getAreaPoints();
                if (areaPoints != null && areaPoints.length > 0) {
                    //将点信息转成point.double集合并且只需要经纬度两列
                    List<Point2D.Double> polygon = Arrays.asList(areaPoints).stream().map(s -> new Point2D.Double(s.getLng(), s.getLat())).collect(Collectors.toList());
                    return CoordinateUtil.isPtInPoly(polygon, new Point2D.Double(point2D.getX(), point2D.getY()));
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 没有超速
     * 即连续的点都小于临界速度
     *
     * @param hisListPosition
     * @param limitSpeed
     * @return
     */
    public static boolean unOverSpeed(QueueList<BasicPositionDto> hisListPosition, double limitSpeed) {
        return hisListPosition.stream().allMatch(s -> s.getSpeedKmH() < limitSpeed) ? true : false;
    }

    /**
     * 超速
     * 即连续的点都大于等于临界速度
     *
     * @param hisListPosition
     * @param limitSpeed
     * @return
     */
    public static boolean overSpeed(QueueList<BasicPositionDto> hisListPosition, double limitSpeed) {
        return hisListPosition.stream().allMatch(s -> s.getSpeedKmH() >= limitSpeed) ? true : false;
    }

    /**
     * 判断区域内停车，即连续点速度为0
     *
     * @param hisListPosition
     * @return
     */
    public static boolean isStop(QueueList<BasicPositionDto> hisListPosition) {
        return hisListPosition.stream().allMatch(s -> s.getSpeed() == 0) ? true : false;
    }

    /**
     * 判断区域内停车结束，即连续的点速度都大于0
     *
     * @param hisListPosition
     * @return
     */
    public static boolean iSNotStop(QueueList<BasicPositionDto> hisListPosition) {
        return hisListPosition.stream().allMatch(s -> s.getSpeed() > 0) ? true : false;
    }


    /**
     * 判断只要有一条线路上的标识为true，则返回true，如果全为false，则返回false
     *
     * @param lineStrings
     * @return
     */
    public static boolean checkAnyOnRoute(LineStringDto[] lineStrings) {
        return Arrays.asList(lineStrings).stream().anyMatch(s -> s.isOnRouteLine() == true) ? true : false;
    }

    /**
     * 获取连续的点到线路上的最短距离
     * 检查连续的点是否在当前线路上
     * 如果都没有在该路线上，则返回false，说明已偏移路线
     *
     * @param hisListPosition
     * @param lineString
     * @return
     */
    public static double checkOnRouteLine(QueueList<BasicPositionDto> hisListPosition, LineStringDto lineString, double width) {
        //获取线路点
        String pointSequence = lineString.getPointSequence();
        List<Point2D> pointList = getRoutLinePointList(pointSequence);
        //判断连续的点在路线上
        //boolean onRoute = checkContinueOnRoute(hisListPosition, pointList, width);
        int count = checkContinueOnRoute(hisListPosition, pointList, width);
        if (count == 0) {
            lineString.setOnRouteLine(false);
        }
        if (count == 1) {
            lineString.setOnRouteLine(true);
        }
        List<Double> list = Lists.newArrayList();
        for (int i = 0; i < hisListPosition.size(); i++) {
            double dist = CoordinateUtil.pointToRouteDist(pointList, new Point2D.Double(hisListPosition.get(i).getLongitude(), hisListPosition.get(i).getLatitude()));
            list.add(dist);
        }
        //获取每条线路上连续的点到该线路最短的距离
        return list.stream().mapToDouble(s -> s.doubleValue()).summaryStatistics().getMin();
    }

    /**
     * 判断连续的点是否在线路上
     * 如果点都没在线路上则返回false，否则返回true
     *
     * @param hisListPosition
     * @param routeLinePointList
     * @param width
     * @return
     */
    public static int checkContinueOnRoute(QueueList<BasicPositionDto> hisListPosition, List<Point2D> routeLinePointList, double width) {
        List<Boolean> booList = Lists.newArrayList();
        for (BasicPositionDto basicPositionDto : hisListPosition) {
            double latitude = basicPositionDto.getLatitude();
            double longitude = basicPositionDto.getLongitude();
            Point2D.Double point = new Point2D.Double(longitude, latitude);
            //判断点是否在路线上
            boolean isPointOnRoute = CoordinateUtil.pointOnRoute(routeLinePointList, point, width);
            booList.add(isPointOnRoute);
        }
        //只要路线中都为false的话则认为线路偏移 0 为偏移，1为没有偏移
        return booList.stream().allMatch(s -> s.booleanValue() == false) ? 0 : (booList.stream().allMatch(s -> s.booleanValue() == true) ? 1 : 2);
    }

    /**
     * 将点字符串转化成二维坐标点集合
     * 点序列，纬度(lat),经度(lng)eg:34.200753,108.920162;34.201081,108.921837;.......
     *
     * @param pointSequence
     * @return
     */
    public static List<Point2D> getRoutLinePointList(String pointSequence) {
        List<Point2D> pointList = Lists.newArrayList();
        if (pointSequence != null && pointSequence.length() > 0) {
            String[] pointArr = pointSequence.split(";");
            for (String str : pointArr) {
                String[] point = str.split(",");
                if (point != null && point.length >= 2) {
                    pointList.add(new Point2D.Double(Double.valueOf(point[1]), Double.valueOf(point[0])));
                }
            }
        }
        return pointList;
    }

    /**
     * 判断是否停车超时
     *
     * @param terminalAlarmInfoDTO
     * @param hisListPosition
     * @param alarmConfig
     * @return
     */
    public static boolean allStopOverTime(TerminalAlarmInfoDto terminalAlarmInfoDTO, QueueList<BasicPositionDto> hisListPosition, AlarmConfigDto alarmConfig) {
        if (StringUtils.isNotBlank(terminalAlarmInfoDTO.getAlarmTime())) {
            LocalDateTime alarmTime = DateTimeUtils.parseLocalDateTime(terminalAlarmInfoDTO.getAlarmTime());
            return hisListPosition.stream().allMatch(s -> DateTimeUtils.durationMinutes(alarmTime, s.getDate()) >= alarmConfig.getConfigValue());
        }
        return false;
    }

    /**
     * 判断连续的点位是否触发车辆载重异常
     *
     * @param configValue
     * @param hisListPosition
     * @return
     */
    public static boolean unusualLoad(double configValue, QueueList<BasicPositionDto> hisListPosition) {
        return hisListPosition.stream().allMatch(s -> (s.getLoadSensorValue() == null ? 0.0 : s.getLoadSensorValue()) < configValue);
    }
}
