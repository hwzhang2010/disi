package com.hywx.sitm.bo.gps;

import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.util.ByteUtil;

public class SitmGpsVx extends AbstractSitmGps {

	@Override
	public SitmParam getGpsParam(String satelliteId, String value, GpsFrame frame) {
        double vx = frame.getVx();
		
		return new SitmParam(ByteUtil.fromDouble2Bytes(vx, false), String.valueOf(vx));
	}

}
