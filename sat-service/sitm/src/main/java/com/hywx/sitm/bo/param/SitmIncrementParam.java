package com.hywx.sitm.bo.param;

import com.hywx.sitm.util.ByteUtil;

public class SitmIncrementParam extends AbstractSitmParam {
	
	private String value;

	@Override
	public String valueOfParam() {
		
		return this.value;
	}
	
	@Override
	public byte[] bytesOfByte(String value, double coefficient) {
		short b = new Double(Double.parseDouble(value) + coefficient).shortValue();
		this.value = String.valueOf(b % (0xFF + 1));
		
		return new byte[] { (byte) b };
	}
	
	@Override
	public byte[] bytesOfSByte(String value, double coefficient) {
		byte increment = new Double(Double.parseDouble(value) + coefficient).byteValue();
		this.value = String.valueOf(increment);
		
		return new byte[] { increment };
	}

	@Override
	public byte[] bytesOfUShort(String value, double coefficient) {
		int increment = new Double(Double.parseDouble(value) + coefficient).intValue();
		this.value = String.valueOf(increment);
		
		return ByteUtil.fromUShort2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfShort(String value, double coefficient) {
		short increment = new Double(Double.parseDouble(value) + coefficient).shortValue();
		this.value = String.valueOf(increment);
		
		return ByteUtil.fromShort2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfUInt(String value, double coefficient) {
		long increment = Math.round(Double.parseDouble(value) + coefficient);
		this.value = String.valueOf(increment);
		
		return ByteUtil.fromUInt2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfInt(String value, double coefficient) {
		int increment = new Double(Double.parseDouble(value) + coefficient).intValue();
		this.value = String.valueOf(increment);
		
		return ByteUtil.fromInt2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfLong(String value, double coefficient) {
		long increment = Math.round(Double.parseDouble(value) + coefficient);
		this.value = String.valueOf(increment);
	
		return ByteUtil.fromLong2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfFloat(String value, double coefficient) {
		float increment = (float) (Float.parseFloat(value) + coefficient);
		this.value = String .format("%.4f", increment);
		
		return ByteUtil.fromFloat2Bytes(increment, false);
	}

	@Override
	public byte[] bytesOfDouble(String value, double coefficient) {
		double increment = Double.parseDouble(value) + coefficient;
		this.value = String .format("%.6f", increment);
		
		return ByteUtil.fromDouble2Bytes(increment, false);
	}


}
