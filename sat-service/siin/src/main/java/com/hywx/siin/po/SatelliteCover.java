package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteCover {
	private String epoch;
	private String substar;
	private double radius;
	private String groundStations;
	
	public SatelliteCover() {
	}

	public SatelliteCover(String epoch, String substar, double radius, String groundStations) {
		this.epoch = epoch;
		this.substar = substar;
		this.radius = radius;
		this.groundStations = groundStations;
	}
	
	public SatelliteCover(Date date, double lng, double lat, double alt, double radius, String groundStations) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		double lng2 = new BigDecimal(lng).setScale(2, RoundingMode.UP).doubleValue();
		while (lng2 > 180)
			lng2 -= 360;
		double lat2 = new BigDecimal(lat).setScale(2, RoundingMode.UP).doubleValue();
		double alt2 = new BigDecimal(alt).setScale(2, RoundingMode.UP).doubleValue();
		this.substar = String.format("(%.2fº, %.2fº, %.2fkm)", lng2, lat2, alt2);
		this.radius = new BigDecimal(radius).setScale(4, RoundingMode.UP).doubleValue();
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
		return "SatelliteCover: {epoch=" + epoch + ", substar=" + substar + ", radius=" + radius + ", groundStations="
				+ groundStations + "}";
	}

	
	
	
	
	

}
