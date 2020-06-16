package com.zkkj.gps.gateway.ccs;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.utils.CollectionsSortUtil;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        List<GPSPositionDto> list= new ArrayList<>();
        GPSPositionDto g1=new GPSPositionDto();
        g1.setGpsTime("2020-06-04 17:00:00");
        GPSPositionDto g2=new GPSPositionDto();
        g2.setGpsTime("2020-06-04 12:00:00");
        GPSPositionDto g3=new GPSPositionDto();
        g3.setGpsTime("2020-06-04 18:00:00");
        GPSPositionDto g4=new GPSPositionDto();
        g4.setGpsTime("2020-06-04 14:00:00");
        GPSPositionDto g5=new GPSPositionDto();
        g5.setGpsTime("2020-06-04 11:00:00");

        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4);
        list.add(g5);
//        for (int x=0; x<10000;x++){
//            gpsPositionDto = new GPSPositionDto();
//
//            gpsPositionDto.setGpsTime(DateTimeUtils.getCurrentDateTimeStr());
//            list.add(gpsPositionDto);
//
//        }

        for (GPSPositionDto gpsPositionDto1:list){
            System.out.println(gpsPositionDto1.toString());
        }
        System.out.println("_sssssssssssssss________________________________________________________________");
        List<GPSPositionDto> gpsPositionDtos = CollectionsSortUtil.timeSort(list);
        for (GPSPositionDto gpsPositionDto1:gpsPositionDtos){
            System.out.println(gpsPositionDto1.toString());
        }
    }
}
