package com.hywx.sitm.bo.type;

import org.springframework.util.StringUtils;

import com.hywx.sitm.bo.param.SitmParam;

public class SitmStringType extends AbstractSitmType {

	@Override
	public SitmParam getSitmParam(String value, String paramType, double coefficient) {
		if (StringUtils.isEmpty(value))
			return null;
			
		return new SitmParam(value.getBytes(), value);
	}

}
