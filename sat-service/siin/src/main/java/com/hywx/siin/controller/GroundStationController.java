package com.hywx.siin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hywx.siin.common.Page;
import com.hywx.siin.common.Resp;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.service.GroundStationService;
import com.hywx.siin.vo.GroundStationBusinessVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "信关站controller", tags = {"信关站操作接口"})
@RestController
public class GroundStationController {
	@Autowired
	private GroundStationService groundStationService;
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取所有的信关站信息", notes = "包括信关站ID, 代号, 名称, 地理位置(经度, 纬度, 高度), 应用程序初始化时会把所有的信关站信息放入redis中")
	@RequestMapping(value = "api/v1/siin/groundstations", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listGroundStations(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin groundstations param:" + param);
		
		List<GroundStationInfo> list = groundStationService.listAllGroundStations();
		 
        return Resp.getInstantiationSuccessList("siin groundstations", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstations/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listGroundStationsByPage(@RequestBody JSONObject param) {
		System.out.println("siin groundstations page param:" + param);
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = groundStationService.listGroundStationInfoByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin groundstations page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/exist", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp existGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin groundstation exist param:" + param);
		String groundStationId = param.getString("groundStationId");
		
		Boolean exist = groundStationService.existGroundStation(groundStationId);
		if (!exist)
			return Resp.getInstantiationErrorJsonString(String.format("信关站ID%s不存在", groundStationId), false);
		
		GroundStationInfo info = groundStationService.getGroundStationById(groundStationId);
		
        return Resp.getInstantiationSuccessJsonString("siin groundstation exist", info);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/insert", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin groundstation insert param:" + param);
		String groundStationId = param.getString("groundStationId");
		String groundStationName = param.getString("groundStationName");
		String groundStationText = param.getString("groundStationText");
		double groundStationLng = param.getDoubleValue("groundStationLng");
		double groundStationLat = param.getDoubleValue("groundStationLat");
		double groundStationAlt = param.getDoubleValue("groundStationAlt");
		
		int insert = groundStationService.insert(groundStationId, groundStationName, groundStationText, groundStationLng, groundStationLat, groundStationAlt);
		 
        return Resp.getInstantiationSuccessString("siin groundstation insert", insert);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/delete", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public Resp deleteGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin groundstation delete param:" + param);
		String groundStationId = param.getString("groundStationId");
		
		int delete = groundStationService.delete(groundStationId);
		 
        return Resp.getInstantiationSuccessString("siin groundstation delete", delete);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin groundstation udpate param:" + param);
		String groundStationId = param.getString("groundStationId");
		String groundStationName = param.getString("groundStationName");
		String groundStationText = param.getString("groundStationText");
		double groundStationLng = param.getDoubleValue("groundStationLng");
		double groundStationLat = param.getDoubleValue("groundStationLat");
		double groundStationAlt = param.getDoubleValue("groundStationAlt");
		
		int update = groundStationService.update(groundStationName, groundStationText, groundStationLng, groundStationLat, groundStationAlt, groundStationId);
		 
        return Resp.getInstantiationSuccessString("siin groundstation udpate", update);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/business/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listGroundStationBusiness(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin groundstation business param:" + param);
		
		List<GroundStationBusinessVO> list = groundStationService.listGroundStationBusinesses();
		 
        return Resp.getInstantiationSuccessList("siin groundstation business", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/groundstation/business/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateGroundStationBusiness(@RequestBody JSONObject param) {
		System.out.println("siin groundstation business udpate param:" + param);
		String groundStationId = param.getString("groundStationId");
		double usage = param.getDoubleValue("usage");
		String equipment = param.getString("equipment");
		String carrier = param.getString("carrier");
		String health = param.getString("health");
		
		int update = groundStationService.updateGroundStationBusiness(groundStationId, usage, equipment, carrier, health);
		 
        return Resp.getInstantiationSuccessString("siin groundstation business udpate", update);
	}
	
	

}
