package com.hywx.siin.po;

public class SatelliteWaveBeam {
	private String epoch;
	private double lng;
	private double lat;
	
	public SatelliteWaveBeam() {
	}

	public SatelliteWaveBeam(String epoch, double lng, double lat) {
		this.epoch = epoch;
		this.lng = lng;
		this.lat = lat;
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

	@Override
	public String toString() {
		return "SatelliteWaveBeam: {epoch=" + epoch + ", lng=" + lng + ", lat=" + lat + "}";
	}
	
	
	

}
