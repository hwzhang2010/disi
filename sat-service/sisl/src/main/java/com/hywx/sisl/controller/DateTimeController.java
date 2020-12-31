package com.hywx.sisl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hywx.sisl.common.Resp;
import com.hywx.sisl.service.DateTimeService;
import com.hywx.sisl.vo.DateTimeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "时间controller", tags = {"时间操作接口"})
@RestController
public class DateTimeController {
	
	@Autowired
	private DateTimeService dateTimeService;
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sisl/datetime", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getDateTime(@RequestBody JSONObject param) {
		System.out.println("sisl datetime param:" + param);
		
		DateTimeVO vo = dateTimeService.getDateTime();
		 
        return Resp.getInstantiationSuccessJsonString("sisl datetime", vo);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sisl/datetime/start", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateDateTimeStart(@RequestBody JSONObject param) {
		System.out.println("sisl datetime start param:" + param);
		String start = param.getString("start");
		
		DateTimeVO vo = dateTimeService.updateDateTimeStart(start);
		 
        return Resp.getInstantiationSuccessJsonString("sisl datetime start", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "更新后台计算卫星位置和速度的历元时间", notes = "前端定时把Cesium的clock时间以时间戳的形式发给后台，用于计算卫星下一时刻的位置和速度(SGP4计算使用的历元时间)")
	@RequestMapping(value = "api/v1/sisl/datetime/timestamp/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateTimeStamp(@ApiParam(value = "JSON对象, 包括1个属性, key为timeStamp, value类型为Long, 表示前端以时间戳形式发送的历元时间", required = true) @RequestBody JSONObject param) {
		System.out.println("sisl datetime timestamp update param:" + param);
		
		Long timeStamp = param.getLong("timeStamp");
		String date = dateTimeService.updateTimeStamp(timeStamp);
		 
        return Resp.getInstantiationSuccessString("sisl datetime timestamp update", date);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "更新后台计算卫星位置和速度的历元时间", notes = "前端定时把Cesium的clock时间以时间戳的形式发给后台，用于计算卫星下一时刻的位置和速度(SGP4计算使用的历元时间)")
	@RequestMapping(value = "api/v1/sisl/datetime/timestamp/request", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp requestTimeStamp(@ApiParam(value = "JSON对象, 包括1个属性, key为timeStamp, value类型为Long, 表示前端以时间戳形式发送的历元时间", required = true) @RequestBody JSONObject param) {
		System.out.println("sisl datetime timestamp request param:" + param);
		
		Long timeStamp = param.getLong("timeStamp");
		String date = dateTimeService.updateTimeStamp(timeStamp);
		 
        return Resp.getInstantiationSuccessString("sisl datetime timestamp request", date);
	}
	
	
	

}
