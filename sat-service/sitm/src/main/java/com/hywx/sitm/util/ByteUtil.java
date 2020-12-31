package com.hywx.sitm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 字节处理工具类
 * @author zhang.huawei
 *
 */
public class ByteUtil {
	//private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
    private static final char[] hexCode = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    
	private static final Map<Character, Byte> hexMap = new HashMap<>();
    static {
        for (int i = 0; i < hexCode.length; i++) {
            char c = hexCode[i];
            hexMap.put(c, (byte) i);
        }
    }
	
    /**
     * 获取字节的指定位的值，算法的思想是:先对这个字节从high位至第7位处理为0,然后对这个处理后的字节逻辑右移low位.
     * @param data 字节
     * @param low 起始低位
     * @param high 截止高位
     * @return 起始低位和截止高位之间的位值
     */
    public static byte getBits(byte data, int low, int high) {
    	byte bits = data;  //初始化为整个字节
        if (low < 0 || high > 7 || low > high)
        {
            return 0;
        }

        for (int i = high + 1; i < 8; i++)
        {
            switch (i)
            {
            case 0:  //将字节的第0位置0
                bits &= 0xFE;
                break;
            case 1:  //将字节的第1位置0
                bits &= 0xFD;
                break;
            case 2:  //将字节的第2位置0
                bits &= 0xFB;
                break;
            case 3:  //将字节的第3位置0
                bits &= 0xF7;
                break;
            case 4:  //将字节的第4位置0
                bits &= 0xEF;
                break;
            case 5:  //将字节的第5位置0
                bits &= 0xDF;
                break;
            case 6:  //将字节的第6位置0
                bits &= 0xBF;
                break;
            case 7:  //将字节的第7位置0
                bits &= 0x7F;
                break;
            default:
                break;
            }
        }

        bits >>= low;  //将字节右移low位
        return bits;
    }
    
    /**
     * 将一个byte字节的某位置为1或者0
     * @param data byte字节
     * @param k 位索引，从右开始为第0位
     * @param notZero true表示置为1，false表示置为0
     */
    public static byte getBit(byte data, int k, boolean notZero) {
    	byte ret = data;  //初始化为原始字节
    	
    	//置为1
    	if (notZero) {
    		ret = (byte)(data | (0x1 << k));
    	} else {
    		// ~(0x1 << k) = (0x1 << 9) - 1 - (0x1 << k)
    		ret = (byte)(data & (~(0x1 << k)));
    	}
    	
    	return ret;
    }
    

