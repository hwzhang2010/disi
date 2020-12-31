package com.hywx.sirs.vo;

//<<"测站名称"<<"站码"<<"接收IP"<<"接收端口"<<"连接状态"<<"接收计数"<<"发送IP"<<"发送端口"<<"连接状态"<<"发送计数";
public class ExchangeVO {
	private String name;
	private String scode;
	private String recvIP;
    private int recvPort;
    private String recvState;
    private long recvCount;
    private String sendIP;
    private int sendPort;
    private String sendState;
    private long sendCount;
    
    public ExchangeVO() {
    }
    
    public ExchangeVO(StationVO station) {
    	this.name = station.getName();
    	this.scode = station.getScode();
    	this.recvIP = station.getRecvIP();
    	this.recvPort = station.getRecvPort();
    	this.sendIP = station.getSendIP();
    	this.sendPort = station.getSendPort();
    	this.recvState = "not listen";
    	this.recvCount = 0;
    	this.sendState = "not connect";
    	this.sendCount = 0;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getRecvIP() {
		return recvIP;
	}

	public void setRecvIP(String recvIP) {
		this.recvIP = recvIP;
	}

	public int getRecvPort() {
		return recvPort;
	}

	public void setRecvPort(int recvPort) {
		this.recvPort = recvPort;
	}

	public String getRecvState() {
		return recvState;
	}

	public void setRecvState(String recvState) {
		this.recvState = recvState;
	}

	public long getRecvCount() {
		return recvCount;
	}

	public void setRecvCount(long recvCount) {
		this.recvCount = recvCount;
	}

	public String getSendIP() {
		return sendIP;
	}

	public void setSendIP(String sendIP) {
		this.sendIP = sendIP;
	}

	public int getSendPort() {
		return sendPort;
	}

	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}

	public String getSendState() {
		return sendState;
	}

	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	public long getSendCount() {
		return sendCount;
	}

	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}

	@Override
	public String toString() {
		return "ExchangeVO: {name=" + name + ", scode=" + scode + ", recvIP=" + recvIP + ", recvPort=" + recvPort
				+ ", recvState=" + recvState + ", recvCount=" + recvCount + ", sendIP=" + sendIP + ", sendPort="
				+ sendPort + ", sendState=" + sendState + ", sendCount=" + sendCount + "}";
	}
    
    

}
