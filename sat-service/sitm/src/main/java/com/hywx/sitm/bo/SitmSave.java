package com.hywx.sitm.bo;

public class SitmSave {
	private SitmType type;
	private byte[] data;
	
	public SitmSave() {
	}

	public SitmSave(SitmType type, byte[] data) {
		this.type = type;
		this.data = data;
	}

	public SitmType getType() {
		return type;
	}

	public void setType(SitmType type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	

}
