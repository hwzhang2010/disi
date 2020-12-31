package com.hywx.sisl.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeVO {
	private String start;
	private String end;
	
	public DateTimeVO() {
	}

	public DateTimeVO(String start, String end) {
		this.start = start;
		this.end = end;
	}
	
	public DateTimeVO(Date startDate, Date endDate) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		this.start = format.format(startDate);
		this.end = format.format(endDate);
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "DateTimeVO: {start=" + start + ", end=" + end + "}";
	}
	
	
	

}
