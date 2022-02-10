package com.hywx.siin.util;

/**
 * 地理计算工具类
 * @author zhang.huawei
 *
 */
public class GisUtil {
	// 地球半径
	private static final double EARTH_RADIUS = 6378.137;
	
	/**
	 * 根据经纬度，计算两点间的距离
	 * @param lng  第1点的经度
	 * @param lat  第1点的纬度
	 * @param lng2  第2点的经度
	 * @param lat2 第2点的纬度
	 * @return 两点之间的距离，单位:km
	 */
	public static double getDistanceFromPosition(double lng, double lat, double lng2, double lat2) {
		// 纬度
        double latitude = Math.toRadians(lat);
        double latitude2 = Math.toRadians(lat2);
        // 经度
        double longitude = Math.toRadians(lng);
        double longitude2 = Math.toRadians(lng2);
        // 纬度之差
        double latDistance = latitude - latitude2;
        // 经度之差
        double lngDistance = longitude - longitude2;
        // 计算两点距离的公式
        double c = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDistance / 2), 2) +
                Math.cos(latitude) * Math.cos(latitude2) * Math.pow(Math.sin(lngDistance / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        c =  c * EARTH_RADIUS;
        
        return c;
	}
	
	public static double getDistanceFromPosition2(double lng, double lat, double lng2, double lat2) {
		double lngDistance = Math.toRadians(lng - lng2);
	    double latDistance = Math.toRadians(lat - lat2);
	 
	    double a = Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
	            + Math.cos(Math.toRadians(lng)) * Math.cos(Math.toRadians(lng2))
	            * Math.sin(latDistance / 2) * Math.sin(latDistance / 2);
	 
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    c *= EARTH_RADIUS;
	    
	    return c;
	}
	
	/**
	 * 根据经纬度，距离和方位角计算另一点的经纬度
	 * @param lng: 已知经度
	 * @param lat: 已知纬度
	 * @param distance: 距离,单位:km
	 * @param azimuth: 方位角,单位:度
	 * @return
	 */
	public static double[] getLngLat(double lng, double lat, double distance, double azimuth) {
		// 赤道半径
		final double Ea = 6378.138;
		// 极半径
		final double Eb = 6356.725; 
		
		double dx = distance * Math.sin(Math.toRadians(azimuth));
		double dy = distance * Math.cos(Math.toRadians(azimuth));
		double ec = Eb + (Ea -Eb) * (90 - lat) / 90.0;
		double ed = ec * Math.cos(Math.toRadians(lat));
		
		double newLng = Math.toDegrees((dx / ed + Math.toRadians(lng)));
		double newLat = Math.toDegrees(dy / ec + Math.toRadians(lat));
		
		return new double[] { newLng, newLat };
	}
	
	/**
	 * 在地图计算圆的外接正方形的左上顶点、右下顶点
	 * @param radius：圆的半径：km
	 * @param lng：圆心经度
	 * @param lat：圆心纬度
	 * @return 
	 */
	public static double[] getAround(double radius, double lng, double lat) {
		//the circumference of the earth is 24901 m
		double degree = (24901 * 1609) / 360.0 / 1000.0;
		
		double dpmLat = 1 / degree;
		double radiusLat = dpmLat * radius;
		double minLat = lat - radiusLat;
		double maxLat = lat + radiusLat;
		
		double mpdLng = degree * Math.cos(lat * Math.PI / 180.0);
		double dpmLng = 1 / mpdLng;
		double radiusLng = dpmLng * radius;
		double minLng = lng - radiusLng;
		double maxLng = lng + radiusLng;
		
		return new double[] { minLng, minLat, maxLng, maxLat };
	}
	
	/**
	 * 判断两个矩形是否相交
	 * @param x01
	 * @param x02
	 * @param y01
	 * @param y02
	 * @param x11
	 * @param x12
	 * @param y11
	 * @param y12
	 * @return
	 */
	public static boolean rectangleIntersect(double x01, double x02, double y01, double y02, double x11, double x12, double y11, double y12) {
		double zx = Math.abs(x01 + x02 - x11 - x12);
		double x = Math.abs(x01 - x02) + Math.abs(x11 - x12);
		double zy = Math.abs(y01 + y02 - y11 - y12);
		double y = Math.abs(y01 - y02) + Math.abs(y11 - y12);
		if (zx <= x && zy <= y)  //如果有=，则可能交于1点
			return true;
		
		return false;
	}
	

}
