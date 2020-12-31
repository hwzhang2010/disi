package com.hywx.sitm.vo;

import java.util.List;

public class SitmSatelliteRunningVO {
	private int count;
    private List<String> runningList;
    
    public SitmSatelliteRunningVO() {
    }

	public SitmSatelliteRunningVO(int count, List<String> runningList) {
		this.count = count;
		this.runningList = runningList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<String> getRunningList() {
		return runningList;
	}

	public void setRunningList(List<String> runningList) {
		this.runningList = runningList;
	}
    
    
    
}
