package com.hywx.sisn.bo.rc;


/**
 * 命令信息
 * 命令代号
 * 命令名称
 * 命令类型（1：参数命令:2：过程命令:3：脚本命令）
 * 命令值(命令类型为1是有值，其他为空)
 */
public class Command {

    private String commandId;
    private String commandName;
    private int commandType;
    private String commandValue;

    public Command() {
    }
    
    public Command(String commandId, String commandName, int commandType, String commandValue){
        this.commandId=commandId;
        this.commandName=commandName;
        this.commandType=commandType;
        this.commandValue=commandValue;
    }

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	public String getCommandValue() {
		return commandValue;
	}

	public void setCommandValue(String commandValue) {
		this.commandValue = commandValue;
	}

    


}
