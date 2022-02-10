package com.hywx.siin.po;

public class StationNetState {
	private Integer id;
	private String groundStationName;
	private Integer subsystemId;
	private String equipmentId;
	private String warning;
	private Integer healthLevel;
	
	public StationNetState() {
	}

	public StationNetState(Integer id, String groundStationName, Integer subsystemId, String equipmentId, String warning, Integer healthLevel) {
		this.id = id;
		this.groundStationName = groundStationName;
		this.subsystemId = subsystemId;
		this.equipmentId = equipmentId;
		this.warning = warning;
		this.healthLevel = healthLevel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroundStationName() {
		return groundStationName;
	}

	public void setGroundStationName(String groundStationName) {
		this.groundStationName = groundStationName;
	}

	public Integer getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(Integer subsystemId) {
		this.subsystemId = subsystemId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public Integer getHealthLevel() {
		return healthLevel;
	}

	public void setHealthLevel(Integer healthLevel) {
		this.healthLevel = healthLevel;
	}

	@Override
	public String toString() {
		return "StationNetState: {id=" + id + ", groundStationName=" + groundStationName + ", subsystemId=" + subsystemId
				+ ", equipmentId=" + equipmentId + ", warning=" + warning + ", healthLevel=" + healthLevel + "}";
	}
	
	
	

}
