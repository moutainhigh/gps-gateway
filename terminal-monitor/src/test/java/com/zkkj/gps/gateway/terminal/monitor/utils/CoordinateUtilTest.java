package com.zkkj.gps.gateway.terminal.monitor.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chailixing
 * 2019/4/9 10:17
 * 区域工具测试
 */
public class CoordinateUtilTest extends BaseTest {
	// 记下时间和匹配点数
	private long l;
	private long count;

	@Before
	public void before() {
		l = System.currentTimeMillis();
	}

	@After
	public void after() {
		System.out.println("耗时" + (System.currentTimeMillis() - l) + "ms");
		System.out.println("点总数: " + pointList.size());
		System.out.println("匹配点数: " + count);
	}

	// 线路点是否在圆内
	@Test
	public void isInCircle() throws Exception {
		// 禾草沟-铜川电厂标准线路
		count = pointList.stream()
				//.peek(x -> System.out.print("pointInfo: " + x))
				// 禾草沟发货区 -> 圆
				.map(point -> CoordinateUtil.isInCircle(2500,
						37.0813118489583, 109.555677625868, point.x, point.y))
				//.peek(x -> System.out.println(x ? " successful!" : " failing!"))
				.filter(result -> result.equals(Boolean.TRUE))
				.count();
	}

	// 线路点是否在区域内
	@Test
	public void isPtInPoly() throws Exception {
		List<Point2D.Double> areaList = new ArrayList<>();
		areaList.add(new Point2D.Double(37.093626423289, 109.534077097737));
		areaList.add(new Point2D.Double(37.0941856484413, 109.555311577661));
		areaList.add(new Point2D.Double(37.0708108494937, 109.568120013151));
		areaList.add(new Point2D.Double(37.0691893406045, 109.538300997319));
		// 禾草沟-铜川电厂标准线路
		count = pointList.stream()
				//.peek(x -> System.out.print("pointInfo: " + x))
				// 自定义发货区域 -> 多边形
				.map(point -> CoordinateUtil.isPtInPoly(areaList, point))
				//.peek(x -> System.out.println(x ? " successful!" : " failing!"))
				.filter(result -> result.equals(Boolean.TRUE))
				.count();
	}

	//获取所有点坐标
	public List<Point2D.Double> returnPoint() {
		List<Point2D.Double> areaList = new ArrayList<>();
		if (pointList != null) {
			for (int i = 0; i < pointList.size(); i++) {
				areaList.add(new Point2D.Double(pointList.get(i).x, pointList.get(i).y));
			}
		}
		return areaList;
	}
}