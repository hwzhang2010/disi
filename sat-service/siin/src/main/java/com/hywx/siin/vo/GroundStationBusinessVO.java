package com.hywx.siin.vo;

import com.hywx.siin.po.GroundStationBusiness;

public class GroundStationBusinessVO {
	private String groundStationId;
	private double lng;
	private double lat;
	private double usage;
	private String equipment;
	private String carrier;
	private String health;
	
	public GroundStationBusinessVO() {
	}

	public GroundStationBusinessVO(String groundStationId, double lng, double lat, GroundStationBusiness business) {
		this.groundStationId = groundStationId;
		this.lng = lng;
		this.lat = lat;
		this.usage = business.getUsage();
		this.equipment = business.getEquipment();
		this.carrier = business.getCarrier();
		this.health = business.getHealth();
	}

	public String getGroundStationId() {
		return groundStationId;
	}

	public void setGroundStationId(String groundStationId) {
		this.groundStationId = groundStationId;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
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

	@Override
	public String toString() {
		return "GroundStationBusinessVO: {groundStationId=" + groundStationId + ", lng=" + lng + ", lat=" + lat
				+ ", usage=" + usage + ", equipment=" + equipment + ", carrier=" + carrier + ", health=" + health + "}";
	}

	
	
	
	

}
