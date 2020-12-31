package com.hywx.sisn.bo.rc;


/**
 * 远程控制命令
 */
public class RemoteCommandBack {

    private String id;
    private String stationId;
    private String equipId;
    private int commandType;
    private String result;
    private String description;


    public RemoteCommandBack(){
    }
    
    public RemoteCommandBack(String id, String stationId, String equipId, int commandType,String result, String description ){
        this.id=id;
        this.stationId=stationId;
        this.equipId=equipId;
        this.commandType=commandType;
        this.result=result;
        this.description=description;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
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
    
    


}
