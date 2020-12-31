package com.hywx.sitm.bo.gps;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.expression.Operation;
import org.springframework.stereotype.Component;

import com.hywx.sitm.bo.type.SitmBinaryType;
import com.hywx.sitm.bo.type.SitmByteType;
import com.hywx.sitm.bo.type.SitmDoubleType;
import com.hywx.sitm.bo.type.SitmFloatType;
import com.hywx.sitm.bo.type.SitmIntType;
import com.hywx.sitm.bo.type.SitmLongType;
import com.hywx.sitm.bo.type.SitmSByteType;
import com.hywx.sitm.bo.type.SitmShortType;
import com.hywx.sitm.bo.type.SitmStringType;
import com.hywx.sitm.bo.type.SitmUIntType;
import com.hywx.sitm.bo.type.SitmUShortType;

/**
 * 简单工厂模式(静态工厂模式)：根据注册的参数，动态决定创建哪种数据类型的字节数据
 * GPS周： GPS_Week
 * GPS秒：GPS_Second
 * 卫星位置X方向：GPS_PosX
 * 卫星位置Y方向：GPS_PosY
 * 卫星位置Z方向：GPS_PosZ
 * 卫星速度X方向：GPS_SpeedX
 * 卫星位置Y方向：GPS_SpeedY
 * 卫星位置Z方向：GPS_SpeedZ
 * 
 * @author zhang.huawei
 *
 */
public class SitmGpsFactory {
	
	private static Map<String, AbstractSitmGps> sitmGpsMap = new HashMap<>();
    private static void register(String key, AbstractSitmGps gps) {
    	sitmGpsMap.put(key, gps);
    }
    
    /**
	 * 注册GPS参数代号
	 */
	static {
        register("GPS_Week", new SitmGpsWeek());
        register("GPS_Second", new SitmGpsSecond());
        register("GPS_PosX", new SitmGpsSx());
        register("GPS_PosY", new SitmGpsSy());
        register("GPS_PosZ", new SitmGpsSz());
        register("GPS_SpeedX", new SitmGpsVx());
        register("GPS_SpeedY", new SitmGpsVy());
        register("GPS_SpeedZ", new SitmGpsVz());
    }

    public static AbstractSitmGps getInstance(String key) {
    	if (sitmGpsMap.containsKey(key))
            return sitmGpsMap.get(key);
    	
    	return null;
    }
}