	/** 
	 * 字节数组转16进制 
	 * @param bytes 需要转换的byte数组 
	 * @return  转换后的Hex字符串 
	 */ 
    public static String toHex(byte[] data) {
    	StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(hexCode[(b >> 4) & 0xF]);
            sb.append(hexCode[(b & 0xF)]);
            sb.append(' ');
        }
        return sb.toString();
    }
   
    /**
     * 字节数组转16进制 
     * @param data 需要转换的byte数组 
     * @param offset 偏移位置
     * @param count 转换的字节个数
     * @return 转换后的Hex字符串
     */
    public static String toHex(byte[] data, int offset, int count) {
    	StringBuilder sb = new StringBuilder();
        for (int i = offset; i < offset + count; i++) {
        	byte b = data[i];
            sb.append(hexCode[(b >> 4) & 0xF]);
            sb.append(hexCode[(b & 0xF)]);
        }
        return sb.toString();
    }
    
    /**
     * int类型转16进制
     * @param n 需要转换的int类型
     * @return 转换后的Hex字符串
     */
    public static String toHex(int n) {
//    	StringBuilder sb = new StringBuilder(8);
//        while(n != 0) {
//            sb = sb.append(hexCode[n % 16]);
//            n = n / 16;            
//        }
//        return sb.reverse().toString();
    	
    	return String.format("%04x", n).toUpperCase();
    }
    
    public static byte[] toByteArray(String hexString) {
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            char high = hexString.charAt(i * 2);
            char low = hexString.charAt(i * 2 + 1);
            
            result[i] = (byte) ((hexMap.get(high) << 4) + hexMap.get(low));
        }
        
        return result;
    }
    
    
    /**
     * byte数组转byte类型
     * @param data byte数组
     * @param offset 偏移位置
     * @return 转换后的ushort类型
     */
    public static short toByte(byte[] data, int offset) {
    	if (data.length < 1 || data.length < offset + 1)
            return 0;
        
    	if (data[offset] < 0)
            return (short) (256 + data[offset]);
    	
        return data[offset];
    }
    
    /**
     * byte数组转short类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的short类型
     */
    public static short toShort(byte[] data, int offset, boolean isNet) {
    	if (data.length < 2 || data.length < offset + 2)
            return 0;
        
    	if (isNet)
            return (short) (0xff00 & data[offset] << 8 | (0xff & data[offset + 1]));
    	else
    		return (short) (0xff00 & data[offset + 1] << 8 | (0xff & data[offset]));
    }
    
    /**
     * byte数组转short类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的int类型
     */
    public static int toUShort(byte[] data, int offset, boolean isNet) {
    	if (data.length < 2 || data.length < offset + 2)
            return 0;
        
    	if (isNet)
            return (0xff00 & data[offset] << 8 | (0xff & data[offset + 1]));
    	else
    		return (0xff00 & data[offset + 1] << 8 | (0xff & data[offset]));
    }
    
    
    /**
     * byte数组转int类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的int类型
     */
    public static int toInt(byte[] data, int offset, boolean isNet) {
    	if (data.length < 4 || data.length < offset + 4)
            return 0;
        
    	if (isNet)
            return 	(0xff000000 & (data[offset] << 24))     | 
				    (0x00ff0000 & (data[offset + 1] << 16)) | 
				    (0x0000ff00 & (data[offset + 2] << 8))  | 
				    (0x000000ff &  data[offset + 3]);
    	else
    		return 	(0xff000000 & (data[offset + 3] << 24))     | 
				    (0x00ff0000 & (data[offset + 2] << 16)) | 
				    (0x0000ff00 & (data[offset + 1] << 8))  | 
				    (0x000000ff &  data[offset]);
    }
    
    
    /**
     * byte数组转int类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的int类型
     */
    public static long toUInt(byte[] data, int offset, boolean isNet) {
    	if (data.length < 4 || data.length < offset + 4)
            return 0;
        
    	if (isNet)
            return 	(0xff000000 & (data[offset] << 24))     | 
				    (0x00ff0000 & (data[offset + 1] << 16)) | 
				    (0x0000ff00 & (data[offset + 2] << 8))  | 
				    (0x000000ff &  data[offset + 3]);
    	else
    		return 	(0xff000000 & (data[offset + 3] << 24))     | 
				    (0x00ff0000 & (data[offset + 2] << 16)) | 
				    (0x0000ff00 & (data[offset + 1] << 8))  | 
				    (0x000000ff &  data[offset]);
    }
    
    /**
     * byte数组转long类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的long类型
     */
    public static long toLong(byte[] data, int offset, boolean isNet) {
    	if (data.length < 8 || data.length < offset + 8)
            return 0;
    	
    	if (isNet)
	    	return 	(0xff00000000000000L & ((long)data[offset] << 56))      | 
					(0x00ff000000000000L & ((long)data[offset + 1] << 48))  | 
					(0x0000ff0000000000L & ((long)data[offset + 2] << 40))  | 
					(0x000000ff00000000L & ((long)data[offset + 3] << 32))  |
					(0x00000000ff000000L & ((long)data[offset + 4] << 24))  | 
					(0x0000000000ff0000L & ((long)data[offset + 5] << 16))  | 
					(0x000000000000ff00L & ((long)data[offset + 6] << 8))   | 
					(0x00000000000000ffL &  (long)data[offset + 7]);
    	else
    		return 	(0xff00000000000000L & ((long)data[offset + 7] << 56))      | 
    				(0x00ff000000000000L & ((long)data[offset + 6] << 48))  | 
    				(0x0000ff0000000000L & ((long)data[offset + 5] << 40))  | 
    				(0x000000ff00000000L & ((long)data[offset + 4] << 32))  |
    				(0x00000000ff000000L & ((long)data[offset + 3] << 24))  | 
    				(0x0000000000ff0000L & ((long)data[offset + 2] << 16))  | 
    				(0x000000000000ff00L & ((long)data[offset + 1] << 8))   | 
    				(0x00000000000000ffL &  (long)data[offset]);
    }
    
    /**
     * byte数组转float类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的float类型
     */
    public static float toFloat(byte[] data, int offset, boolean isNet) {
    	if (data.length < 4 || data.length < offset + 4)
            return 0;
    	
    	return Float.intBitsToFloat(toInt(data, offset, isNet));
    }
    
    /**
     * byte数组转double类型
     * @param data byte数组
     * @param offset 偏移位置
     * @param isNet 是否网络字节序
     * @return 转换后的double类型
     */
    public static double toDouble(byte[] data, int offset, boolean isNet) {
    	if (data.length < 8 || data.length < offset + 8)
            return 0;
    	
    	return Double.longBitsToDouble(toLong(data, offset, isNet));
    }
    
    
    /**
     * short类型转byte数组
     * @param value short类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromShort2Bytes(short value, boolean isNet) {
    	byte[] data = new byte[2];
    	if (isNet) {
    		data[1] = (byte) (value & 0xff);  
    		data[0] = (byte) (value >> 8 & 0xff); 
    	} else {
    		data[0] = (byte) (value & 0xff);  
    		data[1] = (byte) (value >> 8 & 0xff);  
    	}
    	
    	return data;
    }
    

    
    /**
     * ushort类型转byte数组
     * @param value ushort类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromUShort2Bytes(int value, boolean isNet) {
    	byte[] data = new byte[2];
    	if (isNet) {
    		data[1] = (byte) (value & 0xff);  
    		data[0] = (byte) (value >> 8 & 0xff); 
    	} else {
    		data[0] = (byte) (value & 0xff);  
    		data[1] = (byte) (value >> 8 & 0xff); 
    	}
    	
    	return data;
    }
    
    /**
     * int类型转byte数组
     * @param value int类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromInt2Bytes(int value, boolean isNet) {
    	byte[] data = new byte[4];
    	if (isNet) {
    		data[3] = (byte) (value & 0xff);  
    		data[2] = (byte) (value >> 8 & 0xff);  
    		data[1] = (byte) (value >> 16 & 0xff);  
    		data[0] = (byte) (value >> 24 & 0xff); 
    	} else {
    		data[0] = (byte) (value & 0xff);  
    		data[1] = (byte) (value >> 8 & 0xff);  
    		data[2] = (byte) (value >> 16 & 0xff);  
    		data[3] = (byte) (value >> 24 & 0xff); 
    	}
    	
    	return data;
    }
    

    
    /**
     * uint类型转byte数组
     * @param value uint类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromUInt2Bytes(long value, boolean isNet) {
    	byte[] data = new byte[4];
    	if (isNet) {
    		data[3] = (byte) (value & 0xff);  
    		data[2] = (byte) (value >> 8 & 0xff);  
    		data[1] = (byte) (value >> 16 & 0xff);  
    		data[0] = (byte) (value >> 24 & 0xff); 
    	} else {
    		data[0] = (byte) (value & 0xff);  
    		data[1] = (byte) (value >> 8 & 0xff);  
    		data[2] = (byte) (value >> 16 & 0xff);  
    		data[3] = (byte) (value >> 24 & 0xff); 
    	}
    	
    	return data;
    }
    

    
    /**
     * long类型转byte数组
     * @param value long类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromLong2Bytes(long value, boolean isNet) {
    	byte[] data = new byte[8];
    	if (isNet) {
    		data[7] = (byte) (value & 0xff);  
    		data[6] = (byte) (value >> 8 & 0xff);  
    		data[5] = (byte) (value >> 16 & 0xff);  
    		data[4] = (byte) (value >> 24 & 0xff); 
    		data[3] = (byte) (value >> 32 & 0xff);
    		data[2] = (byte) (value >> 40 & 0xff);
    		data[1] = (byte) (value >> 48 & 0xff);
    		data[0] = (byte) (value >> 56 & 0xff);
    	} else {
    		data[0] = (byte) (value & 0xff);  
    		data[1] = (byte) (value >> 8 & 0xff);  
    		data[2] = (byte) (value >> 16 & 0xff);  
    		data[3] = (byte) (value >> 24 & 0xff);
    		data[4] = (byte) (value >> 32 & 0xff);
    		data[5] = (byte) (value >> 40 & 0xff);
    		data[6] = (byte) (value >> 48 & 0xff);
    		data[7] = (byte) (value >> 56 & 0xff);
    	}
    	
    	return data;
    }
    
    /**
     * float类型转byte数组
     * @param value float类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromFloat2Bytes(float value, boolean isNet) {
    	
    	return fromInt2Bytes(Float.floatToIntBits(value), isNet);
    }
    
    /**
     * double类型转byte数组
     * @param value double类型值
     * @param isNet 是否网络字节序
     * @return
     */
    public static byte[] fromDouble2Bytes(double value, boolean isNet) {
    	
    	return fromLong2Bytes(Double.doubleToLongBits(value), isNet);
    }
    
    
}
