package com.zkkj.gps.gateway.ccs.utils;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import org.apache.commons.collections4.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 对于集合排序
 */
public class CollectionsSortUtil {
    public static List<GPSPositionDto> timeSort(List<GPSPositionDto> gpsPositionDtoList){
        Collections.sort(gpsPositionDtoList, new Comparator<GPSPositionDto>() {
            @Override
            public int compare(GPSPositionDto o1, GPSPositionDto o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd SS:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getGpsTime());
                    Date dt2 = format.parse(o2.getGpsTime());
                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return gpsPositionDtoList;
    }
}
