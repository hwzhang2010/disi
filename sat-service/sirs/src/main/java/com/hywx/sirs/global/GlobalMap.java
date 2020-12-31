package com.hywx.sirs.global;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.hywx.sirs.bo.StationData;

/**
 * 缓冲队列
 * @author zhang.huawei
 *
 */
public class GlobalMap {
	//队列容量
	private static final int QUEUE_SIZE = 1024;
	//缓冲数据队列
	private static Map<String, LinkedBlockingQueue<byte[]>> stationDataMap;
	//站址 - 站码映射
	private static Map<InetSocketAddress, String> stationAddressMap;
	//站码 - 连接状态
	private static Map<String, Boolean> stationConnectionMap;
	
	public static String getStationByAddress(InetSocketAddress address) {
		if (stationAddressMap == null || !stationAddressMap.containsKey(address))
			return null;
		
		return stationAddressMap.get(address);
	}
	
	public static void putStationAddress(InetSocketAddress address, String station) {
		if (stationAddressMap == null)
			stationAddressMap = new ConcurrentHashMap<>();
		
		stationAddressMap.put(address, station);
	}
	
	public static boolean getStationConnection(String station) {
		if (stationConnectionMap == null || !stationConnectionMap.containsKey(station))
		    return false;
		
		return stationConnectionMap.get(station);
	}
	
	public static void putStationConnection(String station, boolean connection) {
		if (stationConnectionMap == null)
			stationConnectionMap = new ConcurrentHashMap<>();
		
		stationConnectionMap.put(station, connection);
	}
	
	public static void putStationConnection(InetSocketAddress address, boolean connection) {
		String station = getStationByAddress(address);
		if (station != null)
			putStationConnection(station, connection);
	}
	
	
	public static void putStation(String station) {
		if (stationDataMap == null)
			stationDataMap = new ConcurrentHashMap<>();
		
		if (stationDataMap.containsKey(station)) 
			stationDataMap.remove(station);
		stationDataMap.put(station, new LinkedBlockingQueue<byte[]>(QUEUE_SIZE));
	}
	
	public static void putStationList(List<String> stationList) {
		if (stationDataMap == null)
			stationDataMap = new ConcurrentHashMap<>();
		
		stationDataMap.clear();
		for (String station : stationList) {
			stationDataMap.put(station, new LinkedBlockingQueue<byte[]>(QUEUE_SIZE));
		}
	}
	
	
	public static synchronized void putStationData(String station, byte[] data) {
		if (!stationDataMap.containsKey(station))
			stationDataMap.put(station, new LinkedBlockingQueue<byte[]>(QUEUE_SIZE));
		
		try {
			if (stationDataMap.get(station).size() >= QUEUE_SIZE)
				stationDataMap.get(station).poll();
			stationDataMap.get(station).put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void putStationData(StationData stationData) {
		if (stationDataMap == null)
			stationDataMap = new ConcurrentHashMap<>();
		
		putStationData(stationData.getStation(), stationData.getData());
	}
	
	public static byte[] getStationData(String station) {
		if (stationDataMap == null || !stationDataMap.containsKey(station))
			return null;
		
		return stationDataMap.get(station).poll();
	}
	
	public static synchronized byte[] getStationData(InetSocketAddress address) {
		if (stationAddressMap == null || !stationAddressMap.containsKey(address))
			return null;
		String station = stationAddressMap.get(address);
		
		return getStationData(station);
	}
	
	private static boolean isStationEmpty(String station) {
		if (stationDataMap == null || !stationDataMap.containsKey(station))
			return false;
		
		return stationDataMap.get(station).isEmpty();
	}
	
    public static synchronized boolean isStationEmpty(InetSocketAddress address) {
    	if (stationAddressMap == null || !stationAddressMap.containsKey(address))
			return false;
		String station = stationAddressMap.get(address);
		
		return isStationEmpty(station);
	}
    
}
