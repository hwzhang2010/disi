package com.hywx.sisn.vo;

import com.hywx.siin.po.FaultSend;

public class FaultSatelliteVO {
	private String satelliteId;
	private Integer level;
	private Integer device;

	public FaultSatelliteVO() {
	}

	public FaultSatelliteVO(String satelliteId, Integer level, Integer device) {
		this.satelliteId = satelliteId;
		this.level = level;
		this.device = device;
	}
	
	public FaultSatelliteVO(FaultSend faultSend) {
		this.satelliteId = faultSend.getSatelliteId();
		this.device = faultSend.getDeviceId();
		this.level = faultSend.getLevel();
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getDevice() {
		return device;
	}

	public void setDevice(Integer device) {
		this.device = device;
	}
	
	
	
}

