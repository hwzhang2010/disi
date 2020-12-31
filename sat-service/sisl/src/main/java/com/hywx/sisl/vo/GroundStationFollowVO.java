package com.hywx.sisl.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GroundStationFollowVO {
	private String epoch;
	private double azimuth;
	private double elevation;
	
	public GroundStationFollowVO() {
	}

	public GroundStationFollowVO(Date epoch, double azimuth, double elevation) {
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(epoch);   
        cal.add(Calendar.HOUR, 8);
		this.epoch = format.format(cal.getTime());
		
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
		return "GroundStationFollowVO: {epoch=" + epoch + ", azimuth=" + azimuth + ", elevation=" + elevation + "}";
	}
	
	
	

}
