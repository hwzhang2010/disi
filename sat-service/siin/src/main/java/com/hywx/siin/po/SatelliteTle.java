package com.hywx.siin.po;

public class SatelliteTle {
	private String satelliteId;
	private String tleLine0;
	private String tleLine1;
	private String tleLine2;
	
	public SatelliteTle() {
	}
	
	public SatelliteTle(String tleLine0, String tleLine1, String tleLine2) {
		this.tleLine0 = tleLine0;
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;
	}

	public String[] getTle() {
		String[] tle = { null, null, null };
		tle[0] = this.tleLine0;
		tle[1] = this.tleLine1;
		tle[2] = this.tleLine2;
		
		return tle;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public String getTleLine0() {
		return tleLine0;
	}

	public void setTleLine0(String tleLine0) {
		this.tleLine0 = tleLine0;
	}

	public String getTleLine1() {
		return tleLine1;
	}

	public void setTleLine1(String tleLine1) {
		this.tleLine1 = tleLine1;
	}

	public String getTleLine2() {
		return tleLine2;
	}

	public void setTleLine2(String tleLine2) {
		this.tleLine2 = tleLine2;
	}

	@Override
	public String toString() {
		return "SatelliteTle: {satelliteId=" + satelliteId + ", tleLine0=" + tleLine0 + ", tleLine1=" + tleLine1
				+ ", tleLine2=" + tleLine2 + "}";
	}
	
	

}
