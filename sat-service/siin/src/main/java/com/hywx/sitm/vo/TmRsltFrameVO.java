package com.hywx.sitm.vo;

public class TmRsltFrameVO {
	
	private int id;
	private String codeName;
	private String name;
	private int bd;
	private String srcType;
	private String paramType;
	private String range;
	private double coefficient;
	private String value;
	
	public TmRsltFrameVO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBd() {
		return bd;
	}

	public void setBd(int bd) {
		this.bd = bd;
	}

	public String getSrcType() {
		return srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TmRsltFrameVO: {id=" + id + ", codeName=" + codeName + ", name=" + name + ", bd=" + bd + ", srcType="
				+ srcType + ", paramType=" + paramType + ", range=" + range + ", coefficient=" + coefficient
				+ ", value=" + value + "}";
	}

	

	
	

}


