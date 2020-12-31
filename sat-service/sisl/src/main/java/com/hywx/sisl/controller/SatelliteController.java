package com.hywx.sisl.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hywx.sisl.common.Resp;
import com.hywx.sisl.po.SatelliteInfo;
import com.hywx.sisl.po.SatelliteTle;
import com.hywx.sisl.service.SatelliteService;
import com.hywx.sisl.vo.OrbitElemVO;
import com.hywx.sisl.vo.SatelliteCircleVO;
import com.hywx.sisl.vo.SatelliteCoverVO;
import com.hywx.sisl.vo.SatellitePositionVO;

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
	@RequestMapping(value = "api/v1/sisl/satellites", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatellites(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("satellites param:" + param);
		
		List<SatelliteInfo> list = satelliteService.listSatellite();
		 
        return Resp.getInstantiationSuccessList("sisl satellites", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取前端已经添加的卫星ID", notes = "前端操作外测时会选择要使用的卫星, 被选中的卫星ID会被后台放入redis中")
	@RequestMapping(value = "api/v1/sisl/satellites/selected", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatellitesSelected(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("satellite selected param:" + param);
		
		List<String> list = satelliteService.listSatelliteIdInRedis();
		 
        return Resp.getInstantiationSuccessList("sisl satellite selected", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "把前端添加使用的卫星ID放入redis中", notes = "前端操作选择要使用的卫星, 被选中的卫星ID以JSON数组的形式发给后台")
	@RequestMapping(value = "api/v1/sisl/satellites/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertSatelliteIdToRedis(@ApiParam(value = "JSON数组, key为satellites, 表示前端选中的一组卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellites add param:" + param);
		JSONArray satelliteIdArray = param.getJSONArray("satellites");
	
		satelliteService.insertSatelliteIdToRedis(satelliteIdArray);
		 
        return Resp.getInstantiationSuccessString("sisl satellites add", "sisl satellites add ok");
	}
	
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID计算该卫星24小时的星下点位置, 时间间隔为1分钟", notes = "把后台计算的卫星星下点位置数据提供给前端, 作为cesium的SampledPositionProperty使用")
	@RequestMapping(value = "api/v1/sisl/satellite/positions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatellitePositions(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式; "
	    		                                + "2.key为satelliteId, value类型为String, 表示卫星ID", 
	                  required = true)
	        @RequestBody JSONObject param) {
		
		System.out.println("satellite positions param:" + param);
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		
		List<SatellitePositionVO> list = satelliteService.listSatellitePositions(satelliteId, timeStamp);
		 
        return Resp.getInstantiationSuccessList("sisl satellite positions", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID计算该卫星24小时的星下点位置(包含覆盖半径), 时间间隔为1分钟", notes = "把后台计算的卫星星下点位置(包含覆盖半径)数据提供给前端, 作为cesium的SampledPositionProperty和SampledProperty使用")
	@RequestMapping(value = "api/v1/sisl/satellite/circles", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatelliteCircles(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式; "
				                                + "2.key为satelliteId, value类型为String, 表示卫星ID", 
		              required = true)
			@RequestBody JSONObject param) {
		
		System.out.println("satellite circles param:" + param);
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		
		List<SatelliteCircleVO> list = satelliteService.listSatelliteCircles(satelliteId, timeStamp);
		 
        return Resp.getInstantiationSuccessList("sisl satellite circles", list);
	}
	
	
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的轨道根数", notes = "根据卫星ID获取该卫星的轨道根数")
	@RequestMapping(value = "api/v1/sisl/satellite/orbitelem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatelliteOrbitElem(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellite orbitelem param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		OrbitElemVO vo = satelliteService.getOrbitElem(satelliteId);
		 
        return Resp.getInstantiationSuccessString("sisl satellite orbitelem", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的轨道根数, 并把轨道根数发送给数据交互分系统", notes = "根据卫星ID获取该卫星的轨道根数, 并把轨道根数发送给数据交互分系统")
	@RequestMapping(value = "api/v1/sisl/satellite/orbitelem/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatelliteOrbitElemSend(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellite orbitelem send param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		OrbitElemVO vo = satelliteService.getOrbitElemSend(satelliteId);
		 
        return Resp.getInstantiationSuccessString("sisl satellite orbitelem send", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的两行根数", notes = "根据卫星ID获取该卫星的两行根数")
	@RequestMapping(value = "api/v1/sisl/satellite/tle", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatelliteTle(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellite tle param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		SatelliteTle tle = satelliteService.getSatelliteTle(satelliteId);
		 
        return Resp.getInstantiationSuccessString("sisl satellite tle", tle);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的运行周期", notes = "根据卫星ID获取该卫星的运行周期")
	@RequestMapping(value = "api/v1/sisl/satellite/tle/period", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatelliteTlePeriod(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellite tle period param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		Double period = satelliteService.getSatelliteTlePeriod(satelliteId);
		 
        return Resp.getInstantiationSuccessString("sisl satellite tle period", period);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取一组卫星的最短运行周期", notes = "获取一组卫星的最短运行周期")
	@RequestMapping(value = "api/v1/sisl/satellite/tle/minperiod", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatelliteTleMinPeriod(@ApiParam(value = "JSON数组, key为satellites, 表示前端要求计算比较的卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("satellite tle min period param:" + param);
		JSONArray satelliteIdArray = param.getJSONArray("satellites");
		
		Double minPeriod = satelliteService.getSatelliteTleMinPeriod(satelliteIdArray);
		 
        return Resp.getInstantiationSuccessString("sisl satellite tle min period", minPeriod);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID计算该卫星一个运行周期的星下点位置, 时间间隔为1分钟", notes = "根据卫星ID计算该卫星一个运行周期的星下点位置, 时间间隔为1分钟")
	@RequestMapping(value = "api/v1/sisl/satellite/tle/positions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatelliteTlePositions(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式; "
		                                        + "2.key为satelliteId, value类型为String, 表示卫星ID", 
                      required = true)
			@RequestBody JSONObject param) {
		
		System.out.println("satellite tle positions param:" + param);
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		
		List<SatellitePositionVO> list = satelliteService.listSatelliteTlePositions(satelliteId, timeStamp);
		 
        return Resp.getInstantiationSuccessList("sisl satellite tle positions", list);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID和一组信关站计算该卫星24小时的卫星覆盖半径和经过信关站的时刻", notes = "根据卫星ID和一组信关站计算该卫星24小时的卫星覆盖半径和经过信关站的时刻")
	@RequestMapping(value = "api/v1/sisl/satellite/covers", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSatelliteCovers(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为timeStamp, value类型为Long, 表示计算的起始时间, 以时间戳的形式; "
                                                + "2.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "3.key为groundStationIds, value类型为JSON数组, 表示使用的一组信关站", 
                     required = true)
			@RequestBody JSONObject param) {
		System.out.println("satellite covers param:" + param);
		String satelliteId = param.getString("satelliteId");
		Long timeStamp = param.getLong("timeStamp");
		JSONArray groundStationIdArray = param.getJSONArray("groundStationIds");
		
		List<SatelliteCoverVO> list = satelliteService.listSatelliteCovers(satelliteId, timeStamp, groundStationIdArray);
		 
        return Resp.getInstantiationSuccessList("sisl satellite covers", list);
	}
	

}
