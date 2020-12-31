package com.hywx.sitm.global;

/**
 * 全局访问的常量
 * @author zhang.huawei
 *
 */
public class GlobalConstant {
	// 帧头字节长度
	public static final int FRAME_HEADER_LENGTH = 32;
	// 遥测源码有效字节长度
	public static final int FRAME_TM_LENGTH = 1024;
	
	//使用二分法查找，必须先排序, GPS的参数
	public static final String[] GPS_PARAMS = { "GPS_PosX", "GPS_PosY", "GPS_PosZ", "GPS_Second", "GPS_SpeedX", "GPS_SpeedY", "GPS_SpeedZ", "GPS_Week" };
			
    // 外测提供的GPS最小帧长(帧头 + 有效长度)
    public static final int FRAME_GPS_MIN_LENGTH = 88;
    // 遥控最小帧长(帧头 + 有效使用字节)
    public static final int FRAME_TC_MIN_LENGTH = 46;
    // 遥控关联遥测参数ID的索引
    public static final int FRAME_TC_ID_INDEX = 44;
    
    // SID(自动发送的遥测源码SID)
    public static final long SID = 0x01110000;
	
	// DID
	public static final long DID = 0x00100000;
	
	// BID, 遥测源码
	public static long BID_TM = 0x00000090;
	//public static long BID_TM = 0x00009000;
	// BID, GPS
	public static long BID_GPS = 0x000000F1;
    // BID, 测距测速
	public static long BID_TC = 0x00000201;
    // BID, 测角
	public static long BID_INJECTION = 0x0000202;
}
