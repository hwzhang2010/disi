package com.hywx.siin.vo;

public class SatelliteCoverVO {
	private String epoch;
	private String substar;
	private double radius;
	private String groundStations;
	
	public SatelliteCoverVO() {
	}

	public SatelliteCoverVO(String epoch, String substar, double radius, String groundStations) {
		this.epoch = epoch;
		this.substar = substar;
		this.radius = radius;
		this.groundStations = groundStations;
	}

	public SatelliteCoverVO(String epoch, double lng, double lat, double alt, double radius, String groundStations) {
		this.epoch = epoch;
		this.substar = String.format("(%.2fº, %.2fº, %.2fkm)", lng, lat, alt);
		this.radius = radius;
		this.groundStations = groundStations;
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public String getSubstar() {
		return substar;
	}

	public void setSubstar(String substar) {
		this.substar = substar;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getGroundStations() {
		return groundStations;
	}

	public void setGroundStations(String groundStations) {
		this.groundStations = groundStations;
	}

	@Override
	public String toString() {
		return "SatelliteCoverVO: {epoch=" + epoch + ", substar=" + substar + ", radius=" + radius + ", groundStations="
				+ groundStations + "}";
	}
	
	
	
	

}
