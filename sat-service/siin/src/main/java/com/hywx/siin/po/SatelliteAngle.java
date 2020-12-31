package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteAngle {
	private String epoch;
	private double azimuth;
	private double elevation;
	
	public SatelliteAngle() {
	}

	public SatelliteAngle(String epoch, double azimuth, double elevation) {
		this.epoch = epoch;
		this.azimuth = azimuth;
		this.elevation = elevation;
	}
	
	public SatelliteAngle(Date date, double azimuth, double elevation) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		
		this.azimuth = new BigDecimal(azimuth).setScale(2, RoundingMode.UP).doubleValue();
		this.elevation = new BigDecimal(elevation).setScale(2, RoundingMode.UP).doubleValue();
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	@Override
	public String toString() {
		return "SatelliteAngle: {epoch=" + epoch + ", azimuth=" + azimuth + ", elevation=" + elevation + "}";
	}
	
	
	

}
