package com.hywx.sisn.vo;

import com.hywx.siin.po.FaultSend;

public class FaultGroundStationVO {
	private String groundStationId;
	private String groundStationText;
	private Integer level;
	private Integer device;

	public FaultGroundStationVO() {
	}

	public FaultGroundStationVO(String groundStationId, String groundStationText, Integer level, Integer device) {
		this.groundStationId = groundStationId;
		this.groundStationText = groundStationText;
		this.level = level;
		this.device = device;
	}
	
	public FaultGroundStationVO(FaultSend faultSend, String groundStationText) {
		this.groundStationId = faultSend.getGroundStationId();
		this.groundStationText = groundStationText;
		this.level = faultSend.getLevel();
		this.device = faultSend.getDeviceId();
	}

	public String getGroundStationId() {
		return groundStationId;
	}

	public void setGroundStationId(String groundStationId) {
		this.groundStationId = groundStationId;
	}

	public String getGroundStationText() {
		return groundStationText;
	}

	public void setGroundStationText(String groundStationText) {
		this.groundStationText = groundStationText;
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
