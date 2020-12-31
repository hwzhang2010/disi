package com.hywx.siin.po;

public class InjectionData {
	private int id;
	private long sendTime;
	private String satelliteId;
	private String stationId;
	private String data;
	
	public InjectionData() {
	}

	public InjectionData(long sendTime, String satelliteId, String stationId, String data) {
		this.sendTime = sendTime;
		this.satelliteId = satelliteId;
		this.stationId = stationId;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "InjectionData: {id=" + id + ", sendTime=" + sendTime + ", satelliteId=" + satelliteId + ", stationId="
				+ stationId + ", data=" + data + "}";
	}

	
	
	
	
	

}
