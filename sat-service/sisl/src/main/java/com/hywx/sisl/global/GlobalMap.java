package com.hywx.sisl.global;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓冲队列
 * @author zhang.huawei
 *
 */
public class GlobalMap {
	
	//缓冲站址 - 站码映射队列
	private static Map<String, Long> stationMap;
	
	public static void putStation(String station) {
		if (stationMap == null)
			stationMap = new ConcurrentHashMap<>();
		
		if (stationMap.containsKey(station)) 
			stationMap.remove(station);
		
		//如果字符串以0x开头，去掉0x
		station = station.startsWith("0x") ? station.substring(2) : station;
		Long value = Long.valueOf(station, 16);
		
		stationMap.put(station, value);
	}
	
	public static void putStationList(List<String> stationList) {
		if (stationMap == null)
			stationMap = new ConcurrentHashMap<>();
		
		stationMap.clear();
		for (String station : stationList) {
			//如果字符串以0x开头，去掉0x
			station = station.startsWith("0x") ? station.substring(2) : station;
			Long value = Long.valueOf(station, 16);
			
			stationMap.put(station, value);
		}
	}
	
	
	public static long getStation(String station) {
		if (stationMap == null || !stationMap.containsKey(station))
			return -1;
		
		return stationMap.get(station);
	}
	
	
    
}
