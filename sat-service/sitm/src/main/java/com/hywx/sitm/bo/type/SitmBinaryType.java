package com.hywx.sitm.bo.type;

import org.springframework.util.StringUtils;

import com.hywx.sitm.bo.param.SitmParam;
import com.hywx.sitm.util.ByteUtil;

public class SitmBinaryType extends AbstractSitmType {

	@Override
	public SitmParam getSitmParam(String value, String paramType, double coefficient) {
		if (StringUtils.isEmpty(value))
			return null;
		
		String regex="^[A-Fa-f0-9]+$";
		if(value.matches(regex)) {
		    byte[] buffer = ByteUtil.toByteArray(value);
		    return new SitmParam(buffer, value);
		}
		
		return null;
	}

}
