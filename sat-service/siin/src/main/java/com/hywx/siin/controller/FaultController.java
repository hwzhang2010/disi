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
import com.hywx.siin.po.Device;
import com.hywx.siin.po.Fault;
import com.hywx.siin.service.FaultService;
import com.hywx.sisn.vo.FaultGroundStationVO;
import com.hywx.sisn.vo.FaultLevelVO;
import com.hywx.sisn.vo.FaultSatelliteVO;

@RestController
public class FaultController {
	@Autowired
	private FaultService faultService;
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/devices/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listDevicesByPage(@RequestBody JSONObject param) {
		System.out.println("siin device list page param:" + param);
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = faultService.listDevicesByPage(currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("siin device list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/main", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultMain(@RequestBody JSONObject param) {
		System.out.println("siin fault list main param:" + param);
		
		Integer id = param.getInteger("id");
		Integer mainId = faultService.getMainById(id);
		List<Fault> list = faultService.listFaultByMainId(mainId);
		 
        return Resp.getInstantiationSuccessList("siin fault list main", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/levels", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultLevels(@RequestBody JSONObject param) {
		System.out.println("siin fault list levels param:" + param);
		
		
		List<FaultLevelVO> list = faultService.listFaultLevels();
		 
        return Resp.getInstantiationSuccessList("siin fault list levels", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/satelliteids", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultSatelliteIds(@RequestBody JSONObject param) {
		System.out.println("siin fault list satelliteids param:" + param);

		List<String> list = faultService.listFaultSatelliteIds();
		 
        return Resp.getInstantiationSuccessList("siin fault list satelliteids", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/satellite/device", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultSatelliteDevice(@RequestBody JSONObject param) {
		System.out.println("siin fault list satellite device param:" + param);

		List<Device> list = faultService.listSatelliteDevices();
		 
        return Resp.getInstantiationSuccessList("siin fault list satellite device", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/satellites/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultSatellitesSend(@RequestBody JSONObject param) {
		System.out.println("siin fault satellites send param:" + param);
		//String satelliteId = param.getString("satelliteId");
		//String groundStationId = param.getString("groundStationId");
		
		//JSONArray -> List
		List<String> satelliteIdList = JSONObject.parseArray(param.getJSONArray("satelliteIds").toJSONString(), String.class);
		String groundStationId = "01110000";
		Integer id = 3;
		Integer subId = 1;
		Integer level = 2;
		
		Integer mainId = faultService.getMainById(id);
		Fault fault = faultService.getFaultById(mainId, subId);
		
		// 故障恢复，当未选的时候
		if (satelliteIdList.isEmpty()) {
			List<String> recoverList = faultService.listFaultSatelliteIds();
			Fault faultRecover = new Fault(0, "", 0, "");
			faultService.sendFaultSatellites(recoverList, groundStationId, 0, faultRecover, 0);
		} else {
		    faultService.sendFaultSatellites(satelliteIdList, groundStationId, id, fault, level);
		}
		 
        return Resp.getInstantiationSuccessString("siin fault satellites send", fault);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/faultrecover/satellites/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultSatellitesRecoverSend(@RequestBody JSONObject param) {
		System.out.println("siin fault satellites recover send param:" + param);
		//String satelliteId = param.getString("satelliteId");
		
		//JSONArray -> List
		List<String> satelliteIdList = JSONObject.parseArray(param.getJSONArray("satelliteIds").toJSONString(), String.class);
		String groundStationId = "01110000";
		Integer id = 3;
		
		Fault faultRecover = new Fault(0, "", 0, "");
		
		faultService.sendFaultSatellites(satelliteIdList, groundStationId, id, faultRecover, 0);
		 
        return Resp.getInstantiationSuccessString("siin fault satellites recover send", faultRecover);
	}
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/satellite/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultSatelliteSend(@RequestBody JSONObject param) {
		System.out.println("siin fault satellite send param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = "01110000";
		Integer id = param.getInteger("id");
		Integer subId = 2;
		Integer level = param.getInteger("level");
		
		Integer mainId = faultService.getMainById(id);
		Fault fault = faultService.getFaultById(mainId, subId);
		
		faultService.sendFaultSatellite(satelliteId, groundStationId, id, fault, level);
		 
        return Resp.getInstantiationSuccessString("siin fault satellite send", fault);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/faultrecover/satellite/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultSatelliteRecoverSend(@RequestBody JSONObject param) {
		System.out.println("siin fault satellite recover send param:" + param);
		String satelliteId = param.getString("satelliteId");
		String groundStationId = "01110000";
		Integer id = 3;
		
		Fault faultRecover = new Fault(0, "", 0, "");
		
		faultService.sendFaultSatellite(satelliteId, groundStationId, id, faultRecover, 0);
		 
        return Resp.getInstantiationSuccessString("siin fault satellite recover send", faultRecover);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/satellites", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultSatellites(@RequestBody JSONObject param) {
		System.out.println("siin fault list satellites param:" + param);

		List<FaultSatelliteVO> list = faultService.listFaultSatellites();
		 
        return Resp.getInstantiationSuccessList("siin fault list satellites", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/satellites/query", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultSatellite(@RequestBody JSONObject param) {
		System.out.println("siin fault list satellites query param:" + param);
		String satelliteId = param.getString("satelliteId");
		//Integer level = param.getInteger("level");
		String levelString = param.getString("level");
        
		List<FaultSatelliteVO> list = null;
		if (satelliteId.isEmpty() && levelString.isEmpty()) {
		    list = faultService.listFaultSatellites();
		}
		if (!satelliteId.isEmpty() && !levelString.isEmpty()) {
			Integer level = Integer.parseInt(levelString);
			list = faultService.listFaultSatellitesByIdLevel(satelliteId, level);
		}
		if (!satelliteId.isEmpty()) {
			if (levelString.isEmpty()) {
				list = faultService.listFaultSatellitesById(satelliteId);
			}
		} else {
			if (!levelString.isEmpty()) {
				Integer level = Integer.parseInt(levelString);
				list = faultService.listFaultSatellitesByLevel(level);
			}
		}
		 
        return Resp.getInstantiationSuccessList("siin fault list satellites query", list);
	}
	
	
	
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/groundstationids", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultGroundStationIds(@RequestBody JSONObject param) {
		System.out.println("siin fault list groundstationids param:" + param);

		List<String> list = faultService.listFaultGroundStationIds();
		 
        return Resp.getInstantiationSuccessList("siin fault list groundstationids", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/groundstation/device", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultGroundStationDevice(@RequestBody JSONObject param) {
		System.out.println("siin fault list groundstation device param:" + param);

		List<Device> list = faultService.listGroundStationDevices();
		 
        return Resp.getInstantiationSuccessList("siin fault list groundstation device", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/groundstations/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultGroundStationsSend(@RequestBody JSONObject param) {
        System.out.println("siin fault groundstations send param:" + param);
		
		//JSONArray -> List
		List<String> groundStationIdList = JSONObject.parseArray(param.getJSONArray("groundStationIds").toJSONString(), String.class);
		String satelliteId = "0101";
		Integer id = 33;
		Integer subId = 1;
		//Integer level = param.getInteger("level");
		Integer level = 2;
		
		Integer mainId = faultService.getMainById(id);
		Fault fault = faultService.getFaultById(mainId, subId);
		
		// 故障恢复，当未选的时候
		if (groundStationIdList.isEmpty()) {
			List<String> recoverList = faultService.listFaultGroundStationIds();
			Fault faultRecover = new Fault(0, "", 0, "");
			faultService.sendFaultGroundStations(satelliteId, recoverList, 0, faultRecover, level);
		} else {
		    faultService.sendFaultGroundStations(satelliteId, groundStationIdList, id, fault, level);
		}
		 
        return Resp.getInstantiationSuccessString("siin fault groundstations send", fault);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/faultrecover/groundstations/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultGroundStationsRecoverSend(@RequestBody JSONObject param) {
		System.out.println("siin fault groundstations recover send param:" + param);
		
		//JSONArray -> List
		List<String> groundStationIdList = JSONObject.parseArray(param.getJSONArray("groundStationIds").toJSONString(), String.class);	
		String satelliteId = "0101";
		Integer id = 33;
		Fault faultRecover = new Fault(0, "", 0, "");
		
		faultService.sendFaultGroundStations(satelliteId, groundStationIdList, id, faultRecover, 0);
		 
        return Resp.getInstantiationSuccessString("siin fault groundstations recover send", faultRecover);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/groundstation/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultGroundStationSend(@RequestBody JSONObject param) {
		System.out.println("siin fault groundstation send param:" + param);
		
		String groundStationId = param.getString("groundStationId");
		String satelliteId = "0101";
		Integer id = param.getInteger("id");
		Integer subId = 2;
		Integer level = param.getInteger("level");
		
		Integer mainId = faultService.getMainById(id);
		Fault fault = faultService.getFaultById(mainId, subId);
		
		faultService.sendFaultGroundStation(satelliteId, groundStationId, id, fault, level);
		 
        return Resp.getInstantiationSuccessString("siin fault groundstation send", fault);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/faultrecover/groundstation/send", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getFaultGroundStationRecoverSend(@RequestBody JSONObject param) {
		System.out.println("siin fault groundstation recover send param:" + param);
		
		String groundStationId = param.getString("groundStationId");
		String satelliteId = "0101";
		Integer id = 33;
		
		Fault faultRecover = new Fault(0, "", 0, "");
		
		faultService.sendFaultGroundStation(satelliteId, groundStationId, id, faultRecover, 0);
		 
        return Resp.getInstantiationSuccessString("siin fault groundstation recover send", faultRecover);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/groundstations", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultGroundStation(@RequestBody JSONObject param) {
		System.out.println("siin fault list groundstation param:" + param);

		List<FaultGroundStationVO> list = faultService.listFaultGroundStations();
		 
        return Resp.getInstantiationSuccessList("siin fault list groundstation", list);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/siin/fault/list/groundstations/query", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listFaultGroundStationsById(@RequestBody JSONObject param) {
		System.out.println("siin fault list groundstations query param:" + param);
		String groundStationId = param.getString("groundStationId");
		//Integer level = param.getInteger("level");
		String levelString = param.getString("level");

		List<FaultGroundStationVO> list = null;
		if (groundStationId.isEmpty() && levelString.isEmpty())
			list = faultService.listFaultGroundStations();
		if (!groundStationId.isEmpty() && !levelString.isEmpty()) {
			Integer level = Integer.parseInt(levelString);
			list = faultService.listFaultGroundStationsByIdLevel(groundStationId, level);
		}
		if (groundStationId.isEmpty()) {
			if (!levelString.isEmpty()) {
				Integer level = Integer.parseInt(levelString);
				list = faultService.listFaultGroundStationsByLevel(level);
			}
		} else {
			if (levelString.isEmpty()) {
				list = faultService.listFaultGroundStationsById(groundStationId);
			}
		}
		
		 
        return Resp.getInstantiationSuccessList("siin fault list groundstations query", list);
	}
	

}
