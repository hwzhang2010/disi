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
	
    
	// BID, 远控指令
	public static long BID_RC = 0x00000601;
}
