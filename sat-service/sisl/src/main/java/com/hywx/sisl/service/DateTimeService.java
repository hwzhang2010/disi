package com.hywx.sisl.service;

import com.hywx.sisl.vo.DateTimeVO;

public interface DateTimeService {

	// 获取仿真的时间(开始时间)
	DateTimeVO getDateTime();
	
	// 设置仿真的时间(开始时间)
	DateTimeVO updateDateTimeStart(String start);
	
	// 设置SGP4的计算时间
	String updateTimeStamp(Long timeStamp);
	
	// 设置SGP4的计算时间
	String requestTimeStamp(Long timeStamp, Double multiplier);
}
