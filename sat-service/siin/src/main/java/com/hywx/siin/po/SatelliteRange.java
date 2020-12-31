package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteRange {
	private String epoch;
	private double range;
	private double rangeRate;
	
	public SatelliteRange() {
	}

	public SatelliteRange(String epoch, double range, double rangeRate) {
		this.epoch = epoch;
		this.range = range;
		this.rangeRate = rangeRate;
	}
	
	public SatelliteRange(Date date, double range, double rangeRate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		this.range = new BigDecimal(range).setScale(6, RoundingMode.UP).doubleValue();
		this.rangeRate = new BigDecimal(rangeRate).setScale(6, RoundingMode.UP).doubleValue();
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getRangeRate() {
		return rangeRate;
	}

	public void setRangeRate(double rangeRate) {
		this.rangeRate = rangeRate;
	}

	@Override
	public String toString() {
		return "SatelliteRange: {epoch=" + epoch + ", range=" + range + ", rangeRate=" + rangeRate + "}";
	}
	
	

}
