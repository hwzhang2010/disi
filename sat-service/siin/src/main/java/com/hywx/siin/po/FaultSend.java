package com.hywx.siin.po;

public class FaultSend {
	// 0表示卫星，1表示信关站
	private Integer type;
	private String satelliteId;
	private String groundStationId;
	private Integer deviceId;
	private Integer mainId;
    private String mainName;
	private Integer subId;
	private String subName;
	private Integer level;
	private Boolean waiting;
	
	public FaultSend() {
	}

	public FaultSend(Integer type, String satelliteId, String groundStationId, Integer deviceId, Integer mainId,
			String mainName, Integer subId, String subName, Integer level, Boolean waiting) {
		this.type = type;
		this.satelliteId = satelliteId;
		this.groundStationId = groundStationId;
		this.deviceId = deviceId;
		this.mainId = mainId;
		this.mainName = mainName;
		this.subId = subId;
		this.subName = subName;
		this.level = level;
		this.waiting = waiting;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getWaiting() {
		return waiting;
	}

	public void setWaiting(Boolean waiting) {
		this.waiting = waiting;
	}

	
    
	
	
	

}
