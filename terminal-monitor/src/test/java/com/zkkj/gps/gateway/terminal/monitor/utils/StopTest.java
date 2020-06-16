package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.zkkj.gps.gateway.terminal.monitor.dto.AreaDTO;
import com.zkkj.gps.gateway.terminal.monitor.dto.AreaPointDTO;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * author : cyc
 * Date : 2019-04-16
 * 停车超时, 超速测试类
 */
public class StopTest {

	//获取配置中设置的该区域速度
	private int speed = 9;

	//队列数据
	private QueueList<BasicPositionDto> list;

	@Test
	public void test() throws Exception {
		AreaDTO area = new AreaDTO();
		area.setCenterLng(35.167957178239800);
		area.setCenterLat(108.650734125773000);
		area.setGraphType(1);
		area.setRadius(266d);
		area.setAreaName("新茂煤场");
		Set<AreaPointDTO> areaPointSet = new LinkedHashSet<>();
		AreaPointDTO areaPoint = new AreaPointDTO();
		areaPoint.setLng(35.167957178239800);
		areaPoint.setLat(108.650734125773000);
		areaPointSet.add(areaPoint);
		area.setAreaPoint(areaPointSet);

		//获取队列信息
		list = DataSource.initData();
		/**
		 * 判断停车超时或者超速遵循原则
		 * 1.判断是否进入区域
		 * 2.如果进入该区域，速度位置没有发生变化则没有停车超时，如果速速大于该区域的速度则为超速提示
		 * 3.如果没有进入该区域，速度位置没有发生变化则停车超时
		 */

		//判断是否进入某个区域
//		boolean isInOutArea = checkInOutArea(list, area);
//		//连续的经纬度点没有发生变化并且速度为0
//		boolean isNoChange = checkIsStop(list);
//		if (isInOutArea) {
//			if (isNoChange) {
//				System.out.println("进入了当前区域位置和速度没有发生变化");
//			}
//			//速度大于区域行驶速度为超速
//			boolean isOverSpeed = isOverSpeed(list);
//			if (isOverSpeed) {
//				System.out.println("在该区域内超速了");
//			}
//		} else {
//			if (isNoChange) {
//				System.out.println("停车超时");
//			}
//		}
	}

	/**
	 * 是否超速
	 *
	 * @param list
	 * @return
	 */
//	private boolean isOverSpeed(QueueList<P_0200> list) {
//		return list.stream().allMatch(p -> p.getSiteBasicMessage().getSpeed() > speed);
//	}

	/**
	 * 判断是否停车，true为停车，false没有停车
	 *
	 * @param list
	 * @return
	 */
//	private boolean checkIsStop(QueueList<P_0200> list) {
//		boolean flag = true;
//		for (int i = 0; i < list.size() - 1; i++) {
//			for (int j = i + 1; j < list.size(); j++) {
//				if (!(list.get(i).getSiteBasicMessage().getLongitude() == list.get(j).getSiteBasicMessage().getLongitude()
//						&& list.get(i).getSiteBasicMessage().getLatitude() == list.get(j).getSiteBasicMessage().getLatitude()
//						&& list.get(i).getSiteBasicMessage().getSpeed() == 0 && list.get(j).getSiteBasicMessage().getSpeed() == 0)) {
//					flag = false;
//					break;
//				}
//			}
//		}
//		return flag;
//	}

	/**
	 * 判断点集合是否进入当前区域
	 *
	 * @param list
	 * @param area
	 * @return
	 */

	/**
	 * 判断点是否在区域内
	 *
	 * @param point
	 * @param area
	 * @return
	 */
	private boolean isInArea(Point2D.Double point, AreaDTO area) {
		boolean flag = true;
		switch (area.getGraphType()) {
			case 1:
				flag = CoordinateUtil.isInCircle(area.getRadius(), area.getCenterLng(), area.getCenterLat(), point.getY(), point.getX());
				break;
			case 2:
				flag = true;
				break;
			case 3:
				flag = true;
				break;
		}
		return flag;
	}

}
