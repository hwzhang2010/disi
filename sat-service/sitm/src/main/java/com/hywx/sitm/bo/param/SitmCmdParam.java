package com.hywx.sitm.bo.param;

import com.hywx.sitm.util.ByteUtil;

public class SitmCmdParam extends AbstractSitmParam {
	
	private String value;

	@Override
	public String valueOfParam() {
		
		return this.value;
	}

	@Override
	public byte[] bytesOfByte(String value, double coefficient) {
		short b = new Double(Double.parseDouble(value)).shortValue();
		this.value = String.valueOf(b % (0xFF + 1));
		
		return new byte[] { (byte) b };
	}
	
	@Override
	public byte[] bytesOfSByte(String value, double coefficient) {
		byte sb = new Double(Double.parseDouble(value)).byteValue();
		this.value = String.valueOf(sb);
		
		return new byte[] { sb };
	}

	@Override
	public byte[] bytesOfUShort(String value, double coefficient) {
		int us = new Double(Double.parseDouble(value)).intValue();
		this.value = String.valueOf(us);
		
		return ByteUtil.fromUShort2Bytes(us, false);
	}

	@Override
	public byte[] bytesOfShort(String value, double coefficient) {
		short ss = new Double(Double.parseDouble(value)).shortValue();
		this.value = String.valueOf(ss);
		
		return ByteUtil.fromShort2Bytes(ss, false);
	}

	@Override
	public byte[] bytesOfUInt(String value, double coefficient) {
		long ui = Math.round(Double.parseDouble(value));
		this.value = String.valueOf(ui);
		
		return ByteUtil.fromUInt2Bytes(ui, false);
	}

	@Override
	public byte[] bytesOfInt(String value, double coefficient) {
		int i = new Double(Double.parseDouble(value)).intValue();
		this.value = String.valueOf(i);
		
		return ByteUtil.fromInt2Bytes(i, false);
	}

	@Override
	public byte[] bytesOfLong(String value, double coefficient) {
		long l = Math.round(Double.parseDouble(value));
		this.value = String.valueOf(l);
	
		return ByteUtil.fromLong2Bytes(l, false);
	}

	@Override
	public byte[] bytesOfFloat(String value, double coefficient) {
		float f = Float.parseFloat(value);
		this.value = String .format("%.4f", f);
		
		return ByteUtil.fromFloat2Bytes(f, false);
	}

	@Override
	public byte[] bytesOfDouble(String value, double coefficient) {
		double dbl = Double.parseDouble(value);
		this.value = String .format("%.6f", dbl);
		
		return ByteUtil.fromDouble2Bytes(dbl, false);
	}

}
