package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.zkkj.gps.gateway.terminal.monitor.dto.AreaDTO;
import com.zkkj.gps.gateway.terminal.monitor.dto.AreaPointDTO;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaPointsDto;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum.POLYGON;

/**
 * author : cyc
 * Date : 2019-04-10
 * 进出区域测试
 */
public class TestDemo {


    //缓存区域内订单信息，现在假定是车的信息
    private Map<String, String> cacheList = new HashMap<>();


    @Test
    public void testii() {
        QueueList<Point2D.Double> queueList = new QueueList<>(4);
        Point2D.Double p1 = new Point2D.Double(108.879253229628000, 34.194699941634600);
        Point2D.Double p2 = new Point2D.Double(108.879253229628000, 34.194699941634600);
        Point2D.Double p3 = new Point2D.Double(108.879253229628000, 34.194699941634600);
        Point2D.Double p4 = new Point2D.Double(108.879253229628000, 34.194699941634600);
        queueList.add(p1);
        queueList.add(p2);
        queueList.add(p3);
        queueList.add(p4);
        AreaDto area = new AreaDto();

        area.setCenterLat(108.879253229628000);
        area.setCenterLng(34.194699941634600);
        area.setRadius(500.0);
        area.setGraphTypeEnum(POLYGON);
        AreaPointsDto[] areaPointsDtos = new AreaPointsDto[4];
        AreaPointsDto areaPointsDto1 = new AreaPointsDto();
        areaPointsDto1.setLat(34.1952867748609);
        areaPointsDto1.setLng(108.880268952052);
        areaPointsDto1.setSequence(1);

        AreaPointsDto areaPointsDto2 = new AreaPointsDto();
        areaPointsDto2.setLat(34.1952896405259);
        areaPointsDto2.setLng(108.888098400016);
        areaPointsDto2.setSequence(2);

        AreaPointsDto areaPointsDto3 = new AreaPointsDto();
        areaPointsDto3.setLat(34.1908396088344);
        areaPointsDto3.setLng(108.885700191999);
        areaPointsDto3.setSequence(3);

        AreaPointsDto areaPointsDto4 = new AreaPointsDto();
        areaPointsDto4.setLat(108.879575239768);
        areaPointsDto4.setLng(34.1914280454693);
        areaPointsDto4.setSequence(4);
        areaPointsDtos[0] = areaPointsDto1;
        areaPointsDtos[1] = areaPointsDto2;
        areaPointsDtos[2] = areaPointsDto3;
        areaPointsDtos[3] = areaPointsDto4;

        area.setAreaPoints(areaPointsDtos);



        boolean b = checkAllInArea1(queueList, area);
        System.out.println(b);
    }


    private boolean checkAllInArea1(QueueList<Point2D.Double> queueList, AreaDto area) {
        boolean flag = true;
        for (Point2D.Double p : queueList) {
            if (!checkSingleInArea(p, area)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断单个点是否在区域内
     *
     * @param p
     * @param area
     * @return
     */
    private boolean checkSingleInArea(Point2D p, AreaDto area) {
        //该区域是圆
        if (area.getGraphTypeEnum().getSeq() == 1) {
            return CoordinateUtil.isInCircle(area.getRadius(), area.getCenterLng(), area.getCenterLat(), p.getY(), p.getX());
        }
        //该区域是矩形
        if (area.getGraphTypeEnum().getSeq() == 2) {
            return true;
        }

        return true;
    }

    /**
     * 获取区域
     *
     * @return
     */
    private AreaDTO getArea() {
        AreaDTO area = new AreaDTO();
        area.setId(1);
        area.setAreaName("中矿科技");
        area.setGraphType(1);
        area.setRadius(5.0);
        area.setCenterLat(108.650734125773000);
        area.setCenterLng(35.157957178239800);
        Set<AreaPointDTO> areaPointSet = new LinkedHashSet<>();
        AreaPointDTO areaPoint = new AreaPointDTO();
        areaPoint.setLat(108.650734125773000);
        areaPoint.setLng(35.157957178239800);
        areaPointSet.add(areaPoint);
        area.setAreaPoint(areaPointSet);
        return area;
    }

}
