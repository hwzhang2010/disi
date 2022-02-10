package com.hywx.siin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hywx.siin.common.Resp;
import com.hywx.siin.service.StationNetService;

@RestController
public class StationNetController {
	@Autowired
	private StationNetService stationNetService;
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/stationnet/reply/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateStationNetReply(@RequestBody JSONObject param) {
		System.out.println("siin stationnet reply udpate param:" + param);
		String reply = param.getString("reply");
		
		int update = stationNetService.updateReply(reply);
		 
        return Resp.getInstantiationSuccessString("siin stationnet reply udpate", update);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/stationnet/state/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateStationNetState(@RequestBody JSONObject param) {
		System.out.println("siin stationnet state udpate param:" + param);
		String groundStationName = param.getString("groundStationName");
		Integer subsystemId = param.getInteger("subsystemId");
		String equipmentId = param.getString("equipmentId");
		String warning = param.getString("warning");
		Integer healthLevel = param.getInteger("healthLevel");
		
		System.out.println("*****" + groundStationName + ", " + subsystemId + ", " + equipmentId + ", " + warning + ", " + healthLevel);
		
		int update = stationNetService.updateState(groundStationName, subsystemId, equipmentId, warning, healthLevel);
		// 发送
		stationNetService.sendStationNetState();
		
        return Resp.getInstantiationSuccessString("siin stationnet state udpate", update);
	}
	

}
