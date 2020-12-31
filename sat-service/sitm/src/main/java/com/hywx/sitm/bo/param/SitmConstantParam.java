package com.hywx.sitm.bo.param;

import com.hywx.sitm.util.ByteUtil;

/**
 * 仿真参数类型：常数
 * @author zhang.hauwei
 *
 */
public class SitmConstantParam extends AbstractSitmParam {
	private String value;

	@Override
	public String valueOfParam() {
		
		return this.value;
	}
	
	@Override
	public byte[] bytesOfByte(String value, double coefficient) {
		int b = (int) (Double.doubleToLongBits(coefficient) >= 0 ? coefficient : 0xFF + coefficient);
		this.value = String.valueOf(b % (0xFF + 1));
		
		return new byte[] { (byte) coefficient };
	}
	
	@Override
	public byte[] bytesOfSByte(String value, double coefficient) {
		this.value = String.valueOf((byte) coefficient);
		
		return new byte[] { (byte) coefficient };
	}

	@Override
	public byte[] bytesOfUShort(String value, double coefficient) {
		this.value = String.valueOf((int) coefficient);
		
		return ByteUtil.fromUShort2Bytes((int) coefficient, false);
	}

	@Override
	public byte[] bytesOfShort(String value, double coefficient) {
		this.value = String.valueOf((short) coefficient);
		
		return ByteUtil.fromShort2Bytes((short) coefficient, false);
	}

	@Override
	public byte[] bytesOfUInt(String value, double coefficient) {
		this.value = String.valueOf((long) coefficient);
		
		return ByteUtil.fromUInt2Bytes((long) coefficient, false);
	}

	@Override
	public byte[] bytesOfInt(String value, double coefficient) {
		this.value = String.valueOf((int) coefficient);
		
		return ByteUtil.fromInt2Bytes((int) coefficient, false);
	}

	@Override
	public byte[] bytesOfLong(String value, double coefficient) {
		this.value = String.valueOf((long) coefficient);
	
		return ByteUtil.fromLong2Bytes((long) coefficient, false);
	}

	@Override
	public byte[] bytesOfFloat(String value, double coefficient) {
		this.value = String.valueOf((float) coefficient);
		
		return ByteUtil.fromFloat2Bytes((float) coefficient, false);
	}

	@Override
	public byte[] bytesOfDouble(String value, double coefficient) {
		this.value = String.valueOf(coefficient);
		
		return ByteUtil.fromDouble2Bytes(coefficient, false);
	}

	

}
