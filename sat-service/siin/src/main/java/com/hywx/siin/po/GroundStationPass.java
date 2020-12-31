package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.github.amsacode.predict4java.SatPassTime;

public class GroundStationPass {
	private String startTime;
	private String endTime;
	private double duration;
	private int aosAzimuth;
	private double maxElevation;
	private int losAzimuth;
	
	public GroundStationPass() {
	}

	public GroundStationPass(String startTime, String endTime, double duration, int aosAzimuth, double maxElevation, int losAzimuth) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.aosAzimuth = aosAzimuth;
		this.maxElevation = maxElevation;
		this.losAzimuth = losAzimuth;
	}
	
	public GroundStationPass(SatPassTime satPassTime) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		Calendar cal = Calendar.getInstance();   
		
		Date startTime = satPassTime.getStartTime();
		cal.setTime(startTime);   
        cal.add(Calendar.HOUR, 8);
		this.startTime = format.format(cal.getTime());
		
		Date endTime = satPassTime.getEndTime();
		cal.setTime(endTime);
		cal.add(Calendar.HOUR, 8);
		this.endTime = format.format(cal.getTime());
		
		double duration = (endTime.getTime() - startTime.getTime()) / 60000.0;
		// 四舍五入，保留2位小数
		this.duration = new BigDecimal(duration).setScale(2, RoundingMode.UP).doubleValue();
		this.aosAzimuth = satPassTime.getAosAzimuth();
		this.maxElevation = new BigDecimal(satPassTime.getMaxEl()).setScale(2, RoundingMode.UP).doubleValue();
		this.losAzimuth = satPassTime.getLosAzimuth();
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public int getAosAzimuth() {
		return aosAzimuth;
	}

	public void setAosAzimuth(int aosAzimuth) {
		this.aosAzimuth = aosAzimuth;
	}

	public double getMaxElevation() {
		return maxElevation;
	}

	public void setMaxElevation(double maxElevation) {
		this.maxElevation = maxElevation;
	}

	public int getLosAzimuth() {
		return losAzimuth;
	}

	public void setLosAzimuth(int losAzimuth) {
		this.losAzimuth = losAzimuth;
	}

	@Override
	public String toString() {
		return "GroundStationPass: {startTime=" + startTime + ", endTime=" + endTime + ", duration=" + duration
				+ ", aosAzimuth=" + aosAzimuth + ", maxElevation=" + maxElevation + ", losAzimuth=" + losAzimuth + "}";
	}

}
