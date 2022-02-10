package com.hywx.sisn.global;

/**
 * 全局访问的常量
 * @author zhang.huawei
 *
 */
public class GlobalConstant {
	// 帧头字节长度
	public static final int FRAME_HEADER_LENGTH = 32;
	
	// 远控指令有效字节长度
	public static final int FRAME_RC_LENGTH = 1024;
		
	// 站网计划有效字节长度
	public static final int FRAME_STATIONNET_PLAN_LENGTH = 1024;
	
	// 故障有效字节长度
	public static final int FRAME_FAULT_LENGTH = 7;
	
	// DID
	public static final long DID = 0x00100000;
	
	
	// BID, 远控指令
	public static final long BID_RC = 0x00000601;
	
	// BID, 站网计划
	public static final long BID_STATIONNET_PLAN = 0x00000605;
	
	// BID, 站网状态
	public static final long BID_STATIONNET_STATE = 0x00000606;
	
	//BID, 故障数据
	public static final long BID_FAULT = 0x00000030;
	
}
