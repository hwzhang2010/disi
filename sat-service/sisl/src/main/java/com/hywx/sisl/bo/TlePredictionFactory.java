package com.hywx.sisl.bo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hywx.sisl.orbit.tle.prediction.Tle;


/**
 * 简单工厂模式(静态工厂模式)：根据注册的参数，动态决定实例化的内容
 * 
 * @author zhang.huawei
 *
 */
public class TlePredictionFactory {
	private TlePredictionFactory() {
	}
	
	// sgp4计算卫星位置和速度(GPS)使用的两行根数
    private static Map<String, Tle> tleMap;
    
    // 静态初始化
    static {
    	tleMap = new ConcurrentHashMap<>();
    }
    
    public static void registerTle(String satelliteId, final Tle tle) {
    	// 实例化进行SGP4计算使用的卫星两行根数
    	if (tleMap.containsKey(satelliteId))
    		tleMap.remove(satelliteId);
    	tleMap.put(satelliteId, tle);
    }
    
    public static Map<String, Tle> getTleMap() {
    	return tleMap;
    }
    
    public static Tle getTle(String satelliteId) {
    	if (tleMap.containsKey(satelliteId))
    		return tleMap.get(satelliteId);
    	
    	return null;
    }
    

}
