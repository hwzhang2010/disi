package com.hywx.sisn.bo.rc;


import java.util.List;

/**
 * 远程控制命令
 */
public class RemoteCommand {

    private String id;
    private String stationId;
    private String equipId;
    private int commandType;
    private List<Command> commandList;


    public RemoteCommand(){
    }
    
    public RemoteCommand(String id, String stationId, String equipId,int commandType, List<Command> commandList){
        this.id=id;
        this.stationId=stationId;
        this.equipId=equipId;
        this.commandType=commandType;
        this.commandList=commandList;
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

	public List<Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(List<Command> commandList) {
		this.commandList = commandList;
	}
    
    


}
