package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteSingle {
	private String epoch;
	private String substar;
	private double subAngle;
	private double earthAngle;
	private double coverArea;
	
	public SatelliteSingle()  {
	}

	public SatelliteSingle(String epoch, String substar, double subAngle, double earthAngle, double coverArea) {
		this.epoch = epoch;
		this.substar = substar;
		this.subAngle = subAngle;
		this.earthAngle = earthAngle;
		this.coverArea = coverArea;
	}
	
	public SatelliteSingle(Date date, double lng, double lat, double alt, double subAngle, double earthAngle, double coverArea) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		double lng2 = new BigDecimal(lng).setScale(2, RoundingMode.UP).doubleValue();
		while (lng2 > 180)
			lng2 -= 360;
		double lat2 = new BigDecimal(lat).setScale(2, RoundingMode.UP).doubleValue();
		double alt2 = new BigDecimal(alt).setScale(2, RoundingMode.UP).doubleValue();
		this.substar = String.format("(%.2fº, %.2fº, %.2fkm)", lng2, lat2, alt2);
		
		this.subAngle = new BigDecimal(subAngle).setScale(4, RoundingMode.UP).doubleValue();
		this.earthAngle = new BigDecimal(earthAngle).setScale(4, RoundingMode.UP).doubleValue();
		this.coverArea = new BigDecimal(coverArea).setScale(4, RoundingMode.UP).doubleValue();
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

	public double getSubAngle() {
		return subAngle;
	}

	public void setSubAngle(double subAngle) {
		this.subAngle = subAngle;
	}

	public double getEarthAngle() {
		return earthAngle;
	}

	public void setEarthAngle(double earthAngle) {
		this.earthAngle = earthAngle;
	}

	public double getCoverArea() {
		return coverArea;
	}

	public void setCoverArea(double coverArea) {
		this.coverArea = coverArea;
	}

	@Override
	public String toString() {
		return "SatelliteSingle: {epoch=" + epoch + ", substar=" + substar + ", subAngle=" + subAngle + ", earthAngle="
				+ earthAngle + ", coverArea=" + coverArea + "}";
	}

	
	
	
	

}
