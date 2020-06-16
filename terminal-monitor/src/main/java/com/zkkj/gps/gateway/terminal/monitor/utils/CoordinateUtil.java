package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * author : cyc
 * Date : 2019-04-01
 * 坐标工具类
 */
public class CoordinateUtil {


    /**
     * 地球半径
     */
    private static double EARTH_RADIUS = 6378138.0;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 判断是否在矩形区域内
     *
     * @param lat    测试点纬度
     * @param lng    测试点经度
     * @param minLat 纬度范围限制1
     * @param maxLat 纬度范围限制2
     * @param minLng 经度范围限制1
     * @param maxLng 经度范围限制2
     * @return 未在范围内，true再范围内
     **/
    public static boolean isInRectangleArea(double lat, double lng, double minLat, double maxLat, double minLng, double maxLng) {
        if (isInRange(lat, minLat, maxLat)) {
            if (minLng * maxLng > 0) {
                if (isInRange(lng, minLng, maxLng)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (Math.abs(minLng) + Math.abs(maxLng) < 180) {
                    if (isInRange(lng, minLng, maxLng)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    double left = Math.max(minLng, maxLng);
                    double right = Math.min(minLng, maxLng);
                    if (isInRange(lng, left, 180) || isInRange(lng, right, -180)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否在经纬度范围内
     *
     * @param point
     * @param left
     * @param right
     * @return boolean
     * @throws
     * @Title: isInRange
     */
    public static boolean isInRange(double point, double left, double right) {
        if (point >= Math.min(left, right) && point <= Math.max(left, right)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param radius 半径(米)
     * @return false 未在圆的范围内，true在圆的范围内
     * @Description 计算是否在圆上（单位、千米）
     * @Param lat1 纬度
     * @Param lng1 经度
     * @Param lat2 纬度
     * @Param lng2 经度
     **/
    public static boolean isInCircle(double radius, double lat1, double lng1, double lat2, double lng2) {
        double s = getDistance(lat1, lng1, lat2, lng2);
        if (s > radius) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param
     * @return
     * @Description 计算两个经纬度之间的距离(米)
     * @author liuxin
     * @date 2018/10/31 16:00
     **/
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(
                Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }

    /**
     * @param
     * @return
     * @Description 计算两个点之间的距离(米)
     * @author liuxin
     * @date 2018/10/31 16:00
     **/
    public static double getDistance(Point2D p1, Point2D p2) {
        double radLat1 = rad(p1.getX());
        double radLat2 = rad(p2.getX());
        double a = radLat1 - radLat2;
        double b = rad(p1.getY()) - rad(p2.getY());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(
                Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /**
     * @param polygon 多边形坐标点
     * @param point   需要判断的点坐标
     * @return false 未在范围内，true再范围内
     * @Description 判断点是否在多边形内
     **/
    public static boolean isPtInPoly(List<Point2D.Double> polygon, Point2D.Double point) {
        boolean boundOrVertex = true;//如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;
        double precision = 2e-10;//浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;//neighbour bound vertices--临近绑定顶点
        Point2D.Double p = point; //当前点
        p1 = polygon.get(0);//left vertex--左顶点
        for (int i = 1; i <= polygon.size(); ++i) {//check all rays--检查所有射线
            if (p.equals(p1))
                return boundOrVertex;//p is an vertex--p是一个顶点
            p2 = polygon.get(i % polygon.size());//right vertex--右顶点
            if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {//ray is outside of our interests--射线不在我们的兴趣范围之内
                p1 = p2;
                continue;//next ray left point--下一条射线的左边点
            }
            if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {//ray is crossing over by the algorithm(basebean part of)--射线被算法穿越(常见的一部分)
                if (p.y <= Math.max(p1.y, p2.y)) {//x is before of ray--x在射线之前
                    if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {//overlies on a horizontal ray--在一条水平射线上
                        return boundOrVertex;
                    }
                    if (p1.y == p2.y) {//ray is vertical--射线是垂直的
                        if (p1.y == p.y) {//overlies on a vertical ray--覆盖在垂直光线上
                            return boundOrVertex;
                        } else {//before ray--射线之前
                            ++intersectCount;
                        }
                    } else {//cross point on the left side--左边的交叉点
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y--y的交叉点
                        if (Math.abs(p.y - xinters) < precision) {//overlies on a ray--覆盖在射线
                            return boundOrVertex;
                        }
                        if (p.y < xinters) {//before ray--射线之前
                            ++intersectCount;
                        }
                    }
                }
            } else {//special case when ray is crossing through the vertex--特殊情况下，当射线穿过顶点
                if (p.x == p2.x && p.y <= p2.y) {//p crossing over p2--p交叉p2
                    Point2D.Double p3 = polygon.get((i + 1) % polygon.size());//next vertex--下一个顶点
                    if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {//p.x lies between p1.x & p3.x--p.x在p1.x和p3.x之间
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;//next ray left point--下一条射线的左边点
        }
        if (intersectCount % 2 == 0) {//偶数在多边形外
            return false;
        } else {//奇数在多边形内
            return true;
        }
    }

    /**
     * @return
     * @Description 计算点到直线的最短距离
     * P1和p2确定一条直线和p0之间的距离
     **/
    public static double lineSegPointDist(Point2D p1, Point2D p2, Point2D p0) {
        double dist;
        if (dot(p1, p2, p0) > 0) {
            dist = getDistance(p2, p0);
            return dist;
        }
        if (dot(p2, p1, p0) > 0) {
            dist = getDistance(p1, p0);
            return dist;
        }
        if (getDistance(p1, p2) == 0.0) {
          return getDistance(p1,p0);
        }
        dist = Math.abs(cross(p1, p2, p0) / getDistance(p1, p2));
        return dist;
    }

    /**
     * @return
     * @Description 计算折线段的拐向判断
     * p1和p2是线路上的两个点p0是车辆定位点
     **/
    public static double dot(Point2D p1, Point2D p2, Point2D p0) {
        Point2D ab = new Point2D.Double(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        Point2D bc = new Point2D.Double(p0.getX() - p2.getX(), p0.getY() - p2.getY());
        return (ab.getX() * bc.getX()) + (ab.getY() * bc.getY());
    }

    /**
     * @param
     * @return
     * @Description 计算叉积
     **/
    public static double cross(Point2D p1, Point2D p2, Point2D p0) {
        double ESP = 1E+05;
        Point2D ab = new Point2D.Double(p2.getX() * ESP - p1.getX() * ESP, p2.getY() * ESP - p1.getY() * ESP);
        Point2D ac = new Point2D.Double(p0.getX() * ESP - p1.getX() * ESP, p0.getY() * ESP - p1.getY() * ESP);
        double c = (ab.getX() * ac.getY()) - (ab.getY() * ac.getX());
        return c;
    }

    /**
     * @param points  当前线路点集合
     * @param pt      车辆当前位置
     * @param minDist 车辆当前位置到线路的距离超过次距离则为偏移线路 单位米
     * @return false 未在线路上 true 在线路上
     * @Description 判断当前定位点是否偏移线路
     **/
    public static boolean pointOnRoute(List<Point2D> points, Point2D pt, double minDist) {
        boolean flag = false;
        for (int i = 0; i < points.size() - 2; i++) {
            double dist = lineSegPointDist(points.get(i), points.get(i + 1), pt);
            if (dist < minDist) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取当前点到区域的最短距离
     *
     * @param points
     * @param pt
     * @return
     */
    public static double pointToRouteDist(List<Point2D> points, Point2D pt) {
        List<Double> list = Lists.newArrayList();
        if(points.size() <3){
            return 0;
        }
        for (int i = 0; i < points.size() - 2; i++) {
            double dist = lineSegPointDist(points.get(i), points.get(i + 1), pt);
            list.add(dist);
        }
        return list.stream().mapToDouble(s -> s.doubleValue()).summaryStatistics().getMin();
    }

    public static void main(String[] args) {
        /*Point2D.Double center = new Point2D.Double(116.351408, 40.057942);
        Point2D.Double taget = new Point2D.Double(116.358409, 40.057942);
        System.out.println(isInCircle(3000, center.getY(), center.getX(), taget.getY(), taget.getX()));*/
        System.out.println(Math.abs(0.1 / 0.1));
    }
}
