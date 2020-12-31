package com.hywx.sirs.global;

public class GlobalConstant {

	// DID
	public static final long DID = 0x00100000;
	
	// BID, 遥测源码
	public static long BID_TM = 0x00000090;
	//public static long BID_TM = 0x00009000;
    // BID, 测距测速
	public static long BID_RANGE = 0x00000076;
	//public static long bidOfRange = 0x00007600;
    // BID, 测角
	public static long BID_ANGLE = 0x0000007E;
	//public static long bidOfAngle = 0x00007E00;
	// BID, 轨道根数
	public static long BID_ORBIT_ELEM = 0x00200004;
	// BID, 远控指令
	public static long BID_RC = 0x00000601;
	
}
