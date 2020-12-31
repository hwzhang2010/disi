package com.hywx.siin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.PassPredictor;
import com.github.amsacode.predict4java.SatNotFoundException;
import com.github.amsacode.predict4java.SatPassTime;
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.common.Resp;
import com.hywx.siin.global.GlobalConstant;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.mapper.OrbitMapper;
import com.hywx.siin.mapper.SatelliteMapper;
import com.hywx.siin.orbit.tle.TLEBuilder;
import com.hywx.siin.po.SatelliteAngle;
import com.hywx.siin.po.SatelliteCover;
import com.hywx.siin.po.SatelliteRange;
import com.hywx.siin.po.GroundStationFollow;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.po.GroundStationPass;
import com.hywx.siin.po.SatelliteTle;
import com.hywx.siin.service.OrbitService;
import com.hywx.siin.vo.SatelliteCoverVO;

@Service("orbitService")
public class OrbitServiceImpl implements OrbitService {
	
	@Resource
	private OrbitMapper orbitMapper;
	@Resource
	private SatelliteMapper satelliteMapper;
	@Resource
	private GroundStationMapper groundStationMapper;
	
	@Override 
	public List<SatelliteTle> listTle() {
		return orbitMapper.listTle();
	}
	
	@Override
	public SatelliteTle getTle(String satelliteId) {
		
		return orbitMapper.getTle(satelliteId);
	}
	
	@Override
	public Boolean existTle(String satelliteId) {
		Boolean exist = orbitMapper.existTle(satelliteId);
		if (exist == null)
			return false;
		
		return exist;
	}

	@Override
	public int insertTle(String satelliteId, String tleLine0, String tleLine1, String tleLine2) {
		
		return orbitMapper.insertTle(satelliteId, tleLine0, tleLine1, tleLine2);
	}

	@Override
	public int deleteTle(String satelliteId) {
		
		return orbitMapper.deleteTle(satelliteId);
	}

	@Override
	public int updateTle(String tleLine0, String tleLine1, String tleLine2, String satelliteId) {
		
		return orbitMapper.updateTle(tleLine0, tleLine1, tleLine2, satelliteId);
	}
	
	@Override
	public boolean verifyTle(String line1, String line2) {
		com.hywx.siin.orbit.tle.TLE tle = new com.hywx.siin.orbit.tle.TLE("0000", line1, line2);
		if (tle.isLine1Valid() && tle.isLine2Valid())
			return true;
		
		return false;
	}
	
	@Override
	public boolean verifyTle(double t, double inclination, double raan, double eccentricity, double argumentOfPerigee, double meanAnomaly) {
		if (t <= 0)
			return false;
		
		// 每天运行的圈数
		double meanMotion = 1440 / t;
		
		com.hywx.siin.orbit.tle.TLE tle = TLEBuilder.newBuilder("0000")
				                                    .setSatelliteNumber(40930)
				                                    .setInternationalDesignator("15052A")
				                                    .setEpoch(1602663007000L)
				                                    .setElementSetNumber(999)
				                                    .setOrbitalElements(inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly)
				                                    .setRevolutions(6796)
				                                    .setMeanMotion(meanMotion)
				                                    .setFirstDerivativeMeanMotion(.00000876)
				                                    .setDragTerm(.000034425)
				                                    .setClassification('S')
				                                    .build();
		
		//System.out.println(tle);
		if (tle.isLine1Valid() && tle.isLine2Valid())
			return true;
		
		return false;
	}
		
	@Override
	public int updateTle(String satelliteId, double t, double inclination, double raan, double eccentricity, double argumentOfPerigee, double meanAnomaly) {
		if (t <= 0)
			return -2;
		
		// 每天运行的圈数
		double meanMotion = 1440 / t;
		
		com.hywx.siin.orbit.tle.TLE tle = TLEBuilder.newBuilder(satelliteId)
				                                    .setSatelliteNumber(40930)
				                                    .setInternationalDesignator("15052A")
				                                    .setEpoch(1602663007000L)
				                                    .setElementSetNumber(999)
				                                    .setOrbitalElements(inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly)
				                                    .setRevolutions(6796)
				                                    .setMeanMotion(meanMotion)
				                                    .setFirstDerivativeMeanMotion(.00000876)
				                                    .setDragTerm(.000034425)
				                                    .setClassification('S')
				                                    .build();
		
		return orbitMapper.updateTle(satelliteId, tle.getLine1(), tle.getLine2(), satelliteId);
	}
	
