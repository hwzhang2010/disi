package com.hywx.sitm.bo;

import java.time.LocalDateTime;

import com.hywx.sitm.global.GlobalConstant;
import com.hywx.sitm.po.GpsFrame;

public class SitmGpsFromExternal {
	private byte[] header;
	private GpsFrame gpsFrame;
	
	public SitmGpsFromExternal() {
		this.header = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		this.gpsFrame = new GpsFrame(LocalDateTime.now());
	}

	public SitmGpsFromExternal(byte[] header, GpsFrame gpsFrame) {
		this.header = header;
		this.gpsFrame = gpsFrame;
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public GpsFrame getGpsFrame() {
		return gpsFrame;
	}

	public void setGpsFrame(GpsFrame gpsFrame) {
		this.gpsFrame = gpsFrame;
	}
	
	
	

}
