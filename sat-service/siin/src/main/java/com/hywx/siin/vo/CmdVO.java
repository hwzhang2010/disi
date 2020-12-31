package com.hywx.siin.vo;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CmdVO {
	private String sendTime;
	private String sendData;
	
	public CmdVO() {
	}

	public CmdVO(String sendTime, String sendData) {
		this.sendTime = sendTime;
		this.sendData = sendData;
	}
	
	public CmdVO(long sendTime, String data) {
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date(sendTime);
		this.sendTime = format.format(date);
		
		this.sendData = data;
		if (data.length() > 64)
			this.sendData = data.substring(0, 64);
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendData() {
		return sendData;
	}

	public void setSendData(String sendData) {
		this.sendData = sendData;
	}

	@Override
	public String toString() {
		return "CmdVO: {sendTime=" + sendTime + ", sendData=" + sendData + "}";
	}
	
	

}
