package com.hywx.siin.global;

public class GlobalConstant {
	// 批量插入的数目
	public static final int BATCH_NUM = 1024;
	
	// 地球半径，单位:km
	public static final double EARTH_RADIUS_KM = 6.378137E3;
	
	// SID(自动发送的遥测源码SID)
    public static final long SID = 0x01110000;
    
    // 遥测参数表名称前缀(表名: 前缀 + 卫星ID)
 	public static final String TM_TABLENAME_PREFIX = "RTFRAMEPARAMETER_";

}
