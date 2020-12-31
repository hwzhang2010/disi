package com.hywx.sisl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.amsacode.predict4java.SatPassTime;
import com.hywx.sisl.common.Resp;
import com.hywx.sisl.po.GroundStationInfo;
import com.hywx.sisl.service.GroundStationService;
import com.hywx.sisl.vo.GroundStationFollowVO;
import com.hywx.sisl.vo.GroundStationPassVO;

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
	@RequestMapping(value = "api/v1/sisl/groundstations", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listGroundStations(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("groundstation param:" + param);
		
		List<GroundStationInfo> list = groundStationService.listGroundStation();
		 
        return Resp.getInstantiationSuccessList("sisl groundstations", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取前端已经添加的信关站ID", notes = "前端操作外测时会选择要使用的信关站, 被选中的信关站ID会被后台放入redis中")
	@RequestMapping(value = "api/v1/sisl/groundstations/selected", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listGroundStationsSelected(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("groundstations selected param:" + param);
		
		List<String> list = groundStationService.listGroundStationIdInRedis();
		 
        return Resp.getInstantiationSuccessList("sisl groundstations selected", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "把前端添加使用的信关站ID放入redis中", notes = "前端操作选择要使用的信关站, 被选中的信关站ID以JSON数组的形式发给后台")
	@RequestMapping(value = "api/v1/sisl/groundstations/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertGroundStationIdToRedis(@ApiParam(value = "JSON数组, key为groundstations, 表示前端选中的信关站ID", required = true) @RequestBody JSONObject param) {
		System.out.println("groundstations add param:" + param);
		JSONArray groundStationIdArray = param.getJSONArray("groundstations");
		
		groundStationService.insertGroundStationIdToRedis(groundStationIdArray);
		 
        return Resp.getInstantiationSuccessString("sisl groundstations add", "sisl groundstations add ok");
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "针对选定的某个信关站和卫星, 根据时间计算卫星下次过境时信关站的信息", notes = "包括过境时的开始时刻，持续时间，结束时刻，入境方位角，最大俯仰角，出境方位角")
	@RequestMapping(value = "api/v1/sisl/groundstation/nextpass", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getGroundStationNextPass(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为groundStationId, value类型为String, 表示信关站ID; "
                                                + "2.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "3.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("groundstation next pass param:" + param);
		String groundStationId = param.getString("groundStationId");
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		
		GroundStationPassVO vo = groundStationService.getNextPass(groundStationId, satelliteId, timeStamp);
		
        return Resp.getInstantiationSuccessString("sisl groundstation next pass", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "针对选定的某个信关站和卫星, 根据时间计算卫星下次过境时信关站的跟踪信息", notes = "包括过境时从开始时刻到结束时刻的方位角和俯仰角，时间间隔为1秒")
	@RequestMapping(value = "api/v1/sisl/groundstation/nextpasses", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listtGroundStationNextPasses(
			@ApiParam(value = "JSON对象, 包括4个属性, 1.key为groundStationId, value类型为String, 表示信关站ID; "
                                                + "2.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "3.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式;"
                                                + "4.key为hours, value类型为Integer, 表示从起始时间推算的小时数", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("groundstation next passes param:" + param);
		String groundStationId = param.getString("groundStationId");
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		Integer hours = param.getInteger("hours");
		
		List<GroundStationPassVO> list = groundStationService.listNextPasses(groundStationId, satelliteId, timeStamp, hours);
		
        return Resp.getInstantiationSuccessList("sisl groundstation next passes", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "针对选定的某个信关站和卫星, 计算一段时间内的卫星过境的信息", notes = "包括时间段内多次过境时的开始时刻，持续时间，结束时刻，入境方位角，最大俯仰角，出境方位角")
	@RequestMapping(value = "api/v1/sisl/groundstation/nextfollows", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listtGroundStationNextFollows(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为groundStationId, value类型为String, 表示信关站ID; "
                                                + "2.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "3.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("groundstation follow param:" + param);
		String groundStationId = param.getString("groundStationId");
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		
		List<GroundStationFollowVO> list = groundStationService.listNextFollows(groundStationId, satelliteId, timeStamp);
		
        return Resp.getInstantiationSuccessList("sisl groundstation follow", list);
	}
	

}
