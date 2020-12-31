package com.hywx.sirs.vo;

public class StationVO {
    private String name;
    private String scode;
    private String recvIP;
    private int recvPort;
    private String sendIP;
    private int sendPort;
    
    public StationVO() {
    }

	public StationVO(String name, String scode, String recvIP, int recvPort, String sendIP, int sendPort) {
		this.name = name;
		this.scode = scode;
		this.recvIP = recvIP;
		this.recvPort = recvPort;
		this.sendIP = sendIP;
		this.sendPort = sendPort;
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

	@Override
	public String toString() {
		return "StationVO: {name=" + name + ", scode=" + scode + ", recvIP=" + recvIP + ", recvPort=" + recvPort
				+ ", sendIP=" + sendIP + ", sendPort=" + sendPort + "}" + System.getProperty("line.separator");
	}
    
}
