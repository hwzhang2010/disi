package com.hywx.sisl.bo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;

/**
 * 简单工厂模式(静态工厂模式)：根据注册的参数，动态决定实例化的内容
 * 
 * @author zhang.huawei
 *
 */
public class PredictFactory {
	private PredictFactory() {
	}
	
	// sgp4计算使用的地面站和卫星
	private static Map<String, GroundStationPosition> groundStationMap;
	private static Map<String, Satellite> satelliteMap;
	
	// 静态初始化
	static {
		groundStationMap = new ConcurrentHashMap<>();
		satelliteMap = new ConcurrentHashMap<>();
	}
	
	public static void registerGroundStation(String groundStationId, final GroundStationPosition position) {
		// 实例化进行SGP4计算使用的地面站
		if (groundStationMap.containsKey(groundStationId))
			groundStationMap.remove(groundStationId);
		groundStationMap.put(groundStationId, position);
	}
	
	public static void registerSatellite(String sateliteId, final TLE tle) {
		// 实例化进行SGP4计算的卫星
		Satellite satellite = SatelliteFactory.createSatellite(tle);
		if (satelliteMap.containsKey(sateliteId))
			satelliteMap.remove(sateliteId);
		satelliteMap.put(sateliteId, satellite);
    }
	
	public static Map<String, GroundStationPosition> getGroundStationMap() {
		return groundStationMap;
	}
	
	public static Map<String, Satellite> getSatelliteMap() {
		return satelliteMap;
	}
	
	public static GroundStationPosition getGroundStationPosition(String key) {
		if (groundStationMap.containsKey(key))
		    return groundStationMap.get(key);
		
		return null;
	}

    public static Satellite getSatellite(String key) {
    	if (satelliteMap.containsKey(key))
            return satelliteMap.get(key);
    	
    	return null;
    }

}
