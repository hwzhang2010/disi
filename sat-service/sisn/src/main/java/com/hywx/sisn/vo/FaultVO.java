package com.hywx.sisn.vo;

public class FaultVO {
	private String satelliteId;
	private String groundStationId;
	private Integer deviceId;
	private Integer mainId;
	private Integer subId;
	private Integer level;
	
	public FaultVO() {
	}

	public FaultVO(String satelliteId, String groundStationId, Integer deviceId, Integer mainId, Integer subId, Integer level) {
		this.satelliteId = satelliteId;
		this.groundStationId = groundStationId;
		this.deviceId = deviceId;
		this.mainId = mainId;
		this.subId = subId;
		this.level = level;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public String getGroundStationId() {
		return groundStationId;
	}

	public void setGroundStationId(String groundStationId) {
		this.groundStationId = groundStationId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getMainId() {
		return mainId;
	}

	public void setMainId(Integer mainId) {
		this.mainId = mainId;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "FaultVO: {satelliteId=" + satelliteId + ", groundStationId=" + groundStationId + ", deviceId=" + deviceId
				+ ", mainId=" + mainId + ", subId=" + subId + ", level=" + level + "}";
	}

	
	
	
	
	

}
