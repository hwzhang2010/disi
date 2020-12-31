package com.hywx.sisl.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteCoverVO {
    private String epoch;
    private String groundStations;
    private double radius;
    
	public SatelliteCoverVO() {
	}
	
	public SatelliteCoverVO(String epoch, String groundStations, double radius) {
		this.epoch = epoch;
		this.groundStations = groundStations;
		this.radius = radius;
	}
	
	public SatelliteCoverVO(Date date, String groundStations, double radius) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		this.epoch = format.format(date);
		this.groundStations = groundStations;
		this.radius = radius;
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public String getGroundStations() {
		return groundStations;
	}

	public void setGroundStations(String groundStations) {
		this.groundStations = groundStations;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
    
    
	
	
	
}
