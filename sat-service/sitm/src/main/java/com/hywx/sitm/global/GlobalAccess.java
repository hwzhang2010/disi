package com.hywx.sitm.global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.hywx.sitm.bo.SitmCmd;
import com.hywx.sitm.bo.SitmGpsFromExternal;
import com.hywx.sitm.po.GpsFrame;

/**
 * 全局访问的变量
 * @author zhang.huawei
 *
 */
public class GlobalAccess {
	
	//记录所在卫星组的卫星
	private static Vector<String> groupSatelliteVector;
	public static boolean isInGroup(String satelliteId) {
		if (groupSatelliteVector == null || groupSatelliteVector.isEmpty())
			return false;
		
		return groupSatelliteVector.contains(satelliteId);
	}
	public static Vector<String> getGroupSatellites() {
		return groupSatelliteVector;
	}
	public static void putSatellite2Group(String satelliteId) {
		if (groupSatelliteVector == null)
			groupSatelliteVector = new Vector<>();
		
		groupSatelliteVector.add(satelliteId);
	}
	public static void putSatellite2Group(List<String> satelliteIdList) {
		for (int i = 0; i < satelliteIdList.size(); i++) {
			putSatellite2Group(satelliteIdList.get(i));
		}
	}
	
	
	//GPS数据值，外测驱动使用
	private static Map<String, SitmGpsFromExternal> sitmGpsFromExternalMap;
	public static SitmGpsFromExternal getSitmGpsFromExternal(String satelliteId) {
		if (isInGroup(satelliteId)) 
			return sitmGpsFromExternalMap.get(satelliteId);
			
		return null;
	}
	public static void putSitmGpsFromExternal(String satelliteId, SitmGpsFromExternal sitmGpsFromExternal) {
		if (sitmGpsFromExternalMap == null)
			sitmGpsFromExternalMap = new ConcurrentHashMap<>();
		
		sitmGpsFromExternalMap.put(satelliteId, sitmGpsFromExternal);
	} 
	public static void setSitmGpsFromExternal(List<String> satelliteIdList) {
		sitmGpsFromExternalMap = new ConcurrentHashMap<>();
		for (int i = 0; i < satelliteIdList.size(); i++) {
			sitmGpsFromExternalMap.put(satelliteIdList.get(i), new SitmGpsFromExternal());
		}
	}
		
	
	//GPS数据源, 自动发送使用
	private static Vector<GpsFrame> gpsFrameVector;
    public static GpsFrame getGpsFrame(String satelliteId) {
    	if (gpsFrameCountMap == null)
    	    return null;
    		
		int index = gpsFrameCountMap.get(satelliteId);
		setGpsFrameCountMap(satelliteId, index + 1);
		
		return gpsFrameVector.get(index);
	}
	public static void setGpsFrameVector(List<GpsFrame> gpsFrameList) {
		if (gpsFrameVector == null)
			gpsFrameVector = new Vector<GpsFrame>();
		Iterator<GpsFrame> it = gpsFrameList.iterator();
		while (it.hasNext()) {
		    gpsFrameVector.add(it.next());
		}
	}
	
	//记录单个卫星发送GPS数据的索引， 自动发送使用
	private static Map<String, Integer> gpsFrameCountMap;
	public static void setGpsFrameCountMap(String satelliteId, Integer index) {
		if (gpsFrameCountMap == null)
			gpsFrameCountMap = new HashMap<>();
		if (index >= gpsFrameVector.size())
			index = 0;
		
		gpsFrameCountMap.put(satelliteId, index);
	}
	
	
	
	
	
	
	
	
	
	

}
