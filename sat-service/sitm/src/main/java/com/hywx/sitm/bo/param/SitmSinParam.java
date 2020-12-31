package com.hywx.sitm.bo.param;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.hywx.sitm.util.ByteUtil;

public class SitmSinParam extends AbstractSitmParam {
	private final long FULL_DEGREE = 360L;
	private final long HALF_DEGREE = 180L;
	private final double RADIAN_DEGREE = 180;
	
	private String value;

	@Override
	public String valueOfParam() {
		
		return this.value;
	}

	@Override
	public byte[] bytesOfByte(String value, double coefficient) {
		int sin = new Double(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient).shortValue();
		sin = sin >= 0 ? sin : 0xFF + sin;
		this.value = String.valueOf(sin);
		
		return new byte[] { (byte) sin };
	}
	
	@Override
	public byte[] bytesOfSByte(String value, double coefficient) {
		byte sin = new Double(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient).byteValue();
		this.value = String.valueOf(sin);
		
		return new byte[] { sin };
	}

	@Override
	public byte[] bytesOfUShort(String value, double coefficient) {
		int sin = new Double(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient).intValue();
		this.value = String.valueOf(sin);
		
		return ByteUtil.fromUShort2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfShort(String value, double coefficient) {
		short sin = new Double(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient).shortValue();
		this.value = String.valueOf(sin);
		
		return ByteUtil.fromShort2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfUInt(String value, double coefficient) {
		long sin = Math.round(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient);
		this.value = String.valueOf(sin);
		
		return ByteUtil.fromUInt2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfInt(String value, double coefficient) {
		int sin = new Double(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient).intValue();
		this.value = String.valueOf(sin);
		
		return ByteUtil.fromInt2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfLong(String value, double coefficient) {
		long sin = Math.round(Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient);
		this.value = String.valueOf(sin);
		
		return ByteUtil.fromLong2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfFloat(String value, double coefficient) {
		float sin = (float) (Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient);
		this.value = String .format("%.4f", sin);
		
		return ByteUtil.fromFloat2Bytes(sin, false);
	}

	@Override
	public byte[] bytesOfDouble(String value, double coefficient) {
		double sin = Math.sin( (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) % FULL_DEGREE - HALF_DEGREE) / RADIAN_DEGREE * Math.PI ) * coefficient;
		this.value = String .format("%.6f", sin);
		
		return ByteUtil.fromDouble2Bytes(sin, false);
	}

}
