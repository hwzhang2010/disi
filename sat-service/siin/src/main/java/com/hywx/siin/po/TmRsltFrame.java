package com.hywx.siin.po;

public class TmRsltFrame {
	private int id;
    private String codeName;
    private String name;
    private String srcType;
    private String rsltType;
    private String bd;
    private String bitRange;
    private int byteOrder;
    private String coefficient;
    private String algorithm;
    private String range;
    private String preCondition;
    private String validFrameCnt;
    private int frameId;
    private int subsystemId;
    private int objId;
    
    public TmRsltFrame() {
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

	public String getSrcType() {
		return srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public String getRsltType() {
		return rsltType;
	}

	public void setRsltType(String rsltType) {
		this.rsltType = rsltType;
	}

	public String getBd() {
		return bd;
	}

	public void setBd(String bd) {
		this.bd = bd;
	}

	public String getBitRange() {
		return bitRange;
	}

	public void setBitRange(String bitRange) {
		this.bitRange = bitRange;
	}

	public int getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(int byteOrder) {
		this.byteOrder = byteOrder;
	}

	public String getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	public String getValidFrameCnt() {
		return validFrameCnt;
	}

	public void setValidFrameCnt(String validFrameCnt) {
		this.validFrameCnt = validFrameCnt;
	}

	public int getFrameId() {
		return frameId;
	}

	public void setFrameId(int frameId) {
		this.frameId = frameId;
	}

	public int getSubsystemId() {
		return subsystemId;
	}

	public void setSubsystemId(int subsystemId) {
		this.subsystemId = subsystemId;
	}

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	@Override
	public String toString() {
		return "TmRsltFrame: {id=" + id + ", codeName=" + codeName + ", name=" + name + ", srcType=" + srcType
				+ ", rsltType=" + rsltType + ", bd=" + bd + ", bitRange=" + bitRange + ", byteOrder=" + byteOrder
				+ ", coefficient=" + coefficient + ", algorithm=" + algorithm + ", range=" + range + ", preCondition="
				+ preCondition + ", validFrameCnt=" + validFrameCnt + ", frameId=" + frameId + ", subsystemId="
				+ subsystemId + ", objId=" + objId + "}";
	}
	
	
    
    
}
