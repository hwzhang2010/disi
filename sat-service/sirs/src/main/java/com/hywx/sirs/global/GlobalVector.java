package com.hywx.sirs.global;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.hywx.sirs.vo.ExchangeVO;
import com.hywx.sirs.vo.StationVO;

public class GlobalVector {
	//数据交互显示列表
	private static Vector<ExchangeVO> exchangeVertor;
	public static Vector<ExchangeVO> getExchangeVector() {
		if (exchangeVertor == null) 
			exchangeVertor = new Vector<ExchangeVO>();
		return exchangeVertor;
	}
	
	public static void putExchange(StationVO station) {
		getExchangeVector().add(new ExchangeVO(station));
	}
	
	public static void putExchange(List<StationVO> stationList) {
		for (StationVO station : stationList)
		    putExchange(station);
	}
	
	
	public static boolean updateRecvState(int port, String state) {
		getExchangeVector().stream().filter(obj -> { 
			if (obj.getRecvPort() == port) {
			    obj.setRecvState(state);
			    return true;
			}
			return false; 
			}).collect(Collectors.toList());
		
		return false;
	}
	
	public static boolean updateRecvCount(int port) {
		getExchangeVector().stream().filter(obj -> { 
			if (obj.getRecvPort() == port) {
				long count = obj.getRecvCount();
			    obj.setRecvCount(count + 1);
			    return true;
			}
			return false; 
			}).collect(Collectors.toList());
		
		return false;
	}
	
//	public static boolean updateSendState(InetSocketAddress address, String state) {
//		getExchangeVector().stream().filter(obj -> { 
//			if (obj.getSendPort() == address.getPort()) {
//			    obj.setSendState(state);
//			    return true;
//			}
//			return false; 
//			}).collect(Collectors.toList());
//		
//		return false;
//	}
	
	public static boolean updateSendState(String scode, String state) {
		getExchangeVector().stream().filter(obj -> { 
			if (scode.equals(obj.getScode())) {
			    obj.setSendState(state);
			    return true;
			}
			return false; 
			}).collect(Collectors.toList());
		
		return false;
	}
	
//	public static boolean updateSendCount(InetSocketAddress address) {
//		getExchangeVector().stream().filter(obj -> { 
//			if (obj.getSendPort() == address.getPort()) {
//				long count = obj.getSendCount();
//			    obj.setSendCount(count + 1);
//			    return true;
//			}
//			return false; 
//			}).collect(Collectors.toList());
//		
//		return false;
//	}
	
	public static boolean updateSendCount(String scode) {
		getExchangeVector().stream().filter(obj -> { 
			if (scode.equals(obj.getSendPort())) {
				long count = obj.getSendCount();
			    obj.setSendCount(count + 1);
			    return true;
			}
			return false; 
			}).collect(Collectors.toList());
		
		return false;
	}
	
	public static void updateZero() {
		for (ExchangeVO vo : exchangeVertor) {
			vo.setRecvCount(0);
			vo.setSendCount(0);
		}
	}
	
}
