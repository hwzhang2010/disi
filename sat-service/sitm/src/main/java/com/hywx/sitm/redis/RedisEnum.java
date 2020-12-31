package com.hywx.sitm.redis;

/*
 * Redis Key 命名规则：以:分割，使用:可用于rdm（Redis Desktop Manager）分组查看，在rdm（Redis Desktop Manager）中以文件夹的形式分组查看
 * 1. 分系统前缀标识(siin);
 * 2. 接口分系统(subsystem);
 * 3. 接口分系统单元项(item);
 * 4. 接口分系统单元项ID(id);
 */
public enum RedisEnum {
	
	/**
     * REDIS KEY - 组成规则
     */
    STTN_REDISKEY(RedisFind.KEY_PREFIX, "subsystem", "item", "id");
	
	//分系统前缀标识
	private String prefix;
	//接口分系统
	private String subsystem;
	//接口分系统单元项
	private String item;
	//接口分系统单元项ID
    private String id;
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	RedisEnum(String prefix, String subsystem, String item, String id) {
	    this.prefix = prefix;
	    this.subsystem = subsystem;
	    this.item = item;
	    this.id = id;
	};

}
