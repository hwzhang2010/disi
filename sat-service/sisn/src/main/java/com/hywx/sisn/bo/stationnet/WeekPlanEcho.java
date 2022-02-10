package com.hywx.sisn.bo.stationnet;

/**
 * 周计划的反馈电文 对应 SendToStationPlan
 */
public class WeekPlanEcho {
	private String trackId;
    private String station;
    private String result;
    private String description;
    private int planType;

    public WeekPlanEcho(){
    }
    public WeekPlanEcho(String trackId, String station, String result, String description,int planType){
        this.trackId=trackId;
        this.station=station;
        this.result=result;
        this.description=description;
        this.planType=planType;
    }
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPlanType() {
		return planType;
	}
	public void setPlanType(int planType) {
		this.planType = planType;
	}
	@Override
	public String toString() {
		return "WeekPlanEcho: {trackId=" + trackId + ", station=" + station + ", result=" + result + ", description="
				+ description + ", planType=" + planType + "}";
	}
    
    

}
