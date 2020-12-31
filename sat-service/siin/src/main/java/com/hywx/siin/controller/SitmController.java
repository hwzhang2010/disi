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
import com.hywx.siin.po.TmRsltFrame;
import com.hywx.siin.service.SatelliteService;
import com.hywx.siin.service.TmService;
import com.hywx.sitm.vo.SitmSatelliteRunningVO;
import com.hywx.sitm.vo.SitmSatelliteVO;
import com.hywx.sitm.vo.TmRsltFrameVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "遥测controller", tags = {"遥测操作接口"})
@RestController
public class SitmController {
	
	@Autowired
	private TmService tmService;
	@Autowired
	private SatelliteService satelliteService; 
	
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星正在进行的遥测源码仿真信息", notes = "根据卫星ID获取该卫星正在进行的遥测源码仿真信息, 如果该卫星未发送遥测源码, 则返回null")
	@RequestMapping(value = "api/v1/sitm", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSitm(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("sitm param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		boolean isRunning = tmService.getSatelliteIsRunning(satelliteId);
		if (isRunning) {
			List<TmRsltFrameVO> list = tmService.listSitms(satelliteId); 
	        return Resp.getInstantiationSuccessList("sitm success", list);
		}
		 
        return Resp.getInstantiationErrorList("sitm error", null);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的遥测源码仿真状态", notes = "根据卫星ID获取该卫星的遥测源码仿真状态, 包括是否在进行遥测源码仿真, 自动发送还是外测驱动")
	@RequestMapping(value = "api/v1/sitm/satellite", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSitmSatellite(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("sitm satellite param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		SitmSatelliteVO vo = tmService.getSatellite(satelliteId);
		 
        return Resp.getInstantiationSuccessJsonString("sitm satellite success", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星是否正在进行遥测源码仿真", notes = "根据卫星ID获取该卫星是否正在进行遥测源码仿真")
	@RequestMapping(value = "api/v1/sitm/satellite/isrunning", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSitmSatelliteIsRunning(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("sitm satellite isrunning param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		boolean isRunning = tmService.getSatelliteIsRunning(satelliteId);
	
		return Resp.getInstantiationSuccessString("sitm satellite isrunning success", isRunning);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "获取所有正在进行遥测源码仿真的卫星数目和仿真发送方式(自动发送/外测驱动)", notes = "获取所有正在进行遥测源码仿真的卫星数目和仿真发送方式(自动发送/外测驱动)")
	@RequestMapping(value = "api/v1/sitm/satellite/running", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSitmSatelliteRunning(@ApiParam(value = "", required = false) @RequestBody JSONObject param) {
		System.out.println("sitm satellite running param:" + param);
		
		SitmSatelliteRunningVO vo = tmService.getSatelliteRunning();
	
		return Resp.getInstantiationSuccessString("sitm satellite running success", vo);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID和遥测源码仿真发送方式(自动发送/外测驱动)改变该卫星的遥测源码仿真状态", notes = "根据卫星ID和遥测源码仿真发送方式(自动发送/外测驱动)改变该卫星的遥测源码仿真状态: 运行->停止;停止->运行")
	@RequestMapping(value = "api/v1/sitm/satellite/run", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSitmSatelliteRun(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为satelliteId, value类型为String, 表示卫星ID; "
                                                 + "2.key为sendType, value类型为String, 表示遥测源码仿真发送方式(自动发送/外测驱动); "
                                                 + "3.key为isRun, value类型为Boolean, 表示遥测源码仿真状态(运行/停止)", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite run param:" + param);
		String satelliteId = param.getString("satelliteId");
		String sendType = param.getString("sendType");
		boolean isRun = param.getBoolean("isRun");
		
		tmService.updateSatelliteRun(satelliteId, sendType, isRun);
	
		return Resp.getInstantiationSuccessString("sitm satellite run success", null);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "批量更新(运行)一组卫星的遥测源码仿真状态", notes = "批量更新(运行)一组卫星的遥测源码仿真状态: 停止->运行")
	@RequestMapping(value = "api/v1/sitm/satellite/run/batch", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp udpateSitmSatelliteRunBatch(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为sendType, value类型为String, 表示遥测源码仿真发送方式(自动发送/外测驱动); "
                                                + "2.key为satelliteIdList, value类型为JSON数组, 表示要运行的一组卫星", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite run batch param:" + param);
		String sendType = param.getString("sendType");
		
	    //JSONArray -> List
		List<String> satelliteIdList = JSONObject.parseArray(param.getJSONArray("satelliteIdList").toJSONString(), String.class);
		for (String string : satelliteIdList) {
			System.out.println("***********" + string);
		}
		
		tmService.updateSatelliteRunBatch(satelliteIdList, sendType);
	
		return Resp.getInstantiationSuccessString("sitm satellite run batch success", null);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "更新全部卫星的遥测源码仿真状态", notes = "更新全部卫星的遥测源码仿真状态: 停止->运行;运行->停止")
	@RequestMapping(value = "api/v1/sitm/satellite/run/all", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp udpateSitmSatelliteRunAll(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为sendType, value类型为String, 表示遥测源码仿真发送方式(自动发送/外测驱动); "
                                                + "2.key为isRun, value类型为Boolean, 表示遥测源码仿真状态(运行/停止)", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite run all param:" + param);
		String sendType = param.getString("sendType");
		boolean isRun = param.getBoolean("isRun");
		
		tmService.updateSatelliteRunAll(sendType, isRun);
	
		return Resp.getInstantiationSuccessString("sitm satellite run all success", null);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID改变该卫星的遥测源码仿真发送方式(自动发送/外测驱动)", notes = "根据卫星ID改变该卫星的遥测源码仿真发送方式(自动发送/外测驱动): 自动发送->外测驱动;外测驱动->自动发送")
	@RequestMapping(value = "api/v1/sitm/satellite/sendtype", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSitmSatelliteSendType(
			@ApiParam(value = "JSON对象, 包括2个属性, 1.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "2.key为sendType, value类型为String, 表示遥测源码仿真发送方式(自动发送/外测驱动); ", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite run param:" + param);
		String satelliteId = param.getString("satelliteId");
		String sendType = param.getString("sendType");
		
		boolean isUpdate = tmService.updateSatelliteSendType(satelliteId, sendType);
        if (isUpdate)
		    return Resp.getInstantiationSuccessString("sitm satellite sendtype", isUpdate);
        return Resp.getInstantiationErrorString("sitm satellite sendtype", isUpdate);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID, 遥测参数ID和遥测参数仿真类型改变该卫星的遥测参数仿真类型(常数/递增/随机/正弦)", notes = "根据卫星ID, 遥测参数ID和遥测参数仿真类型改变该卫星的遥测参数仿真类型(常数/递增/随机/正弦)")
	@RequestMapping(value = "api/v1/sitm/satellite/paramtype", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSitmSatelliteParamType(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "2.key为paramId, value类型为Integer, 表示遥测源码仿真参数ID; "
                                                + "3.key为paramType, value类型为String, 表示遥测源码仿真参数类型(常数/递增/随机/正弦)", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite paramtype param:" + param);
		String satelliteId = param.getString("satelliteId");
		int paramId = param.getInteger("paramId");
		String paramType = param.getString("paramType");
		
		boolean isUpdate = tmService.updateSatelliteParamType(satelliteId, paramId, paramType);
        if (isUpdate)
		    return Resp.getInstantiationSuccessString("sitm satellite paramtype", isUpdate);
        return Resp.getInstantiationErrorString("sitm satellite paramtype", isUpdate);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID, 遥测参数ID和遥测参数仿真系数改变该卫星的遥测参数仿真系数", notes = "根据卫星ID, 遥测参数ID和遥测参数仿真系数改变该卫星的遥测参数仿真系数")
	@RequestMapping(value = "api/v1/sitm/satellite/coefficient", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSitmSatelliteCoefficient(
			@ApiParam(value = "JSON对象, 包括3个属性, 1.key为satelliteId, value类型为String, 表示卫星ID; "
                                                + "2.key为paramId, value类型为Integer, 表示遥测源码仿真参数ID; "
                                                + "3.key为coefficient, value类型为Double, 表示遥测源码仿真参数系数", 
                      required = true)
			@RequestBody JSONObject param) {
		System.out.println("sitm satellite coefficient param:" + param);
		String satelliteId = param.getString("satelliteId");
		int paramId = param.getInteger("paramId");
		double coefficient = param.getDouble("coefficient");
		
		boolean isUpdate = tmService.updateSatelliteCoefficient(satelliteId, paramId, coefficient);
        if (isUpdate)
		    return Resp.getInstantiationSuccessString("sitm satellite coefficient", isUpdate);
        return Resp.getInstantiationErrorString("sitm satellite coefficient", isUpdate);
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID获取该卫星的遥测源码仿真计数", notes = "根据卫星ID获取该卫星的遥测源码仿真计数")
	@RequestMapping(value = "api/v1/sitm/satellite/sendcount", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp getSatellietSendCount(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("sitm satellite sendcount param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		long count = tmService.getSatelliteSendCount(satelliteId);
		
        return Resp.getInstantiationSuccessString("sitm satellite sendcount", String.valueOf(count));
	}
	
	@CrossOrigin  //跨域访问
	@ApiOperation(value = "根据卫星ID重置该卫星的遥测源码仿真计数为0", notes = "根据卫星ID重置该卫星的遥测源码仿真计数为0")
	@RequestMapping(value = "api/v1/sitm/satellite/sendcount/zero", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSatellietSendCountZero(@ApiParam(value = "JSON对象, 包括1个属性, key为satelliteId, value类型为String, 表示卫星ID", required = true) @RequestBody JSONObject param) {
		System.out.println("sitm satellite sendcount zero param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		tmService.updateSatelliteSendCountZero(satelliteId);
		
        return Resp.getInstantiationSuccessString("sitm satellite sendcount zero", "0");
	}
	
	
	/**************************2020.12.18********************************************************************/
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitm/code/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSitmCodesByPage(@RequestBody JSONObject param) {
		System.out.println("sitm code list page param:" + param);
		String satelliteId = param.getString("satelliteId");
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		boolean isRunning = tmService.getSatelliteIsRunning(satelliteId);
		if (isRunning) {
			Page page = tmService.listSitmsByPage(satelliteId, currentPage, pageSize); 
	        return Resp.getInstantiationSuccessList("sitm code list page success", page);
		}
		 
        return Resp.getInstantiationErrorList("sitm code list page error", null);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitm/rslt/list/page", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp listSitmRstlFrame(@RequestBody JSONObject param) {
		System.out.println("sitm rslt frame list page param:" + param);
		String satelliteId = param.getString("satelliteId");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		
		Integer currentPage = param.getInteger("currentPage");
		Integer pageSize = param.getInteger("pageSize");
		
		Page page = tmService.listTmRstlFramesByPage(satelliteId, currentPage, pageSize);
		 
        return Resp.getInstantiationSuccessList("sitm rslt frame list page", page);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitm/rslt/insert", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Resp insertSitmRstlFrame(@RequestBody JSONObject param) {
		System.out.println("sitm rslt frame insert param:" + param);
		String satelliteId = param.getString("satelliteId");
		TmRsltFrame rslt = param.getObject("rslt", TmRsltFrame.class);
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (tmService.existTmRsltTable2(satelliteId) < 1) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的遥测参数表不存在", satelliteId), "false");
		}
		
		int result = tmService.insertTmRslt(satelliteId, rslt);
		 
        return Resp.getInstantiationSuccessString("sitm rslt frame insert", result);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitm/rslt/delete", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public Resp deleteSitmRstlFrame(@RequestBody JSONObject param) {
		System.out.println("sitm rslt frame delete param:" + param);
		String satelliteId = param.getString("satelliteId");
		int id = param.getIntValue("id");
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (tmService.existTmRsltTable2(satelliteId) < 1) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的遥测参数表不存在", satelliteId), "false");
		}
		
		int result = tmService.deleteTmRslt2(satelliteId, id);
		 
        return Resp.getInstantiationSuccessString("sitm rslt frame delete", result);
	}
	
	@CrossOrigin  //跨域访问
	@RequestMapping(value = "api/v1/sitm/rslt/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Resp updateSitmRstlFrame(@RequestBody JSONObject param) {
		System.out.println("sitm rslt frame update param:" + param);
		String satelliteId = param.getString("satelliteId");
		TmRsltFrame rslt = param.getObject("row", TmRsltFrame.class);
		
		if (!satelliteService.existSatellite(satelliteId)) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s不存在", satelliteId), "false");
		}
		if (tmService.existTmRsltTable2(satelliteId) < 1) {
			return Resp.getInstantiationErrorString(String.format("卫星ID%s的遥测参数表不存在", satelliteId), "false");
		}
		
		int result = tmService.updateTmRslt(satelliteId, rslt);
		 
        return Resp.getInstantiationSuccessString("sitm rslt frame update", result);
	}
	
	

}
