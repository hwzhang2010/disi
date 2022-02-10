package com.hywx.sisn.po;

public class StationNetReply {
	private int id;
	private String reply;
	private String reason;
	
	public StationNetReply() {
	}

	public StationNetReply(int id, String reply, String reason) {
		this.id = id;
		this.reply = reply;
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	

}
