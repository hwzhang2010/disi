package com.hywx.siin.vo;

import com.hywx.siin.po.SatelliteBusiness;

public class SatelliteBusinessVO {
	private String satelliteId;
	private double lng;
	private double lat;
	private double usage;
	private String condition;
	private String health;
	
	public SatelliteBusinessVO() {
	}

	public SatelliteBusinessVO(String satelliteId, double lng, double lat, SatelliteBusiness business) {
		this.satelliteId = satelliteId;
		this.lng = lng;
		this.lat = lat;
		this.usage = business.getUsage();
		this.condition = business.getCondition();
		this.health = business.getHealth();
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
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

	@Override
	public String toString() {
		return "SatelliteBusinessVO: {satelliteId=" + satelliteId + ", lng=" + lng + ", lat=" + lat + ", usage=" + usage
				+ ", condition=" + condition + ", health=" + health + "}";
	}

	
	
	

}
