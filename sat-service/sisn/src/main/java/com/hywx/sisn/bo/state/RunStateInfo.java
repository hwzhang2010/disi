package com.hywx.sisn.bo.state;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 运行状态信息
 * id  记录id
 * receiveTime	存储时间  
 * stationId	信关站标识  ****必需
 * subSystem  分系统  ****必需
 * location	位置信息
 * equipId	设备编号  ****必需
 * dataType	数据类别
 * dataContent	数据内容
 * dataProperty	数据属性
 * dataPara	数据参数
 * restraint	约束条件
 * warnInfo	警告信息
 * healthLevel	健康级别
 * disposalInfo	处置信息
 */

public class RunStateInfo implements Serializable {

    private static final long serialVersionUID = 6323573554785370660L;

    private int id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    private String stationId;
    private String location;
    private int subSystem;
    private String equipId;
    private int dataType;
    private String dataContent;
    private String dataProperty;
    private String dataPara;
    private String restraint;
    private String warnInfo;
    private int healthLevel;
    private String disposalInfo;

    public RunStateInfo() {
    	
    }
    
    public RunStateInfo(Date receiveTime,String stationId,String location,int subSystem,String equipId,int dataType,String dataContent,String dataProperty,String dataPara,String restraint,String warnInfo, int healthLevel, String disposalInfo) {
        this.receiveTime=receiveTime;
        this.stationId=stationId;
        this.location=location;
        this.subSystem=subSystem;
        this.equipId=equipId;
        this.dataType=dataType;
        this.dataContent=dataContent;
        this.dataProperty=dataProperty;
        this.dataPara=dataPara;
        this.restraint=restraint;
        this.warnInfo=warnInfo;
        this.healthLevel=healthLevel;
        this.disposalInfo=disposalInfo;

    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(int subSystem) {
		this.subSystem = subSystem;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public String getDataProperty() {
		return dataProperty;
	}

	public void setDataProperty(String dataProperty) {
		this.dataProperty = dataProperty;
	}

	public String getDataPara() {
		return dataPara;
	}

	public void setDataPara(String dataPara) {
		this.dataPara = dataPara;
	}

	public String getRestraint() {
		return restraint;
	}

	public void setRestraint(String restraint) {
		this.restraint = restraint;
	}

	public String getWarnInfo() {
		return warnInfo;
	}

	public void setWarnInfo(String warnInfo) {
		this.warnInfo = warnInfo;
	}

	public int getHealthLevel() {
		return healthLevel;
	}

	public void setHealthLevel(int healthLevel) {
		this.healthLevel = healthLevel;
	}

	public String getDisposalInfo() {
		return disposalInfo;
	}

	public void setDisposalInfo(String disposalInfo) {
		this.disposalInfo = disposalInfo;
	}
    
    
    

}
