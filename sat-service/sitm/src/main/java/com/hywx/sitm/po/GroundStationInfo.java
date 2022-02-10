package com.hywx.sitm.po;

public class GroundStationInfo {
	private String groundStationId;
	private String groundStationName;
	private String groundStationText;
	private double groundStationLng;
	private double groundStationLat;
	private double groundStationAlt;
	private boolean isUsed;
	
	public GroundStationInfo() {
	}

	public String getGroundStationId() {
		return groundStationId;
	}

	public void setGroundStationId(String groundStationId) {
		this.groundStationId = groundStationId;
	}

	public String getGroundStationName() {
		return groundStationName;
	}

	public void setGroundStationName(String groundStationName) {
		this.groundStationName = groundStationName;
	}

	public String getGroundStationText() {
		return groundStationText;
	}

	public void setGroundStationText(String groundStationText) {
		this.groundStationText = groundStationText;
	}

	public double getGroundStationLng() {
		return groundStationLng;
	}

	public void setGroundStationLng(double groundStationLng) {
		this.groundStationLng = groundStationLng;
	}

	public double getGroundStationLat() {
		return groundStationLat;
	}

	public void setGroundStationLat(double groundStationLat) {
		this.groundStationLat = groundStationLat;
	}

	public double getGroundStationAlt() {
		return groundStationAlt;
	}

	public void setGroundStationAlt(double groundStationAlt) {
		this.groundStationAlt = groundStationAlt;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	@Override
	public String toString() {
		return "GroundStationInfo: {groundStationId=" + groundStationId + ", groundStationName=" + groundStationName
				+ ", groundStationText=" + groundStationText + ", groundStationLng=" + groundStationLng
				+ ", groundStationLat=" + groundStationLat + ", groundStationAlt=" + groundStationAlt + ", isUsed="
				+ isUsed + "}";
	}
}
