package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteRegion {
	private String epoch;
	private double ratio;
	
	public SatelliteRegion() {
	}
	
	public SatelliteRegion(String epoch, double ratio) {
		this.epoch = epoch;
		this.ratio = ratio;
	}
	
	public SatelliteRegion(Date date, double ratio) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		this.ratio = new BigDecimal(ratio).setScale(2, RoundingMode.UP).doubleValue();
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = new BigDecimal(ratio).setScale(2, RoundingMode.UP).doubleValue();
	}
	
	

}
