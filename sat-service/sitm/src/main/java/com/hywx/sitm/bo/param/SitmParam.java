package com.hywx.sitm.bo.param;

public class SitmParam {
	private byte[] buffer;
	private String value;
	
	public SitmParam() {
	}

	public SitmParam(byte[] buffer, String value) {
		this.buffer = buffer;
		this.value = value;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public String getValue() {
		return value;
	}

	

}
