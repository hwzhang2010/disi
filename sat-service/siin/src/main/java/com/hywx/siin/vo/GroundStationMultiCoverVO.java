package com.hywx.siin.vo;

import com.hywx.siin.po.SatelliteMulti;

public class GroundStationMultiCoverVO {
	private String groundStationText;
	private Integer count;
	private Double duration;
	
	public GroundStationMultiCoverVO() {
	}

	public GroundStationMultiCoverVO(String groundStationText, Integer count, Double duration) {
		this.groundStationText = groundStationText;
		this.count = count;
		this.duration = duration;
	}
	
	public GroundStationMultiCoverVO(String groundStationText, SatelliteMulti vo) {
		this.groundStationText = groundStationText;
		this.count = vo.getCount();
		this.duration = vo.getDuration();
	}

	public String getGroundStationText() {
		return groundStationText;
	}

	public void setGroundStationText(String groundStationText) {
		this.groundStationText = groundStationText;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "GroundStationMultiCoverVO: {groundStationText=" + groundStationText + ", count=" + count + ", duration="
				+ duration + "}";
	}

	
	
	

}
