package com.hywx.sirs.controller;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sirs.common.Resp;
import com.hywx.sirs.service.ExchangeService;
import com.hywx.sirs.vo.ExchangeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "仿真数据交互controller", tags = {"仿真数据交互状态监视和操作接口"})
@RestController
public class SirsController {
	@Autowired
	private ExchangeService exchangeService;
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取所有的仿真数据交互状态信息", notes = "包括信关站名称, 信关站ID, 接收IP, 接收端口, 连接状态(接收), 接收计数, 发送IP, 发送端口, 连接状态(发送), 发送计数")
	@RequestMapping(value = "api/v1/sirs/exchange", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp sirsExchange(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("sirs exchange param:" + param);
		
		Vector<ExchangeVO> vector = exchangeService.getCurrentExchange();
		 
        return Resp.getInstantiationSuccess("sirs exchange", Resp.LIST, vector);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "仿真数据交互计数清零", notes = "所有的接收计数, 发送计数都置为0")
	@RequestMapping(value = "api/v1/sirs/exchange/countzero", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp sirsExchangeCountZero(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("sirs exchange count zero param:" + param);
		
		exchangeService.putExchangeZero();
		 
        return Resp.getInstantiationSuccess("sirs exchange count zero", Resp.JSONSTRING, null);
	}
	
	
}
