package com.hywx.sitm.bo.type;

import com.hywx.sitm.bo.param.SitmParam;

/**
 * 仿真遥测源码数据类型抽象类：
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
public abstract class AbstractSitmType {
	
	// 根据仿真遥测源码数据类型得到仿真遥测参数
	public abstract SitmParam getSitmParam(String value, String paramType, double coefficient); 

}
