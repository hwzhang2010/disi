package com.hywx.sisl.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.amsacode.predict4java.SatPos;

public class SatelliteCircleVO {
	private String id;
	private String epoch;
	private double lng;
	private double lat;
	private double alt;
	private double radius;
	
	public SatelliteCircleVO() {
	}
	
	public SatelliteCircleVO(String id, Date time, double lng, double lat, double alt, double radius) {
		this.id = id;
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
		this.epoch = format.format(time);
		this.lng = lng / Math.PI * 180;
		while (this.lng > 180)
			this.lng -= 360;
		this.lat = lat / Math.PI * 180;
		this.alt = alt * 1000;
		this.radius = radius * 1000;
	}
	
	public SatelliteCircleVO(String id, SatPos pos) {
		this.id = id;
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
		this.epoch = format.format(pos.getTime());
		this.lng = pos.getLongitude() / Math.PI * 180;
		while (this.lng > 180)
			this.lng -= 360;
		this.lat = pos.getLatitude() / Math.PI * 180;
		this.alt = pos.getAltitude() * 1000;
		this.radius = pos.getRangeCircleRadiusKm() * 1000;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	
	
	
	
	

}
