package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SatelliteMulti {
	private String satelliteId;
	// 覆盖次数
	private int count;
	// 覆盖总时间：分钟
	private double duration;
	
	public SatelliteMulti() {
	}
	
	public SatelliteMulti(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public SatelliteMulti(String satelliteId, int count, double duration) {
		this.satelliteId = satelliteId;
		this.count = count;
		this.duration = duration;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = new BigDecimal(duration).setScale(2, RoundingMode.UP).doubleValue();
	}
	
	
	

}
