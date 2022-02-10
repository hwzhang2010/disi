package com.hywx.siin.po;

public class Device {
	private int id;
	private String name;
	private int faultMain;
	
	public Device() {
	}

	public Device(int id, String name, int faultMain) {
		this.id = id;
		this.name = name;
		this.faultMain = faultMain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFaultMain() {
		return faultMain;
	}

	public void setFaultMain(int faultMain) {
		this.faultMain = faultMain;
	}

	@Override
	public String toString() {
		return "Device: {id=" + id + ", name=" + name + ", faultMain=" + faultMain + "}";
	}

	
	
	

}
