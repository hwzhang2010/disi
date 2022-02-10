package com.hywx.sitm.vo;

import java.util.List;

public class SitmSatelliteFaultVO {
	private int count;
    private List<String> faultList;
    
    public SitmSatelliteFaultVO() {
    }

	public SitmSatelliteFaultVO(int count, List<String> faultList) {
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
