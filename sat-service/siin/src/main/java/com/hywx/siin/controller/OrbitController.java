package com.hywx.siin.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hywx.siin.common.Page;
import com.hywx.siin.common.Resp;
import com.hywx.siin.global.GlobalAccess;
import com.hywx.siin.po.GroundStationFollow;
import com.hywx.siin.po.GroundStationPass;
import com.hywx.siin.po.SatelliteAngle;
import com.hywx.siin.po.SatelliteCover;
import com.hywx.siin.po.SatelliteRange;
import com.hywx.siin.po.SatelliteRegion;
import com.hywx.siin.po.SatelliteSingle;
import com.hywx.siin.po.SatelliteMulti;
import com.hywx.siin.po.SatelliteTle;
import com.hywx.siin.po.SatelliteWaveBeam;
import com.hywx.siin.service.GroundStationService;
import com.hywx.siin.service.OrbitService;
import com.hywx.siin.service.SatelliteService;
import com.hywx.siin.vo.GroundStationMultiCoverVO;

import io.swagger.annotations.Api;

@Api(value = "轨道controller", tags = {"轨道操作接口"})
@RestController
public class OrbitController {
	@Autowired
	private SatelliteService satelliteService;
	@Autowired
	private GroundStationService groundStationService;
	@Autowired
	private OrbitService orbitService;
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/elems", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp orbitTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit tle param:" + param);
		String satelliteId = param.getString("satelliteId");
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), null);
		
		if (!orbitService.existTle(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数不存在", satelliteId), null);
		
		SatelliteTle satelliteTle = orbitService.getTle(satelliteId);
		String[] elems = orbitService.getOrbitElemByTle(satelliteTle.getTleLine1(), satelliteTle.getTleLine2());
		
        return Resp.getInstantiationSuccessString("siin orbit tle", elems);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listTle(@RequestBody JSONObject param) {
		//System.out.println("siin orbit tle list param:" + param);
		
		List<SatelliteTle> tleList = orbitService.listTle();
		 
        return Resp.getInstantiationSuccessList("siin orbit tle list", tleList);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listTleByPage(@RequestBody JSONObject param) {
		//System.out.println("siin orbit tle list by page param:" + param);
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listTleByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin orbit tle list by page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getTleById(@RequestBody JSONObject param) {
		//System.out.println("siin orbit tle list by satellite id param:" + param);
		String satelliteId = param.getString("satelliteId");
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), null);
		
		if (!orbitService.existTle(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数不存在", satelliteId), null);
		
		SatelliteTle satelliteTle = orbitService.getTle(satelliteId);
		 
        return Resp.getInstantiationSuccessJsonString("siin orbit tle list by satellite id", satelliteTle);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/verify", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp verifyTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit verify param:" + param);
		//String satelliteId = param.getString("satelliteId");
		double t = param.getDoubleValue("t"); 
		double eccentricity = param.getDoubleValue("e"); 
		double inclination = param.getDoubleValue("i"); 
		double raan = param.getDoubleValue("o"); 
		double argumentOfPerigee = param.getDoubleValue("w");
		double meanAnomaly = param.getDoubleValue("m");
		
		if (!orbitService.verifyTle(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly))
			return Resp.getInstantiationErrorString("orbit elem is not valid", false);
		
		String[] tle = orbitService.getTleByOrbitElem(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly);
		 
        return Resp.getInstantiationSuccessString("轨道根数有效", tle);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/delete", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public Resp deleteTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit delete param:" + param);
		String satelliteId = param.getString("satelliteId");
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		
		if (!orbitService.existTle(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数不存在", satelliteId), false);
		
		int result = orbitService.deleteTle(satelliteId);
		if (result < 1)
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数删除失败", satelliteId), false);
				 
        return Resp.getInstantiationSuccessString(String.format("卫星ID%s的轨道根数删除成功", satelliteId), true);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/exist", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp existTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit tle exist param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		
		Boolean exist = orbitService.existTle(satelliteId);
		 
        return Resp.getInstantiationSuccessString("siin orbit tle exist", exist);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/insert", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit insert param:" + param);
		String satelliteId = param.getString("satelliteId");
		double t = param.getDoubleValue("t"); 
		double eccentricity = param.getDoubleValue("e"); 
		double inclination = param.getDoubleValue("i"); 
		double raan = param.getDoubleValue("o"); 
		double argumentOfPerigee = param.getDoubleValue("w");
		double meanAnomaly = param.getDoubleValue("m");
		
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		
		if (orbitService.existTle(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数已存在", satelliteId), false);
		
		if (!orbitService.verifyTle(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly))
			return Resp.getInstantiationErrorString("orbit elem is not valid", null);
		
		String[] tle = orbitService.getTleByOrbitElem(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly);
		
		int result = orbitService.insertTle(satelliteId, satelliteId, tle[1], tle[2]);
		 
        return Resp.getInstantiationSuccessString(String.format("卫星ID%s的轨道根数添加成功", satelliteId), result);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateTle(@RequestBody JSONObject param) {
		System.out.println("siin orbit update param:" + param);
		String satelliteId = param.getString("satelliteId");
		double t = param.getDoubleValue("t"); 
		double eccentricity = param.getDoubleValue("e"); 
		double inclination = param.getDoubleValue("i"); 
		double raan = param.getDoubleValue("o"); 
		double argumentOfPerigee = param.getDoubleValue("w");
		double meanAnomaly = param.getDoubleValue("m");
		
		if (!satelliteService.existSatellite(satelliteId))
		    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		
		if (!orbitService.existTle(satelliteId))
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的轨道根数不存在", satelliteId), false);
		
		if (!orbitService.verifyTle(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly))
			return Resp.getInstantiationErrorString("orbit elem is not valid", null);
		
		int result = orbitService.updateTle(satelliteId, t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly);
		
        return Resp.getInstantiationSuccessString(String.format("卫星ID%s的轨道根数更新成功", satelliteId), result);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/elem2tle", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp elem2Tle(@RequestBody JSONObject param) {
		System.out.println("siin orbit elem2tle param:" + param);
		double t = param.getDoubleValue("t"); 
		double eccentricity = param.getDoubleValue("e"); 
		double inclination = param.getDoubleValue("i"); 
		double raan = param.getDoubleValue("o"); 
		double argumentOfPerigee = param.getDoubleValue("w");
		double meanAnomaly = param.getDoubleValue("m");
		
		if (!orbitService.verifyTle(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly))
			return Resp.getInstantiationErrorString("orbit elem is not valid", null);
		
		String[] tle = orbitService.getTleByOrbitElem(t, inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly);
		 
        return Resp.getInstantiationSuccessString("siin orbit elem2tle", tle);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/orbit/tle2elem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp tle2Elem(@RequestBody JSONObject param) {
		System.out.println("siin orbit tle2elem param:" + param);
		//String satelliteId = param.getString("satelliteId");
		String line1 = param.getString("line1");
		String line2 = param.getString("line2");
		
		if (!orbitService.verifyTle(line1, line2))
			return Resp.getInstantiationErrorString("tle is not valid", null);
		
		String[] elems = orbitService.getOrbitElemByTle(line1, line2);
		 
        return Resp.getInstantiationSuccessString("siin orbit tle2elem", elems);
	}
	
	
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/range/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listRanges(@RequestBody JSONObject param) {
		System.out.println("siin external range list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		List<SatelliteRange> list = orbitService.listRanges(satelliteId, groundStationId, start, hours);
		 
        return Resp.getInstantiationSuccessList("siin external range list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/range/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveRanges(@RequestBody JSONObject param) {
		System.out.println("siin external save range list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		List<SatelliteRange> list = orbitService.listRanges(satelliteId, groundStationId, start, hours);
		orbitService.insertBatchRange(list);
		
        return Resp.getInstantiationSuccessString("siin external save range list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/range/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listRangesByPage(@RequestBody JSONObject param) {
		System.out.println("siin external range list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listRangesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin external range list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/angle/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listAngles(@RequestBody JSONObject param) {
		System.out.println("siin external angle list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		List<SatelliteAngle> list = orbitService.listAngles(satelliteId, groundStationId, start, hours);
		 
        return Resp.getInstantiationSuccessList("siin external angle list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/angle/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveAngles(@RequestBody JSONObject param) {
		System.out.println("siin external save angle list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		List<SatelliteAngle> list = orbitService.listAngles(satelliteId, groundStationId, start, hours);
		orbitService.insertBatchAngle(list);
		
        return Resp.getInstantiationSuccessString("siin external save angle list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/angle/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listAnglesByPage(@RequestBody JSONObject param) {
		System.out.println("siin external angle list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listAnglesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin external angle list page", page);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/cover/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveCovers(@RequestBody JSONObject param) {
		System.out.println("siin external save cover list param:" + param);
		String satelliteId = param.getString("satelliteId");
		JSONArray groundStationIds = param.getJSONArray("groundStationIds");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(groundStationIds, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  groundStationIdList = JSONObject.parseArray(jsonString, String.class);
		for (String groundStationId : groundStationIdList) {
			if (!groundStationService.existGroundStation(groundStationId)) {
				return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
			}
		}
		
		
		List<SatelliteCover> list = orbitService.listCovers(satelliteId, groundStationIdList, start, hours);
		orbitService.insertBatchCover(list);
		
        return Resp.getInstantiationSuccessString("siin external save cover list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/cover/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listCoversByPage(@RequestBody JSONObject param) {
		System.out.println("siin external cover list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listCoversByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin external cover list page", page);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/pass/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSavePasses(@RequestBody JSONObject param) {
		System.out.println("siin external save pass list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		List<GroundStationPass> list = orbitService.listPasses(satelliteId, groundStationId, start, hours);
		orbitService.insertBatchPass(list);
		
        return Resp.getInstantiationSuccessString("siin external save pass list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/pass/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listPasses(@RequestBody JSONObject param) {
		System.out.println("siin external pass list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		List<GroundStationPass> list = orbitService.listPasses(satelliteId, groundStationId, start, hours);
		
        return Resp.getInstantiationSuccessList("siin external pass list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/pass/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listPassesByPage(@RequestBody JSONObject param) {
		System.out.println("siin external pass list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listPassesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin external pass list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/follow/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveFollows(@RequestBody JSONObject param) {
		System.out.println("siin external save follow list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		List<GroundStationFollow> list = orbitService.listFollows(satelliteId, groundStationId, start);
		orbitService.insertBatchFollow(list);
		
        return Resp.getInstantiationSuccessString("siin external save follow list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/follow/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFollowsByPage(@RequestBody JSONObject param) {
		System.out.println("siin external follow list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listFollowsByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin external follow list page", page);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/single/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveSingles(@RequestBody JSONObject param) {
		System.out.println("siin link single save cover list param:" + param);
		String satelliteId = param.getString("satelliteId");
		Double minPitch = param.getDouble("minPitch");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteSingle> list = orbitService.listSingles(satelliteId, minPitch, start, hours);
		orbitService.insertBatchSingle(list);
		
        return Resp.getInstantiationSuccessString("siin link single save cover list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/single/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSinglesByPage(@RequestBody JSONObject param) {
		System.out.println("siin link single cover list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listSinglesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin link single cover list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/single/groundstation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSingleWithGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin link single groundstation cover param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		Double minPitch = param.getDouble("minPitch");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		SatelliteMulti multi = orbitService.getSingleCover(satelliteId, groundStationId, minPitch, start, hours);
		 
        return Resp.getInstantiationSuccessJsonString("siin link single groundstation cover", multi);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/multi/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listMulti(@RequestBody JSONObject param) {
		System.out.println("siin link multi cover param:" + param);
		String satelliteId = param.getString("satelliteId");
		JSONArray groundStationIds = param.getJSONArray("groundStationIds");
		Double minPitch = param.getDouble("minPitch");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(groundStationIds, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  groundStationIdList = JSONObject.parseArray(jsonString, String.class);
		for (String groundStationId : groundStationIdList) {
			if (!groundStationService.existGroundStation(groundStationId)) {
				return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
			}
		}
		
		List<GroundStationMultiCoverVO> list = orbitService.listGroundStationMultiCover(satelliteId, groundStationIdList, minPitch, start, hours);
		 
        return Resp.getInstantiationSuccessList("siin link multi cover", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/multi/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveMultis(@RequestBody JSONObject param) {
		System.out.println("siin link multi save cover list param:" + param);
		JSONArray satelliteIds = param.getJSONArray("satelliteIds");
		String groundStationId = param.getString("groundStationId");
		Double minPitch = param.getDouble("minPitch");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(satelliteIds, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  satelliteIdList = JSONObject.parseArray(jsonString, String.class);
		for (String satelliteId : satelliteIdList) {	
		    if (!satelliteService.existSatellite(satelliteId)) {
			    return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		    }
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), false);
		}
		
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				List<SatelliteMulti> list = orbitService.listSatelliteSingleCover(satelliteIdList, groundStationId, minPitch, start, hours);
		        orbitService.insertBatchMulti(list);
			}
		});
		singleThreadExecutor.shutdown();
		
        return Resp.getInstantiationSuccessString("siin link multi save cover list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/multi/finished", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getMultiFinished(@RequestBody JSONObject param) {
		System.out.println("siin link multi finished param:" + param);
		
        boolean finished = GlobalAccess.linkCoverMultiFinished;
		
        return Resp.getInstantiationSuccessString("siin link multi finished", finished);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/multi/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listMultisByPage(@RequestBody JSONObject param) {
		System.out.println("siin link multi cover list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listMultisByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin link multi cover list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/region", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getRegion(@RequestBody JSONObject param) {
		System.out.println("siin link region cover param:" + param);
		String satelliteId = param.getString("satelliteId");
		double minLng = param.getDoubleValue("minLng");
		double maxLng = param.getDoubleValue("maxLng");
		double minLat = param.getDoubleValue("minLat");
		double maxLat = param.getDoubleValue("maxLat");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		SatelliteMulti multi = orbitService.getRegionCover(satelliteId, minLng, maxLng, minLat, maxLat, start, hours);
		 
        return Resp.getInstantiationSuccessJsonString("siin link region cover", multi);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/region/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveRegion(@RequestBody JSONObject param) {
		System.out.println("siin link region save param:" + param);
		String satelliteId = param.getString("satelliteId");
		double minLng = param.getDoubleValue("minLng");
		double maxLng = param.getDoubleValue("maxLng");
		double minLat = param.getDoubleValue("minLat");
		double maxLat = param.getDoubleValue("maxLat");
		String start = param.getString("start");
		Integer hours = param.getInteger("hours");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				List<SatelliteRegion> list = orbitService.listRegionCover(satelliteId, minLng, maxLng, minLat, maxLat, start, hours);
				orbitService.insertBatchRegion(list);
			}
		});
		singleThreadExecutor.shutdown();
		 
        return Resp.getInstantiationSuccessJsonString("siin link region save", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/region/finished", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getRegionFinished(@RequestBody JSONObject param) {
		System.out.println("siin link region finished param:" + param);
		
        boolean finished = GlobalAccess.linkCoverRegionFinished;
		
        return Resp.getInstantiationSuccessString("siin link region finished", finished);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/link/region/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listRegionsByPage(@RequestBody JSONObject param) {
		System.out.println("siin link region cover list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listRegionsByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin link region cover list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/max/list/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamMax(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam max list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime);
		orbitService.insertBatchWaveBeam(list);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam max list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/view/list/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamView(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam view list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		double viewAngle = param.getDoubleValue("viewAngle");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime, viewAngle);
		if (list == null) {
			return Resp.getInstantiationErrorString(String.format("视场角%f不合理(请确保小于最大地心角)", viewAngle), false);
		}
		orbitService.insertBatchWaveBeam(list);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam view list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/sway/list/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamSway(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam sway list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		double viewAngle = param.getDoubleValue("viewAngle");
		double swayAngle = param.getDoubleValue("swayAngle");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime, viewAngle, swayAngle);
		if (list == null) {
			return Resp.getInstantiationErrorString(String.format("视场角%f, 侧摆角%f不合理(请确保视场角的一半与侧摆角的和小于最大地心角)", viewAngle, swayAngle), false);
		}
		orbitService.insertBatchWaveBeam(list);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam sway list", list);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamsByPage(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listWaveBeamsByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessPage("siin wavebeam list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/max/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamingMax(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam max list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam max list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/view/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamingView(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam view list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		double viewAngle = param.getDoubleValue("viewAngle");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime, viewAngle);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam view list", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/wavebeam/sway/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listWaveBeamingSway(@RequestBody JSONObject param) {
		System.out.println("siin wavebeam sway list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String datetime = param.getString("datetime");
		double viewAngle = param.getDoubleValue("viewAngle");
		double swayAngle = param.getDoubleValue("swayAngle");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), false);
		}
		
		List<SatelliteWaveBeam> list = orbitService.listWaveBeam(satelliteId, datetime, viewAngle, swayAngle);
		 
        return Resp.getInstantiationSuccessList("siin wavebeam sway list", list);
	}
	
	

}
