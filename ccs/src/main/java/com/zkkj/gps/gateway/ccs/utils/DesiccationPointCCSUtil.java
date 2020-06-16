package com.zkkj.gps.gateway.ccs.utils;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.entity.denoising.DenoisingBean;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.utils.CoordinateUtil;
import com.zkkj.gps.gateway.terminal.monitor.utils.DesiccationPointUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
public class DesiccationPointCCSUtil {

    public static Boolean validatePosition(GPSPositionDto oldGPSPosition, GPSPositionDto newGPSPosition) {
        //无历史点，则任务是新增第一个默认有效
        if (oldGPSPosition == null) {
            return true;
        }
        if (oldGPSPosition != null) {
            if (DesiccationPointUtil.isInChina(newGPSPosition.getLongitude(), newGPSPosition.getLatitude())) {
                //获取两个点之间的距离(米)
                double dist = CoordinateUtil.getDistance(oldGPSPosition.getLatitude(), oldGPSPosition.getLongitude(),
                        newGPSPosition.getLatitude(), newGPSPosition.getLongitude());
                double hourNumber = DateTimeUtils.durationMinutes(DateTimeUtils.getDateByString(newGPSPosition.getGpsTime()),
                        DateTimeUtils.getDateByString(oldGPSPosition.getGpsTime())) / 60.000;
                if (hourNumber > 0.03) {
                    return true;
                }
                if (oldGPSPosition.getSpeedKm() < 3 && newGPSPosition.getSpeedKm() < 3 && hourNumber != 0) {
                    double speed = (dist / 1000) / hourNumber;
                    if ((speed < newGPSPosition.getSpeedKm() + 10 && speed > newGPSPosition.getSpeedKm() - 10) ||
                            (speed < oldGPSPosition.getSpeedKm() + 10 && speed > oldGPSPosition.getSpeedKm() - 10)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean validatePositionNew(DenoisingBean oldGPSPosition, DenoisingBean newGPSPosition) {
        //无历史点，则任务是新增第一个默认有效
        if (oldGPSPosition == null) {
            return true;
        }
        if (oldGPSPosition != null) {
            if (DesiccationPointUtil.isInChina(newGPSPosition.getLongitude(), newGPSPosition.getLatitude())) {
                //获取两个点之间的距离(米)
                double dist = CoordinateUtil.getDistance(oldGPSPosition.getLatitude(), oldGPSPosition.getLongitude(),
                        newGPSPosition.getLatitude(), newGPSPosition.getLongitude());
                double hourNumber = DateTimeUtils.durationMinutes(DateTimeUtils.getDateByString(newGPSPosition.getGpsTimeStr()),
                        DateTimeUtils.getDateByString(oldGPSPosition.getGpsTimeStr())) / 60.000;
                if (hourNumber > 0.03) {
                    return true;
                }
                if (oldGPSPosition.getSpeedKm() < 3 && newGPSPosition.getSpeedKm() < 3 && hourNumber != 0) {
                    double speed = (dist / 1000) / hourNumber;
                    if ((speed < newGPSPosition.getSpeedKm() + 10 && speed > newGPSPosition.getSpeedKm() - 10) ||
                            (speed < oldGPSPosition.getSpeedKm() + 10 && speed > oldGPSPosition.getSpeedKm() - 10)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

//    public static Boolean validatePositionNew(GPSPositionDto oldGPSPosition, GPSPositionDto newGPSPosition) {
//        //无历史点，则任务是新增第一个默认有效
//        if (oldGPSPosition == null) {
//            return true;
//        }
//        if (oldGPSPosition != null) {
//            if (DesiccationPointUtil.isInChina(newGPSPosition.getLongitude(), newGPSPosition.getLatitude())) {
//                //获取两个点之间的距离(米)
//                double dist = CoordinateUtil.getDistance(oldGPSPosition.getLatitude(), oldGPSPosition.getLongitude(),
//                        newGPSPosition.getLatitude(), newGPSPosition.getLongitude());
//                double hourNumber = DateTimeUtils.durationMinutes(DateTimeUtils.getDateByString(newGPSPosition.getGpsTime()),
//                        DateTimeUtils.getDateByString(oldGPSPosition.getGpsTime())) / 60.000;
//                double distKm = dist / 1000.0;
//                double speedKm = distKm / hourNumber;
//                if (speedKm <= 120 && hourNumber > 0.03){
//                    return true;
//                }
//                if (oldGPSPosition.getSpeedKm() < 3 && newGPSPosition.getSpeedKm() < 3 && hourNumber != 0) {
//                    double speed = (dist / 1000) / hourNumber;
//                    if ((speed < newGPSPosition.getSpeedKm() + 10 && speed > newGPSPosition.getSpeedKm() - 10) ||
//                            (speed < oldGPSPosition.getSpeedKm() + 10 && speed > oldGPSPosition.getSpeedKm() - 10)) {
//                        return true;
//                    }
//                } else {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public static void main(String[] args) {
//        List<GPSPositionDto> list = savaPointData();
//        System.out.println(list.size());
//        List<GPSPositionDto> listSuc = Lists.newArrayList();
//        if (!CollectionUtils.isEmpty(list)){
//            if (list.size() >=6){
//                for (int i = 0; i < list.size()-1; i++) {
//                    boolean isNormal = false;
//                    int sucCount = 0;
//                    if (i >= list.size() - 5){
//                        int index1 = 1;
//                        while (index1 <= 5){
//                            int temp1 = i - index1;
//                            isNormal = validatePositionNew(list.get(i), list.get(temp1));
//                            if (isNormal) {
//                                ++sucCount;
//                                if (sucCount>=3){
//                                    break;
//                                }
//                            }
//                            index1++;
//                        }
//                    } else {
//                        int index = 1;
//                        while (index <= 5) {
//                            if (i < list.size() - 5) {
//                                int temp = i + index;
//                                isNormal = validatePositionNew(list.get(i), list.get(temp));
//                            }
//                            if (isNormal) {
//                                ++sucCount;
//                                if (sucCount>=3){
//                                    break;
//                                }
//                            }
//                            index++;
//                        }
//                    }
//                    if (sucCount >= 3) {
//                        System.out.println("这个点在范围内：【" + list.get(i).toString() + "】");
//                        listSuc.add(list.get(i));
//                    }
//                }
//            }
//        }
//    }


    private static List<GPSPositionDto> savaPointData(){
        List<GPSPositionDto> list = new ArrayList<>();
        GPSPositionDto gpsNew0 = new GPSPositionDto();
        gpsNew0.setLongitude(110.1336517);
        gpsNew0.setLatitude(37.0102768);
        gpsNew0.setSpeed(546);
        gpsNew0.setGpsTime("2019-11-28 00:00:00");
        list.add(gpsNew0);

        GPSPositionDto gpsNew = new GPSPositionDto();
        gpsNew.setLongitude(108.9435806);
        gpsNew.setLatitude(34.9172401);
        gpsNew.setSpeed(0);
        gpsNew.setGpsTime("2019-11-28 15:33:58");
        list.add(gpsNew);

        GPSPositionDto gpsNew1 = new GPSPositionDto();
        gpsNew1.setLongitude(108.9435806);
        gpsNew1.setLatitude(34.9172401);
        gpsNew1.setSpeed(0);
        gpsNew1.setGpsTime("2019-11-28 15:34:00");
        list.add(gpsNew1);

        GPSPositionDto gpsNew2 = new GPSPositionDto();
        gpsNew2.setLongitude(109.5127487);
        gpsNew2.setLatitude(38.0255318);
        gpsNew2.setSpeed(14);
        gpsNew2.setGpsTime("2019-11-28 15:35:31");
        list.add(gpsNew2);

        GPSPositionDto gpsNew3 = new GPSPositionDto();
        gpsNew3.setLongitude(109.5127716);
        gpsNew3.setLatitude(38.0255318);
        gpsNew3.setSpeed(2);
        gpsNew3.setGpsTime("2019-11-28 15:35:36");
        list.add(gpsNew3);

        GPSPositionDto gpsNew4 = new GPSPositionDto();
        gpsNew4.setLongitude(109.5127716);
        gpsNew4.setLatitude(38.0255623);
        gpsNew4.setSpeed(27);
        gpsNew4.setGpsTime("2019-11-28 15:35:40");
        list.add(gpsNew4);

        GPSPositionDto gpsNew5 = new GPSPositionDto();
        gpsNew5.setLongitude(109.5127563);
        gpsNew5.setLatitude(38.0256195);
        gpsNew5.setSpeed(54);
        gpsNew5.setGpsTime("2019-11-28 15:35:43");
        list.add(gpsNew5);

        GPSPositionDto gpsNew6 = new GPSPositionDto();
        gpsNew6.setLongitude(109.5128098);
        gpsNew6.setLatitude(38.0255661);
        gpsNew6.setSpeed(47);
        gpsNew6.setGpsTime("2019-11-28 15:35:45");
        list.add(gpsNew6);

        GPSPositionDto gpsNew7 = new GPSPositionDto();
        gpsNew7.setLongitude(109.5128098);
        gpsNew7.setLatitude(38.0255775);
        gpsNew7.setSpeed(5);
        gpsNew7.setGpsTime("2019-11-28 15:35:56");
        list.add(gpsNew7);

        GPSPositionDto gpsNew8 = new GPSPositionDto();
        gpsNew8.setLongitude(109.5128326);
        gpsNew8.setLatitude(38.0255432);
        gpsNew8.setSpeed(0);
        gpsNew8.setGpsTime("2019-11-28 15:36:15");
        list.add(gpsNew8);

        GPSPositionDto gpsNew9 = new GPSPositionDto();
        gpsNew9.setLongitude(108.9435806);
        gpsNew9.setLatitude(34.9172401);
        gpsNew9.setSpeed(0);
        gpsNew9.setGpsTime("2019-11-28 15:36:27");
        list.add(gpsNew9);

        GPSPositionDto gpsNew10 = new GPSPositionDto();
        gpsNew10.setLongitude(109.5128479);
        gpsNew10.setLatitude(38.0255928);
        gpsNew10.setSpeed(1);
        gpsNew10.setGpsTime("2019-11-28 15:36:44");
        list.add(gpsNew10);
        return list;
    }


}
