package com.hywx.sitm.bo.param;

/**
 * 仿真遥测参数抽象类：目前对应4种类型 
 * 1.常数
 * 2.递增
 * 3.随机
 * 4.正弦
 * 5.遥控(特殊处理)
 * @author zhang.huawei
 *
 */
public abstract class AbstractSitmParam {
	 // 得到仿真参数值
     public abstract String valueOfParam();
		 
     // 得到byte/sbyte类型的仿真参数字节数组
	 public abstract byte[] bytesOfByte(String value, double coefficient);
	 
	 // 得到sbyte类型的仿真参数字节数据
	 public abstract byte[] bytesOfSByte(String value, double coefficient);
	 
	 // 得到ushort类型的仿真参数字节数组
     public abstract byte[] bytesOfUShort(String value, double coefficient);
	 
     // 得到short类型的仿真参数字节数组
  	 public abstract byte[] bytesOfShort(String value, double coefficient);
  	 
     // 得到uint类型的仿真参数字节数组
  	 public abstract byte[] bytesOfUInt(String value, double coefficient);
  	 
     // 得到int类型的仿真参数字节数组
  	 public abstract byte[] bytesOfInt(String value, double coefficient);
  	 
     // 得到long类型的仿真参数字节数组
  	 public abstract byte[] bytesOfLong(String value, double coefficient);
  	 
     // 得到float类型的仿真参数字节数组
  	 public abstract byte[] bytesOfFloat(String value, double coefficient);
  	 
     // 得到double类型的仿真参数字节数组
  	 public abstract byte[] bytesOfDouble(String value, double coefficient);
  	 
}
