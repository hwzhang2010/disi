package com.hywx.sitm.bo.gps;

import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.util.ByteUtil;

public class SitmGpsSz extends AbstractSitmGps {

	@Override
	public SitmParam getGpsParam(String satelliteId, String value, GpsFrame frame) {
        double sz = frame.getSz();
		
		return new SitmParam(ByteUtil.fromDouble2Bytes(sz, false), String.valueOf(sz));
	}

}
