package com.hywx.siin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hywx.siin.common.Page;
import com.hywx.siin.common.Resp;
import com.hywx.siin.po.GroundStationFollow;
import com.hywx.siin.po.GroundStationPass;
import com.hywx.siin.po.SatelliteAngle;
import com.hywx.siin.po.SatelliteCover;
import com.hywx.siin.po.SatelliteRange;
import com.hywx.siin.po.SatelliteTle;
import com.hywx.siin.service.GroundStationService;
import com.hywx.siin.service.OrbitService;
import com.hywx.siin.service.SatelliteService;

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
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), "false");
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
		 
        return Resp.getInstantiationSuccessList("siin external range list page", page);
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
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), "false");
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
		 
        return Resp.getInstantiationSuccessList("siin external angle list page", page);
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
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		
		List<SatelliteCover> list = orbitService.listCovers(satelliteId, groundStationIds, start, hours);
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
		 
        return Resp.getInstantiationSuccessList("siin external cover list page", page);
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
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), "false");
		}
		
		List<GroundStationPass> list = orbitService.listPasses(satelliteId, groundStationId, start, hours);
		orbitService.insertBatchPass(list);
		
        return Resp.getInstantiationSuccessString("siin external save pass list", "true");
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/pass/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listPassesByPage(@RequestBody JSONObject param) {
		System.out.println("siin external pass list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = orbitService.listPassesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin external pass list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/external/follow/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSaveFollows(@RequestBody JSONObject param) {
		System.out.println("siin external save follow list param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = param.getString("groundStationId");
		String start = param.getString("start");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (!groundStationService.existGroundStation(groundStationId)) {
			return Resp.getInstantiationErrorString(String.format("信关站ID%s不存在", groundStationId), "false");
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
		 
        return Resp.getInstantiationSuccessList("siin external follow list page", page);
	}
	
	
	
	
	
	
	

}
