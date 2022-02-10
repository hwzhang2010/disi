package com.hywx.sitm.vo;

import java.util.List;

public class SitmGroundStationFaultVO {
	private int count;
    private List<String> faultList;
    
    public SitmGroundStationFaultVO() {
    }

	public SitmGroundStationFaultVO(int count, List<String> faultList) {
		this.count = count;
		this.faultList = faultList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<String> getFaultList() {
		return faultList;
	}

	public void setFaultList(List<String> faultList) {
		this.faultList = faultList;
	}
    
    

}
