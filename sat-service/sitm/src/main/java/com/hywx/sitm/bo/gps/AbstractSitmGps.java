package com.hywx.sitm.bo.gps;

import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.po.GpsFrame;

/**
 * 仿真遥测参数GPS数据抽象类：
 * GPS周： GPS_Week
 * GPS秒：GPS_Second
 * 卫星位置X方向：GPS_PosX
 * 卫星位置Y方向：GPS_PosY
 * 卫星位置Z方向：GPS_PosZ
 * 卫星速度X方向：GPS_SpeedX
 * 卫星位置Y方向：GPS_SpeedY
 * 卫星位置Z方向：GPS_SpeedZ
 * @author zhang.huawei
 *
 */
public abstract class AbstractSitmGps {
	
	// 根据卫星ID和GPS参数代号得到GPS参数数据
	public abstract SitmParam getGpsParam(String satelliteId, String value, GpsFrame frame);

}
