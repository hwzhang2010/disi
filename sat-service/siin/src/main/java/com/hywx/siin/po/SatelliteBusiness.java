package com.hywx.siin.po;

public class SatelliteBusiness {
	private String satelliteId;
	private double usage;
	private String condition;
	private String health;
	
	public SatelliteBusiness() {
	}

	public SatelliteBusiness(String satelliteId, double usage, String condition, String health) {
		this.satelliteId = satelliteId;
		this.usage = usage;
		this.condition = condition;
		this.health = health;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}
	
	

}
