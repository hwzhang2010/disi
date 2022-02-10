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
import com.hywx.siin.po.GpsFrame;
import com.hywx.siin.po.SatelliteInfo;
import com.hywx.siin.service.SatelliteService;
import com.hywx.siin.vo.SatelliteBusinessVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "卫星controller", tags = {"卫星操作接口"})
@RestController
public class SatelliteController {
	
	@Autowired
	private SatelliteService satelliteService;
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取所有的卫星信息", notes = "包括卫星ID, 代号, 名称, 存在于sqlite3数据库中")
	@RequestMapping(value = "api/v1/siin/satellites", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatellites(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin satellites param:" + param);
		
		List<SatelliteInfo> list = satelliteService.listAllSatelliteInfos();
		 
        return Resp.getInstantiationSuccessList("siin satellites", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "把GPS数据文件中的数据插入sqlite3数据库", notes = "(已无用), 读取本地gps.txt文件中的GPS数据, 批量插入sqlite3数据库")
	@RequestMapping(value = "api/v1/satellite/gps/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp addSatellietGps(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin satellite gps add param:" + param);
		
		List<GpsFrame> list = satelliteService.listGpsFrameFromDataSource();
		int count = list.size() / 1024;
		for (int i = 0; i < count; i++) {
			satelliteService.insertBatchGpsFrame(list.subList(1024 * i, 1024 * (i + 1)));
		}
		satelliteService.insertBatchGpsFrame(list.subList(1024 * count, list.size()));
		 
        return Resp.getInstantiationSuccessList("sitm satellite gps add", list.size());
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取已经存入sqlite3数据库中的全部的GPS数据", notes = "从sqlite3数据库中查询所全部的GPS数据")
	@RequestMapping(value = "api/v1/satellite/gps/all", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatellietGps(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin satellite gps all param:" + param);
		
		List<GpsFrame> list = satelliteService.listGpsFrames();
		
        return Resp.getInstantiationSuccessList("sitm satellite gps all", list.subList(0, 1024));
	}
	
	
	
	/***********2020.12***************************************************************/
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellites/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatellitesByPage(@RequestBody JSONObject param) {
		System.out.println("siin satellites page param:" + param);
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = satelliteService.listSatelliteInfoByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin satellites page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/exist", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp existSatellite(@RequestBody JSONObject param) {
		System.out.println("siin satellite exist param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		Boolean exist = satelliteService.existSatellite(satelliteId);
		if (!exist)
			return Resp.getInstantiationErrorJsonString(String.format("卫星ID%s不存在", satelliteId), false);
		
		SatelliteInfo info = satelliteService.getSatelliteById(satelliteId);
		
        return Resp.getInstantiationSuccessJsonString("siin satellite exist", info);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/insert", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertSatellite(@RequestBody JSONObject param) {
		System.out.println("siin satellite insert param:" + param);
		String satelliteId = param.getString("satelliteId");
		String satelliteName = param.getString("satelliteName");
		String satelliteText = param.getString("satelliteText");
		
		int insert = satelliteService.insert(satelliteId, satelliteName, satelliteText);
		 
        return Resp.getInstantiationSuccessString("siin satellite insert", insert);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/delete", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public Resp deleteSatellite(@RequestBody JSONObject param) {
		System.out.println("siin satellite delete param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		int delete = satelliteService.delete(satelliteId);
		 
        return Resp.getInstantiationSuccessString("siin satellite delete", delete);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSatellite(@RequestBody JSONObject param) {
		System.out.println("siin satellite udpate param:" + param);
		String satelliteId = param.getString("satelliteId");
		String satelliteName = param.getString("satelliteName");
		String satelliteText = param.getString("satelliteText");
		
		int update = satelliteService.update(satelliteName, satelliteText, satelliteId);
		 
        return Resp.getInstantiationSuccessString("siin satellite udpate", update);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/business/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatelliteBusiness(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("siin satellite business param:" + param);
		Long timeStamp = param.getLong("timeStamp");
		
		List<SatelliteBusinessVO> list = satelliteService.listSatelliteBusinesses(timeStamp);
		 
        return Resp.getInstantiationSuccessList("siin satellite business", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/satellite/business/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSatelliteBusiness(@RequestBody JSONObject param) {
		System.out.println("siin satellite business udpate param:" + param);
		String satelliteId = param.getString("satelliteId");
		double usage = param.getDoubleValue("usage");
		String condition = param.getString("condition");
		String health = param.getString("health");
		
		if (!satelliteService.existSatellite(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		
		int update = satelliteService.updateBusiness(usage, condition, health, satelliteId);
		 
        return Resp.getInstantiationSuccessString("siin satellite business udpate", update);
	}
	
	
	
	
	
	

}
