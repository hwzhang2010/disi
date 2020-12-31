package com.hywx.siin.po;

import java.util.Arrays;

public class TcData {
	private long sendTime;
	private String satelliteId;
	private String stationId;
	private String data;
	
	public TcData() {
	}

	public TcData(long sendTime, String satelliteId, String stationId, String data) {
		this.sendTime = sendTime;
		this.satelliteId = satelliteId;
		this.stationId = stationId;
		this.data = data;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TcData: {sendTime=" + sendTime + ", satelliteId=" + satelliteId + ", stationId=" + stationId + ", data="
				+ data + "}";
	}
	
	
	
	

}
