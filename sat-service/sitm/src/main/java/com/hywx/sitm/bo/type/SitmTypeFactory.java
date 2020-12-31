package com.hywx.sitm.bo.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 *  简单工厂模式(静态工厂模式)：根据注册的参数，动态决定创建哪种数据类型的字节数据
 * 仿真遥测源码数据类型：
 * 单字节： uc, u; sc, s
 * 双字节：us; ss
 * 4字节：ui; si, i
 * 8字节：ul; sl, l
 * 单精度：f32, f
 * 双精度：f64, d
 * 字符串：str, string
 * 二进制：bin, binary 
 * @author zhang.huawei
 *
 */
@Component
public class SitmTypeFactory {
	

	private static Map<String, AbstractSitmType> sitmTypeMap = new HashMap<>();
	private static void register(String key, AbstractSitmType type) {
		sitmTypeMap.put(key, type);
	}
	
	/**
	 * 注册遥测源码数据类型
	 */
	static {
        register("uc", new SitmByteType());
        register("u", new SitmByteType());
        register("sc", new SitmSByteType());
        register("s", new SitmSByteType());
        register("c", new SitmSByteType());
        register("us", new SitmUShortType());
        register("ss", new SitmShortType());
        register("ui", new SitmUIntType());
        register("si", new SitmIntType());
        register("i", new SitmIntType());
        register("ul", new SitmLongType());
        register("sl", new SitmLongType());
        register("l", new SitmLongType());
        register("f32", new SitmFloatType());
        register("f", new SitmFloatType());
        register("f64", new SitmDoubleType());
        register("d", new SitmDoubleType());
        register("string", new SitmStringType());
        register("str", new SitmStringType());
        register("binary", new SitmBinaryType());
        register("bin", new SitmBinaryType());
    }
	
	public static AbstractSitmType getInstance(String key) {
//		if (sitmTypeMap.containsKey(key))
//		    return sitmTypeMap.get(key);
//		
//		return null;
		
		Assert.notNull(key, "sitmtype can't be null");
		return sitmTypeMap.get(key);
	}

}
