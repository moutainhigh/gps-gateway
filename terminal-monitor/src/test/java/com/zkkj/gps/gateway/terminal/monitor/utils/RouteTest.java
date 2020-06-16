package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.zkkj.gps.gateway.terminal.monitor.dto.RoutePointsDTO;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * author : cyc
 * Date : 2019-04-15
 * 判断是否偏移路线
 */
public class RouteTest extends DataSource {

	//获取路线，一个订单可能包含多条路线
	private Map<Integer, List<RoutePointsDTO>> routePointsMap;

	//从配置文件中获取设置的距离界限,单位米
	private double limitDistance = 1;

	@Test
	public void testRoute() {
		//获取同一订单下的不同路线
		routePointsMap = getRoutePointsMap();
		if (null != routePointsMap && routePointsMap.size() > 0) {
			for (Map.Entry<Integer, List<RoutePointsDTO>> entry : routePointsMap.entrySet()) {
				List<RoutePointsDTO> routePointsList = entry.getValue();
				for (RoutePointsDTO points : routePointsList) {
					//轨迹上的点都不在线路上，则已经偏移路线
					List<Point2D> pointList = points.getPointList();
					//判断队列上的点是否在线路上
//					boolean flag = checkQueueOnRouteLine(pointList, list, limitDistance);
//					points.setOnRouteLine(flag);
				}

				//只要路线中有一个为true，则说明没有偏移路线
				boolean isAnyOnRoute = routePointsList.stream().anyMatch(s -> s.getOnRouteLine() == true);
				if (isAnyOnRoute) {
					//没有偏移，则认为当前路线就是运输的路线
					Optional<RoutePointsDTO> first = routePointsList.stream().filter(s -> s.getOnRouteLine() == true).findFirst();
					RoutePointsDTO points = first.get();
					//System.out.println("当前线路:" + points.toString());
				} else {
					System.out.println("发生路线偏移");
				}

			}
		}
	}

	/**
	 * 判断队列上的点是否在路线上,如果都没在线路上返回false则说明已经偏移路线
	 *
	 * @param pointList
	 * @param list
	 * @param limitDistance
	 * @return
	 */
//	private boolean checkQueueOnRouteLine(List<Point2D> pointList, QueueList<P_0200> list, double limitDistance) {
//		List<Boolean> boolList = new ArrayList<>();
//		for (P_0200 p : list) {
//			Point2D.Double point = new Point2D.Double(p.getSiteBasicMessage().getLongitude(),
//					p.getSiteBasicMessage().getLatitude());
//			boolean b = CoordinateUtil.pointOnRoute(pointList, point, limitDistance);
//			boolList.add(b);
//		}
//		//如果集合中所有的值都为false则说说明不在该路线上
//		return boolList.stream().allMatch(s -> s.booleanValue() == false) ? false : true;
//	}


	/**
	 * 获取路线
	 *
	 * @return
	 */
	private Map<Integer, List<RoutePointsDTO>> getRoutePointsMap() {
		routePointsMap = new HashMap<>();
		List<RoutePointsDTO> routePointList = new ArrayList<>();
		RoutePointsDTO routePoints = new RoutePointsDTO();
		List<Point2D> pointList = new ArrayList<>();
		pointList.add(new Point2D.Double(108.750734125773000, 35.15800));
		pointList.add(new Point2D.Double(108.650734125773000, 35.15810));
		pointList.add(new Point2D.Double(108.650734125773000, 35.15820));
		pointList.add(new Point2D.Double(108.650734125773000, 35.15830));
		pointList.add(new Point2D.Double(108.650734125773000, 35.15840));
		routePoints.setRouteId(1);
		routePoints.setRouteName("铜川到彬县线路1");
		routePoints.setPointList(pointList);
		routePointList.add(routePoints);

		RoutePointsDTO routePoints1 = new RoutePointsDTO();
		List<Point2D> pointList1 = new ArrayList<>();
		pointList1.add(new Point2D.Double(108.650734125773000f, 35.157957178239800f));
		pointList1.add(new Point2D.Double(108.650734125773000f, 35.157997178239800f));
		pointList1.add(new Point2D.Double(108.650734125773000f, 35.158007178239800f));
		pointList1.add(new Point2D.Double(108.650734125773000f, 35.158107178239800f));
		routePoints1.setRouteId(2);
		routePoints1.setRouteName("铜川到彬县线路2");
		routePoints1.setPointList(pointList1);
		routePointList.add(routePoints1);
		routePointsMap.put(1, routePointList);

		return routePointsMap;

	}
}
