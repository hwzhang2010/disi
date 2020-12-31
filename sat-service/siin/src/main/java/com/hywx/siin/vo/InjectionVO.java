package com.hywx.siin.vo;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InjectionVO {
	private String sendTime;
	private String dataLength;
	
	public InjectionVO() {
	}

	public InjectionVO(String sendTime, String dataLength) {
		this.sendTime = sendTime;
		this.dataLength = dataLength;
	}
	
	public InjectionVO(long sendTime, String dataLength) {
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date(sendTime);
		this.sendTime = format.format(date);
		
		this.dataLength = dataLength;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	@Override
	public String toString() {
		return "InjectionVO: {sendTime=" + sendTime + ", dataLength=" + dataLength + "}";
	}

	
	
	

}
