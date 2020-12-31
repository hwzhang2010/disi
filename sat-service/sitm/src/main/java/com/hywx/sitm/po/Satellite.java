package com.hywx.sitm.po;

public class Satellite {
	private String satelliteId;
	private String satelliteName;
	private String satelliteText;
	private boolean isUsed;
	
	public Satellite() {
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public String getSatelliteName() {
		return satelliteName;
	}

	public void setSatelliteName(String satelliteName) {
		this.satelliteName = satelliteName;
	}

	public String getSatelliteText() {
		return satelliteText;
	}

	public void setSatelliteText(String satelliteText) {
		this.satelliteText = satelliteText;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	@Override
	public String toString() {
		return "SatelliteInfo: {satelliteId=" + satelliteId + ", satelliteName=" + satelliteName + ", satelliteText="
				+ satelliteText + ", isUsed=" + isUsed + "}";
	}
	
	

}
