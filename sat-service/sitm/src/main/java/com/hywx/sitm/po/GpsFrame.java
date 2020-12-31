package com.hywx.sitm.po;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GpsFrame {
	private String time;
	private double sx;
	private double sy;
	private double sz;
	private double vx;
	private double vy;
	private double vz;
	
	public GpsFrame() {
	}
	
	public GpsFrame(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
		this.time = formatter.format(localDateTime);
	}

	public GpsFrame(String time, double sx, double sy, double sz, double vx, double vy, double vz) {
		this.time = time;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getSx() {
		return sx;
	}

	public void setSx(double sx) {
		this.sx = sx;
	}

	public double getSy() {
		return sy;
	}

	public void setSy(double sy) {
		this.sy = sy;
	}

	public double getSz() {
		return sz;
	}

	public void setSz(double sz) {
		this.sz = sz;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getVz() {
		return vz;
	}

	public void setVz(double vz) {
		this.vz = vz;
	}

	@Override
	public String toString() {
		return "GpsFrame: {time=" + time + ", sx=" + sx + ", sy=" + sy + ", sz=" + sz + ", vx=" + vx + ", vy=" + vy
				+ ", vz=" + vz + "}";
	}
	
	


}
