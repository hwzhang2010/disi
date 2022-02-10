package com.hywx.sisn.bo.stationnet;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 发送到信关站的工作计划
 * antennaNumber:天线编号
 * satelliteId：跟踪卫星代号
 * azimuth:方位角
 * pitchAngle：俯仰角
 * startTime：开始跟踪时间
 * endTime：结束跟踪时间
 * passXStationTime：跟踪时长
 * equipOrderList：指令集合
 */
public class SendToStationPlan implements Serializable {
	
	private static final long serialVersionUID = 6323573554785370660L;

    private int planType;
    private String trackId;
//    private int missionId;
    private String stationId;
    private String antennaNumber;
    private String satelliteId;
//    private double azimuth;
//    private double pitchAngle;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
//  private int passXStationTime;
//  private List<EquipOrder> equipOrderList;
//  private String equipOrderString;
//  private OrbitElement orbitElement;

   
    public SendToStationPlan(){
    }
    
    public SendToStationPlan(int planType, String trackId, String stationId, String antennaNumber, String satelliteId, Date startTime, Date endTime){
        this.planType=planType;
        this.trackId=trackId;
//      this.missionId=missionId;
        this.stationId=stationId;
        this.antennaNumber=antennaNumber;
        this.satelliteId=satelliteId;
//      this.azimuth=azimuth;
//      this.pitchAngle=pitchAngle;
        this.startTime=startTime;
        this.endTime=endTime;
//      this.passXStationTime=passXStationTime;
    }

	public int getPlanType() {
		return planType;
	}

	public void setPlanType(int planType) {
		this.planType = planType;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getAntennaNumber() {
		return antennaNumber;
	}

	public void setAntennaNumber(String antennaNumber) {
		this.antennaNumber = antennaNumber;
	}

	public String getSatelliteId() {
		return satelliteId;
	}

	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    


}
