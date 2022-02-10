package com.hywx.siin.po;

public class Fault {
	private int mainId;
	private String mainName;
	private int subId;
	private String subName;
	
	public Fault() {
	}

	public Fault(int mainId, String mainName, int subId, String subName) {
		this.mainId = mainId;
		this.mainName = mainName;
		this.subId = subId;
		this.subName = subName;
	}

	public int getMainId() {
		return mainId;
	}

	public void setMainId(int mainId) {
		this.mainId = mainId;
	}

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	@Override
	public String toString() {
		return "Fault: {mainId=" + mainId + ", mainName=" + mainName + ", subId=" + subId + ", subName=" + subName + "}";
	}
	
	
	
	

}
