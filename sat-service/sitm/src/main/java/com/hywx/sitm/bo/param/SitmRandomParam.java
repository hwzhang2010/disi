package com.hywx.sitm.bo.param;

import java.util.Random;

import com.hywx.sitm.util.ByteUtil;

public class SitmRandomParam extends AbstractSitmParam {
	private final int MAX_BYTE = 0xFF;
	private final int MAX_USHORT = 0xFFFF;
	
	private Random random = new Random();
	
	private String value;

	@Override
	public String valueOfParam() {
		
		return this.value;
	}

	@Override
	public byte[] bytesOfByte(String value, double coefficient) {
		int r = random.nextInt(MAX_BYTE);
		this.value = String.valueOf(r);
		
		return new byte[] { (byte) r };
	}
	
	@Override
	public byte[] bytesOfSByte(String value, double coefficient) {
		int r = random.nextInt(Byte.MAX_VALUE) + Byte.MIN_VALUE;
		this.value = String.valueOf(r);
		
		return new byte[] { (byte) r };
	}

	@Override
	public byte[] bytesOfUShort(String value, double coefficient) {
		int r = random.nextInt(MAX_USHORT);
		this.value = String.valueOf(r);
		
		return ByteUtil.fromUShort2Bytes(r, false);
	}

	@Override
	public byte[] bytesOfShort(String value, double coefficient) {
		int r = random.nextInt(MAX_USHORT) + Short.MIN_VALUE;
		this.value = String.valueOf(r);
		
		return ByteUtil.fromShort2Bytes((short)r, false);
	}

	@Override
	public byte[] bytesOfUInt(String value, double coefficient) {
		long r = random.nextInt(Integer.MAX_VALUE);
		this.value = String.valueOf(r);
		
		return ByteUtil.fromUInt2Bytes(r, false);
	}

	@Override
	public byte[] bytesOfInt(String value, double coefficient) {
		int r = random.nextInt();
		this.value = String.valueOf(r);
		
		return ByteUtil.fromInt2Bytes(r, false);
	}

	@Override
	public byte[] bytesOfLong(String value, double coefficient) {
		long r = random.nextLong();
		this.value = String.valueOf(r);
		
		return ByteUtil.fromLong2Bytes(r, false);
	}

	@Override
	public byte[] bytesOfFloat(String value, double coefficient) {
		double r = random.nextFloat() * coefficient;
		this.value = String .format("%.4f", r);
		
		return ByteUtil.fromFloat2Bytes((float) r, false);
	}

	@Override
	public byte[] bytesOfDouble(String value, double coefficient) {
		double r = random.nextDouble() * coefficient;
		this.value = String .format("%.6f", r);
		
		return ByteUtil.fromDouble2Bytes(r, false);
	}

	
}