	@Override
    public String[] getTleByOrbitElem(double t, double inclination, double raan, double eccentricity, double argumentOfPerigee, double meanAnomaly) {
		if (t <= 0)
			return null;
		
		// 每天运行的圈数
		double meanMotion = 1440 / t;
		
		com.hywx.siin.orbit.tle.TLE tle = TLEBuilder.newBuilder("0000")
				                                    .setSatelliteNumber(40930)
				                                    .setInternationalDesignator("15052A")
				                                    .setEpoch(1602663007000L)
				                                    .setElementSetNumber(999)
				                                    .setOrbitalElements(inclination, raan, eccentricity, argumentOfPerigee, meanAnomaly)
				                                    .setRevolutions(6796)
				                                    .setMeanMotion(meanMotion)
				                                    .setFirstDerivativeMeanMotion(.00000876)
				                                    .setDragTerm(.000034425)
				                                    .setClassification('S')
				                                    .build();
		
		//System.out.println(tle);
		
		String[] lines = new String[3];
		lines[0] = tle.getTitle();
		lines[1] = tle.getLine1();
		lines[2] = tle.getLine2();
		
		return lines;
	}
	
	@Override
	public String[] getOrbitElemByTle(String line1, String line2) {
		com.hywx.siin.orbit.tle.TLE tle = new com.hywx.siin.orbit.tle.TLE("0000", line1, line2);
		
		String[] elems = new String[6];
		// 每天运行的圈数
		double meanMotion = tle.getMeanMotion();
		// 运行周期
		double t = 1440 / meanMotion;
		// 偏心率
		double eccentricity = tle.getEccentricity();
		// 轨道倾角
		double inclination = tle.getInclination();
		// 升交点赤经
		double raan = tle.getRaan();
		// 近地点幅角
		double argumentOfPerigee = tle.getArgumentOfPerigee();
		// 平近点角
		double meanAnomaly = tle.getMeanAnomaly();
		
		elems[0] = String.format("%.4f", t);
		elems[1] = String.format("%.6f", eccentricity);
		elems[2] = String.format("%.4f", inclination);
		elems[3] = String.format("%.4f", raan);
		elems[4] = String.format("%.4f", argumentOfPerigee);
		elems[5] = String.format("%.4f", meanAnomaly);
		
		return elems;
	}
	
	@Override
	public List<SatelliteRange> listRanges() {
		
		return orbitMapper.listRanges();
	}
	
	@Override
	public int deleteRange() {
		
		return orbitMapper.deleteRange();
	}

	@Override
	public int insertRange(SatelliteRange range) {
		
		return orbitMapper.insertRange(range);
	}

