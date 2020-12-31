package com.hywx.sitm.bo.type;

import com.hywx.sitm.bo.param.AbstractSitmParam;
import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.bo.param.SitmParamEnum;

public class SitmByteType extends AbstractSitmType {

	@Override
	public SitmParam getSitmParam(String value, String paramType, double coefficient) {
		SitmParamEnum enSitmParam = SitmParamEnum.match(paramType);
		AbstractSitmParam sitmParam = enSitmParam.getSitmParam();
		
		if (sitmParam != null) {
			byte[] buffer = sitmParam.bytesOfByte(value, coefficient);
			String newValue = sitmParam.valueOfParam();
			
			return new SitmParam(buffer, newValue);
		}
		
		return null;
	}

}
