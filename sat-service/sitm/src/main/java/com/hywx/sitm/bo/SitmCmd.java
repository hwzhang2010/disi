package com.hywx.sitm.bo;

import com.hywx.sitm.global.GlobalConstant;

public class SitmCmd {
	private int id;
	private String value;
	
	public SitmCmd() {
	}

	public SitmCmd(int id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public void setCmd(int id, String evenValue, String oddValue) {
		this.id = id;
		
		//奇数double, 偶数int
		if (id % 2 == 0) {  //even
			this.value = evenValue;
		} else {  //odd
			this.value = oddValue;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SitmCmd: {id=" + id + ", value=" + value + "}";
	}
	
	
	
	
	

}
