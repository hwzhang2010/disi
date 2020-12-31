package com.hywx.siin.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hywx.siin.vo.SatelliteCoverVO;

public class SatelliteCover {
	private String epoch;
	private double lng;
	private double lat;
	private double alt;
	private double radius;
	private String groundStations;
	
	public SatelliteCover() {
	}

	public SatelliteCover(String epoch, double lng, double lat, double alt, double radius, String groundStations) {
		this.epoch = epoch;
		this.lng = lng;
		this.lat = lat;
		this.alt = alt;
		this.radius = radius;
		this.groundStations = groundStations;
	}
	
	public SatelliteCover(Date date, double lng, double lat, double alt, double radius, String groundStations) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.epoch = format.format(date);
		this.lng = new BigDecimal(lng).setScale(2, RoundingMode.UP).doubleValue();
		this.lat = new BigDecimal(lat).setScale(2, RoundingMode.UP).doubleValue();
		this.alt = new BigDecimal(alt).setScale(2, RoundingMode.UP).doubleValue();
		this.radius = new BigDecimal(radius).setScale(4, RoundingMode.UP).doubleValue();
		this.groundStations = groundStations;
	}
	
	public SatelliteCoverVO getVO() {
		return new SatelliteCoverVO(this.epoch, this.lng, this.lat, this.alt, this.radius, this.groundStations);
	}

	public String getEpoch() {
		return epoch;
	}

	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
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
		return "SatelliteCover: {epoch=" + epoch + ", lng=" + lng + ", lat=" + lat + ", alt=" + alt + ", radius=" + radius
				+ ", groundStations=" + groundStations + "}";
	}
	
	
	
	

}
