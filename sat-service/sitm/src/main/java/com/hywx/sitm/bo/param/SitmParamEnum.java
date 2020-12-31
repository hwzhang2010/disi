package com.hywx.sitm.bo.param;

public enum SitmParamEnum {
	//常数
	Constant("constant", new SitmConstantParam()),
	//递增
	Increment("increment", new SitmIncrementParam()),
	//随机
	Random("random", new SitmRandomParam()),
	//正弦
	Sin("sin", new SitmSinParam()),
	//遥控
	Cmd("cmd", new SitmCmdParam());
	
	
    private String name;
    private AbstractSitmParam param;
     
    SitmParamEnum(String name){
        this.name = name;
    }
    
    SitmParamEnum(String name, AbstractSitmParam param) {
    	this.name = name;
    	this.param = param;
    }
     
    public String getName(){
        return this.name;
    }
    
    public AbstractSitmParam getSitmParam() {
    	return this.param;
    }
    
    public static SitmParamEnum match(String name) {
    	SitmParamEnum[] values = SitmParamEnum.values();
    	for (SitmParamEnum value : values) {
    		if (value.name.equals(name))
    			return value;
    	}
    	
    	return null;
    }

}
