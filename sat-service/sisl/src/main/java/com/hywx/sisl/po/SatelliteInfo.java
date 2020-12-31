package com.hywx.sisl.po;

public class SatelliteInfo {
	private String satelliteId;
	private String satelliteName;
	private String satelliteText;
	private boolean isUsed;
	
	public SatelliteInfo() {
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
		return "Satellite: {satelliteId=" + satelliteId + ", satelliteName=" + satelliteName + ", satelliteText="
				+ satelliteText + ", isUsed=" + isUsed + "}";
	}
	
	

}
