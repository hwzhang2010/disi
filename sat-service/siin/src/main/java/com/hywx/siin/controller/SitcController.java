package com.hywx.siin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hywx.siin.common.Page;
import com.hywx.siin.common.Resp;
import com.hywx.siin.service.TcService;

import io.swagger.annotations.Api;

@Api(value = "遥控controller", tags = {"遥控操作接口"})
@RestController
public class SitcController {
	@Autowired
	private TcService tcService;
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitc/tc/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listTcsByPage(@RequestBody JSONObject param) {
		System.out.println("siin tc list page param:" + param);
		String start = param.getString("start");
		String end = param.getString("end");
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = tcService.listTcsByPage(satelliteId, groundStationId, start, end, currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin tc list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitc/injection/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listInjectionsByPage(@RequestBody JSONObject param) {
		System.out.println("siin injection list page param:" + param);
		String start = param.getString("start");
		String end = param.getString("end");
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = tcService.listInjectionsByPage(satelliteId, groundStationId, start, end, currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin injection list page", page);
	}
	

}
