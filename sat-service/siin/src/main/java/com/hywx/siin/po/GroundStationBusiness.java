package com.hywx.siin.po;

public class GroundStationBusiness {
	private String groundStationId;
	private double usage;
	private String equipment;
	private String carrier;
	private String health;
	
	public GroundStationBusiness() {
	}

	public GroundStationBusiness(String groundStationId, double usage, String equipment, String carrier, String health) {
		this.groundStationId = groundStationId;
		this.usage = usage;
		this.equipment = equipment;
		this.carrier = carrier;
		this.health = health;
	}

	public String getGroundStationId() {
		return groundStationId;
	}

	public void setGroundStationId(String groundStationId) {
		this.groundStationId = groundStationId;
	}

	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}
	
	
	
	

}
