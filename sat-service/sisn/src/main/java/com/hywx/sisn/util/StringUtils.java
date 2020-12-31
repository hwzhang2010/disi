package com.hywx.sisn.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class StringUtils
{
	/**
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	public static final byte UTF8 = 0;
	public static final byte GBK = 1;
	public static final byte BIG5 = 2;
	public static final byte GB2312 = 3;
	public static final byte UCS2 = 4;
	public static final byte UTF16 = 5;

	public static String[] reservStringtoArray(String paramString, char paramChar) {
		return reservStringtoArray(paramString, String.valueOf(paramChar));
	}

	public static String[] reservStringtoArray(String paramString1, String paramString2) {
		if ((paramString1 == null) || (paramString2 == null))
			return null;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = paramString2.length();
		while (true) {
			k = paramString1.indexOf(paramString2, j);
			if (k == -1)
				break;
			++i;
			j = k + l;
		}
		String[] arrayOfString = new String[++i];
		j = 0;
		i = 0;
		while (true) {
			k = paramString1.indexOf(paramString2, j);
			if (k == -1)
				break;
			if (k == j)
				arrayOfString[i] = "";
			else
				arrayOfString[i] = paramString1.substring(j, k);
			++i;
			j = k + l;
		}
		l = paramString1.length();
		arrayOfString[i] = paramString1.substring(j, l);
		return arrayOfString;
	}

	/**
	 * 数组转换成十六进制字符串
	 *
	 * @param byte[]
	 * @return HexString
	 */
	public synchronized static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 16进制转十进制
	 *
	 * @param num
	 * @return
	 */
	public static int hextotenStr(String dealstr) {
		String str2B = hexString2binaryString(dealstr);
		if (str2B.length() == 4 * dealstr.length() && str2B.substring(0, 1).equals("1"))// 负数，首位唯1
		{
			dealstr = "0";
			for (int i = 1; i < str2B.length(); i++) {
				if (str2B.substring(i, i + 1).equals("1"))
					dealstr = dealstr + "0";
				else
					dealstr = dealstr + "1";
			}

			return -1 * (Integer.parseInt(dealstr, 2) + 1);
		} else {
			return Integer.parseInt(dealstr, 16);
		}

	}

	private static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 把16进制字符串转换成字节数组
	 *
	 * @param hexString
	 * @return byte[]
	 */
	public synchronized static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[len - i - 1] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) hexString.indexOf(c);
		return b;
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decodeGBK(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		try {
			return new String(baos.toByteArray(), "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encodeGBK(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		try {
			bytes = str.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 16进制转byte[]
	 *
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String intToHexStr(int n) {
		String s = Integer.toHexString(n);
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		return s;
	}

	public static String intToHexStr2(int n) {
		String s = Integer.toHexString(n);
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		if (s.length() < 4) {
			s = "00" + s;
		}
		return s;
	}

	/**
	 * byte 换算 4个bit
	 *
	 * @param b
	 * @return
	 */
	public static String[] byteToBit4(String s) {
		byte[] bs = hexStringToByte(s);
		byte b = bs[0];
		String[] args = new String[4];
		args[3] = "" + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
		args[2] = "" + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1);
		args[1] = "" + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1);
		args[0] = "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1);
		return args;
	}

	//将字符串转换为字节数组
	public static byte[] string2byte(String no){
		byte[] num=null;
		try{
			num=no.getBytes("GBK");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return num;
	}

	//将字符串转换为字节数组
	public static byte[] string2byte(String no,String charsetName){
		byte[] num=null;
		try{
			num=no.getBytes(charsetName);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return num;
	}

	//将byte类型转换为字符串
	public static String byte2string(byte[] b){
		String str=null;
		try{
			str = new String(b,"GBK");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return str;
	}

	public static String byte2string(byte[] b,String charsetName){
		String str=null;
		try{
			str = new String(b,charsetName);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return str;
	}


	public static void main(String[] args){
		String s = "0C";
		String[] ss = byteToBit4(s);
		for(String a:ss){
			System.out.println(a);
		}
	}
	/**
	 * bit字符串 转换Hex
	 * @param binary
	 * @return
	 */
	public static String bit8ToByeHex(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}

		return intToHexStr(result);
	}

}