	@Override
	public int insertBatchRange(List<SatelliteRange> list) {
		
		//清空表数据
		orbitMapper.deleteRange();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchRange(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchRange(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
		
		return 0;
	}
	
	

	@Override
	public List<SatelliteRange> listRanges(String satelliteId, String groundStationId, String start, Integer hours) {
		List<SatelliteRange> list = new ArrayList<>();
		
		// 根据信关站ID得到信关站的地理位置坐标
		GroundStationInfo info = groundStationMapper.getGroundStationById(groundStationId);
		GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLng(), info.getGroundStationLat(), info.getGroundStationAlt());
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 开始时间
		    Date startDate = format.parse(start);
		    // 持续小时数, 最多7天
			hours = hours > 24 * 7 ? 24 * 7 : hours;
			// 时间差(秒)
			long seconds = hours * 60 * 60;
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的外测测距测速
			long timeStamp = startDate.getTime();
			for (int i = 0; i < seconds; i++) {
				Date date = new Date(timeStamp + i * 1000L);
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				SatelliteRange vo = new SatelliteRange(date, satPos.getRange(), satPos.getRangeRate());
				list.add(vo);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Page listRangesByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteRange> rangeList = orbitMapper.listRanges();
		PageInfo<SatelliteRange> pageInfo = new PageInfo<>(rangeList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(rangeList);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public List<SatelliteAngle> listAngles() {
		
		return orbitMapper.listAngles();
	}

	@Override
	public int deleteAngle() {
		
		return orbitMapper.deleteAngle();
	}

	@Override
	public int insertAngle(SatelliteAngle angle) {
		
		return orbitMapper.insertAngle(angle);
	}

	@Override
	public int insertBatchAngle(List<SatelliteAngle> list) {
		//清空表数据
		orbitMapper.deleteAngle();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchAngle(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchAngle(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
		
		return 0;
	}
	
	@Override
	public List<SatelliteAngle> listAngles(String satelliteId, String groundStationId, String start, Integer hours) {
        List<SatelliteAngle> list = new ArrayList<>();
		
		// 根据信关站ID得到信关站的地理位置坐标
		GroundStationInfo info = groundStationMapper.getGroundStationById(groundStationId);
		GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLng(), info.getGroundStationLat(), info.getGroundStationAlt());
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 开始时间
		    Date startDate = format.parse(start);
		    // 持续小时数, 最多7天
		 	hours = hours > 24 * 7 ? 24 * 7 : hours;
		 	// 时间差(秒)
		 	long seconds = hours * 60 * 60;
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的外测测角
			long timeStamp = startDate.getTime();
			for (int i = 0; i < seconds; i++) {
				Date date = new Date(timeStamp + i * 1000L);
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				SatelliteAngle vo = new SatelliteAngle(date, satPos.getAzimuth(), satPos.getElevation());
				list.add(vo);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Page listAnglesByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteAngle> angleList = orbitMapper.listAngles();
		PageInfo<SatelliteAngle> pageInfo = new PageInfo<>(angleList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}
	
	

	@Override
	public List<SatelliteCover> listCovers() {
		
		return orbitMapper.listCovers();
	}

	@Override
	public int deleteCover() {
		
		return orbitMapper.deleteCover();
	}

	@Override
	public int insertCover(SatelliteCover cover) {
		
		return orbitMapper.insertCover(cover);
	}

	@Override
	public int insertBatchCover(List<SatelliteCover> list) {
		//清空表数据
		orbitMapper.deleteCover();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchCover(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchCover(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
				
		return 0;
	}
	
	@Override
	public List<SatelliteCover> listCovers(String satelliteId, JSONArray groundStationIdArray, String start, Integer hours) {
		List<SatelliteCover> list = new ArrayList<>();
		
		//将json array数组转换成string
		String jsonString = JSONObject.toJSONString(groundStationIdArray, SerializerFeature.WriteClassName);
		//把string转换成list
		List<String>  groundStationIdList = JSONObject.parseArray(jsonString, String.class);
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 开始时间
		    Date startDate = format.parse(start);
		    // 持续小时数, 最多7天
		 	hours = hours > 24 * 7 ? 24 * 7 : hours;
		 	// 时间差(秒)
		 	long seconds = hours * 60 * 60;
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的覆盖范围
			long timeStamp = startDate.getTime();
			for (int i = 0; i < seconds; i++) {
				Date date = new Date(timeStamp + i * 1000L);
				
				StringBuilder groundStations = new StringBuilder("");
				if (groundStationIdList != null && !groundStationIdList.isEmpty()) {
					for (int j = 0; j < groundStationIdList.size(); j++) {
						GroundStationInfo info = groundStationMapper.getGroundStationById(groundStationIdList.get(j));
						GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLng(), info.getGroundStationLat(), info.getGroundStationAlt());
						SatPos satPos = satellite.getPosition(groundStationPosition, date);
						if (satPos.isAboveHorizon()) {
							groundStations.append(info.getGroundStationText()).append(",");
						}
					}
				}
				// 使用北京的地理坐标作为地面站进行计算卫星的位置
				GroundStationPosition groundStationPosition4Radius = new GroundStationPosition(116, 39, 0);
				SatPos satPos = satellite.getPosition(groundStationPosition4Radius, date);
				// 卫星的星下点位置
				double lng = satPos.getLongitude() * 180 / Math.PI;
				double lat = satPos.getLatitude() * 180 / Math.PI;
				double alt = satPos.getAltitude();
				// 卫星的覆盖半径
				double radius = satPos.getRangeCircleRadiusKm();
				
				SatelliteCover cover = new SatelliteCover(date, lng, lat, alt, radius, groundStations.toString());
				list.add(cover);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public Page listCoversByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteCover> coverList = orbitMapper.listCovers();
		PageInfo<SatelliteCover> pageInfo = new PageInfo<>(coverList);
		
		List<SatelliteCoverVO> voList = new ArrayList<>();
		List<SatelliteCover> tempList = pageInfo.getList();
		for (int i = 0; i < tempList.size(); i++) {
			voList.add(tempList.get(i).getVO());
		}
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(voList);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	
	@Override
	public List<GroundStationPass> listPasses() {
		
		return orbitMapper.listPasses();
	}

	@Override
	public int deletePass() {
		
		return orbitMapper.deletePass();
	}

	@Override
	public int insertPass(GroundStationPass pass) {
		
		return orbitMapper.insertPass(pass);
	}

	@Override
	public int insertBatchPass(List<GroundStationPass> list) {
		//清空表数据
		orbitMapper.deletePass();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchPass(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchPass(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
						
		return 0;
	}
	
	@Override
	public List<GroundStationPass> listPasses(String satelliteId, String groundStationId, String start, Integer hours) {
        List<GroundStationPass> list = new ArrayList<>();
        
        // 得到信关站信息
     	GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
     	// 实例化GroundStationPosition
     	GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLat(), groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationAlt());
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 开始时间
		    Date startDate = format.parse(start);
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			TLE tle = new TLE(satelliteTle.getTle());

			// 实例化PassPredictor
			PassPredictor predictor = new PassPredictor(tle, groundStationPosition);
			// 计算过境时间
			List<SatPassTime> satPassTimeList = predictor.getPasses(startDate, hours, false);
			for (int i = 0; i < satPassTimeList.size(); i++) {
				list.add(new GroundStationPass(satPassTimeList.get(i)));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SatNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Page listPassesByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<GroundStationPass> passList = orbitMapper.listPasses();
		PageInfo<GroundStationPass> pageInfo = new PageInfo<>(passList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	
	@Override
	public List<GroundStationFollow> listFollows() {
		
		return orbitMapper.listFollows();
	}

	@Override
	public int deleteFollow() {
		
		return orbitMapper.deleteFollow();
	}

	@Override
	public int insertFollow(GroundStationFollow follow) {
		
		return orbitMapper.insertFollow(follow);
	}

	@Override
	public int insertBatchFollow(List<GroundStationFollow> list) {
		//清空表数据
		orbitMapper.deleteFollow();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchFollow(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchFollow(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
						
		return 0;
	}

	@Override
	public List<GroundStationFollow> listFollows(String satelliteId, String groundStationId, String start) {
        List<GroundStationFollow> list = new ArrayList<>();
		
		// 得到信关站信息
		GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
		// 实例化GroundStationPosition
		GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLat(), groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationAlt());
		
		// 实例化TLE
		SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
		TLE tle = new TLE(satelliteTle.getTle());
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 开始时间
		    Date startDate = format.parse(start);
		    
			// 实例化PassPredictor
			PassPredictor predictor = new PassPredictor(tle, groundStationPosition);
			// 计算过境时间
			SatPassTime satPassTime = predictor.nextSatPass(startDate);
			// 过境开始时间和结束时间
			long startTime = satPassTime.getStartTime().getTime();
			long endTime = satPassTime.getEndTime().getTime();
			// 过境持续时间, 单位:ms
			long duration = endTime - startTime;
			for (int i = 0; i < duration; i += 1000) {
				Date passTime = new Date(startTime + i);
				SatPos satPos = predictor.getSatPos(passTime);
				
				GroundStationFollow vo = new GroundStationFollow(passTime, satPos.getAzimuth(), satPos.getElevation()); 
				list.add(vo);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();	
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SatNotFoundException e) {
			e.printStackTrace();
		} 
		
		return list;
	}

	@Override
	public Page listFollowsByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<GroundStationFollow> followList = orbitMapper.listFollows();
		PageInfo<GroundStationFollow> pageInfo = new PageInfo<>(followList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}
	

	

	

	

}
