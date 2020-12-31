package com.hywx.sirs.bo;

public class StationData {
    private String station;
    private byte[] data;
    
    public StationData() {
    }

	public StationData(String station, byte[] data) {
		this.station = station;
		this.data = data;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
    
}
