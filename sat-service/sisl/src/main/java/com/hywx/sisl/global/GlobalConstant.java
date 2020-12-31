package com.hywx.sisl.global;

public class GlobalConstant {
	// 帧头字节长度
	public static final int FRAME_HEADER_LENGTH = 32;
	
	// 字节有效长度, 测距测速: 20
	public static final int FRAME_RANGE_LENGTH = 0x14;
	// 字节有效长度, 测角: 30
	public static final int FRAME_ANGLE_LENGTH = 0x1E;
	// 字节有效长度, 轨道根数: 82
	public static final int FRAME_ORBIT_ELEM_LENGTH = 0x52;
	// 字节有效长度, GPS: 48
	public static final int FRAME_GPS_LENGTH = 0x38;
	
	// DID
	public static final long DID = 0x00100000;
	
	
    // BID, 测距测速
	public static long BID_RANGE = 0x00000076;
	//public static long bidOfRange = 0x00007600;
    // BID, 测角
	public static long BID_ANGLE = 0x0000007E;
	//public static long bidOfAngle = 0x00007E00;
	// BID, 轨道根数
	public static long BID_ORBIT_ELEM = 0x00200004;
	// BID, GPS
	public static long BID_GPS = 0x000000F1;
	
	
	
	
	
	
	
}
