package com.hywx.sitm.bo.gps;

import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.global.GlobalAccess;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.util.ByteUtil;

public class SitmGpsSx extends AbstractSitmGps {

	@Override
	public SitmParam getGpsParam(String satelliteId, String value, GpsFrame frame) {
		double sx = frame.getSx();
		
		return new SitmParam(ByteUtil.fromDouble2Bytes(sx, false), String.valueOf(sx));
	}

}
