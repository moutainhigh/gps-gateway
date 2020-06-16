package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/*
 * @Author lx
 * @Description 点位去燥
 * @Date 9:57 2019/9/5
 * @Param
 * @return
 **/
public class DesiccationPointUtil {

    /*
     * @Author lx
     * @Description 判断当前点位是否有效
     * @Date 9:59 2019/9/5
     * @Param
     * @return
     **/
    public static Boolean validatePosition(BasicPositionDto newPointDto, BasicPositionDto oldPointDto) {
        //TODO 结合历史数据，对当前返回定位信息进行验证
        if (newPointDto != null) {
            //无历史点，则任务是新增第一个默认有效
            if (oldPointDto == null) {
                return true;
            }
            if (oldPointDto != null) {
                if (oldPointDto.getDate().equals(newPointDto.getDate()) && oldPointDto.getLongitude().equals(newPointDto.getLongitude()) && oldPointDto.getLatitude().equals(newPointDto.getLatitude())) {
                    return false;
                }
                if (isInChina(newPointDto.getLongitude(), newPointDto.getLatitude())) {
                    //获取两个点之间的距离(米)
                    double dist = CoordinateUtil.getDistance(oldPointDto.getLatitude(), oldPointDto.getLongitude(),
                            newPointDto.getLatitude(), newPointDto.getLongitude());
                    double hourNumber = DateTimeUtils.durationMinutes(DateTimeUtils.getDateByLocalDateTime(newPointDto.getDate()),
                            DateTimeUtils.getDateByLocalDateTime(oldPointDto.getDate())) / 60.000;
                    if (hourNumber > 0.03) {
                        return true;
                    }
                    if (oldPointDto.getSpeedKmH() < 3 && newPointDto.getSpeedKmH() < 3 && hourNumber != 0) {
                        double speed = (dist / 1000) / hourNumber;
                        if ((speed < newPointDto.getSpeedKmH() + 10 && speed > newPointDto.getSpeedKmH() - 10) ||
                                (speed < oldPointDto.getSpeedKmH() + 10 && speed > oldPointDto.getSpeedKmH() - 10)) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断当前点位是否在中国
    public static Boolean isInChina(double latitude, double longitude) {
        List<Point2D.Double> chinaPoint = new ArrayList<>();
        chinaPoint.add(new Point2D.Double(39.2152313091, 73.0371093750));
        chinaPoint.add(new Point2D.Double(53.6836953450, 123.3325195313));
        chinaPoint.add(new Point2D.Double(48.4000324961, 135.5603027344));
        chinaPoint.add(new Point2D.Double(17.7277586099, 112.3681640625));
        return CoordinateUtil.isPtInPoly(chinaPoint, new Point2D.Double(longitude, latitude));
    }
}
