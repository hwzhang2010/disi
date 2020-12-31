package com.hywx.sitm.vo;

public class SitmSatelliteVO {
	private String satelliteId;
	private boolean isRunning;
	private String sendType;
	
	public SitmSatelliteVO() {
	}

	public SitmSatelliteVO(String satelliteId, boolean isRunning) {
		this.satelliteId = satelliteId;
		this.isRunning = isRunning;
		this.sendType = "autoSend";
	}

	public SitmSatelliteVO(String satelliteId, boolean isRunning, String sendType) {
		this.satelliteId = satelliteId;
		this.isRunning = isRunning;
		this.sendType = sendType;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
   

}
