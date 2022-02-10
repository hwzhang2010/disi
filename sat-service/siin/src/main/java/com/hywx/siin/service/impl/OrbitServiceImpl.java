package com.hywx.siin.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.PassPredictor;
import com.github.amsacode.predict4java.Position;
import com.github.amsacode.predict4java.SatNotFoundException;
import com.github.amsacode.predict4java.SatPassTime;
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.global.GlobalAccess;
import com.hywx.siin.global.GlobalConstant;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.mapper.OrbitMapper;
import com.hywx.siin.mapper.SatelliteMapper;
import com.hywx.siin.orbit.tle.TLEBuilder;
import com.hywx.siin.po.SatelliteAngle;
import com.hywx.siin.po.SatelliteCover;
import com.hywx.siin.po.SatelliteRange;
import com.hywx.siin.po.SatelliteRegion;
import com.hywx.siin.po.SatelliteSingle;
import com.hywx.siin.po.SatelliteMulti;
import com.hywx.siin.po.GroundStationFollow;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.po.GroundStationPass;
import com.hywx.siin.po.SatelliteTle;
import com.hywx.siin.po.SatelliteWaveBeam;
import com.hywx.siin.service.OrbitService;
import com.hywx.siin.util.GisUtil;
import com.hywx.siin.vo.GroundStationMultiCoverVO;

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
	public Page listTleByPage(Integer currentPage, int pageSize) {
        Page page = new Page();
		
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteTle> tleList = orbitMapper.listTle();
		PageInfo<SatelliteTle> pageInfo = new PageInfo<>(tleList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(tleList);
		page.setTotal(pageInfo.getTotal());
		
		return page;
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
		
		return count;
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
	public Page listRangesByPage(Integer currentPage, Integer pageSize) {
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
		
		return count;
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
	public Page listAnglesByPage(Integer currentPage, Integer pageSize) {
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
				
		return count;
	}
	
	@Override
	public List<SatelliteCover> listCovers(String satelliteId, List<String> groundStationIdList, String start, Integer hours) {
		List<SatelliteCover> list = new ArrayList<>();
		
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
	public Page listCoversByPage(Integer currentPage, Integer pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteCover> coverList = orbitMapper.listCovers();
		PageInfo<SatelliteCover> pageInfo = new PageInfo<>(coverList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(coverList);
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
						
		return count;
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
	public Page listPassesByPage(Integer currentPage, Integer pageSize) {
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
						
		return count;
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
	public Page listFollowsByPage(Integer currentPage, Integer pageSize) {
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

	@Override
	public List<SatelliteSingle> listSingles() {
		
		return orbitMapper.listSingles();
	}

	@Override
	public int deleteSingle() {
		
		return orbitMapper.deleteSingle();
	}

	@Override
	public int insertSingle(SatelliteSingle single) {
		
		return orbitMapper.insertSingle(single);
	}

	@Override
	public int insertBatchSingle(List<SatelliteSingle> list) {
		//清空表数据
		orbitMapper.deleteSingle();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchSingle(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchSingle(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));

		return count;
	}

	@Override
	public List<SatelliteSingle> listSingles(String satelliteId, Double minPitch, String start, Integer hours) {
		List<SatelliteSingle> list = new ArrayList<>();
		
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
				
				// 使用北京的地理坐标作为地面站进行计算卫星的位置
				GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				// 卫星的星下点位置
				double lng = satPos.getLongitude() * 180 / Math.PI;
				double lat = satPos.getLatitude() * 180 / Math.PI;
				double alt = satPos.getAltitude();
				// 星下视角
				double subAngle = Math.asin( GlobalConstant.EARTH_RADIUS_KM / (alt + GlobalConstant.EARTH_RADIUS_KM) * Math.cos(minPitch * Math.PI / 180.0) );
				// 地心角
				double earthAngle = Math.acos( GlobalConstant.EARTH_RADIUS_KM / (alt + GlobalConstant.EARTH_RADIUS_KM) * Math.cos(minPitch * Math.PI / 180.0) ) * 180 / Math.PI - minPitch;
				// 覆盖面积
				double coverArea = 4 * Math.PI * Math.pow(GlobalConstant.EARTH_RADIUS_KM, 2) * Math.sin(earthAngle / 2.0) * Math.sin(earthAngle / 2.0);
				
				SatelliteSingle single = new SatelliteSingle(date, lng, lat, alt, subAngle, earthAngle, coverArea);
				list.add(single);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Page listSinglesByPage(Integer currentPage, Integer pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteSingle> singleList = orbitMapper.listSingles();
		PageInfo<SatelliteSingle> pageInfo = new PageInfo<>(singleList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(singleList);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public SatelliteMulti getSingleCover(String satelliteId, String groundStationId, Double minPitch, String start, Integer hours) {
		SatelliteMulti vo = new SatelliteMulti(satelliteId);
		
		int count = 0;
		double duration = 0.0;
		boolean inCover = false;
		
		// 得到信关站信息
		GroundStationInfo groundStationInfo = groundStationMapper.getGroundStationById(groundStationId);
		
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
				
				// 使用给定的地面站进行计算卫星的位置
				GroundStationPosition groundStationPosition = new GroundStationPosition(groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationLat(), 0);
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				// 卫星的星下点位置
				double lng = satPos.getLongitude() * 180 / Math.PI;
				double lat = satPos.getLatitude() * 180 / Math.PI;
				double alt = satPos.getAltitude();
				// 星下视角
				//double subAngle = Math.asin( GlobalConstant.EARTH_RADIUS_KM / (alt + GlobalConstant.EARTH_RADIUS_KM) * Math.cos(minPitch * Math.PI / 180.0) );
				// 地心角
				double earthAngle = Math.acos( GlobalConstant.EARTH_RADIUS_KM / (alt + GlobalConstant.EARTH_RADIUS_KM) * Math.cos(minPitch * Math.PI / 180.0) ) * 180 / Math.PI - minPitch;
				// 覆盖面积
				//double coverArea = 4 * Math.PI * Math.pow(GlobalConstant.EARTH_RADIUS_KM, 2) * Math.sin(earthAngle / 2.0) * Math.sin(earthAngle / 2.0);
				// 星下点位置与地心角的弧长
				double radius = GlobalConstant.EARTH_RADIUS_KM * earthAngle * Math.PI / 180.0;
				// 星下点位置与信关站的距离
				double distance = GisUtil.getDistanceFromPosition(lng, lat, groundStationInfo.getGroundStationLng(), groundStationInfo.getGroundStationLat());
				if (distance < radius) {
					if (!inCover) {
						count++;
					}
					inCover = true;
					// 过境时间+1
					duration++;
				} else {
					inCover = false;
				}	
			}
			if (!inCover && count > 0) {
				count++;
			}
			
			vo.setCount(count);
			vo.setDuration(duration / 60.0);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return vo;
	}
	
	@Override
	public List<SatelliteMulti> listSatelliteSingleCover(List<String> satelliteIdList, String groundStationId, Double minPitch, String start, Integer hours) {
		List<SatelliteMulti> list = new ArrayList<>();
		
		// 计算开始前标识置为false
		GlobalAccess.linkCoverMultiFinished = false;
		for (int i = 0; i < satelliteIdList.size(); i++) {
			String satelliteId = satelliteIdList.get(i);
			SatelliteMulti vo = getSingleCover(satelliteId, groundStationId, minPitch, start, hours);
			if (vo.getCount() > 0)
			    list.add(vo);
		}
		// 计算完成后标识置为true
		GlobalAccess.linkCoverMultiFinished = true;
		
		return list;
	}

	@Override
	public List<GroundStationMultiCoverVO> listGroundStationMultiCover(String satelliteId, List<String> groundStationIdList, Double minPitch, String start, Integer hours) {
		List<GroundStationMultiCoverVO> voList = new ArrayList<>();	
		
		for (int i = 0; i < groundStationIdList.size(); i++) {
			String groundStationId = groundStationIdList.get(i);
			GroundStationInfo info = groundStationMapper.getGroundStationById(groundStationId);
			SatelliteMulti vo = getSingleCover(satelliteId, groundStationId, minPitch, start, hours);
		    GroundStationMultiCoverVO coverVO = new GroundStationMultiCoverVO(info.getGroundStationText(), vo);
		    voList.add(coverVO);
		}
		
		return voList;
	}

	@Override
	public List<SatelliteMulti> listMultis() {
		
		return orbitMapper.listMultis();
	}

	@Override
	public int deleteMulti() {
		
		return orbitMapper.deleteMulti();
	}

	@Override
	public int insertMulti(SatelliteMulti single) {
		
		return orbitMapper.insertMulti(single);
	}

	@Override
	public int insertBatchMulti(List<SatelliteMulti> list) {
		//清空表数据
		orbitMapper.deleteMulti();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchMulti(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchMulti(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
		
		return count;
	}

	@Override
	public Page listMultisByPage(Integer currentPage, Integer pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteMulti> list = orbitMapper.listMultis();
		PageInfo<SatelliteMulti> pageInfo = new PageInfo<>(list);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(list);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}
	
	@Override
	public List<SatelliteRegion> listRegions() {
		
		return orbitMapper.listRegions();
	}

	@Override
	public int deleteRegion() {
		
		return orbitMapper.deleteRegion();
	}

	@Override
	public int insertRegion(SatelliteRegion region) {
		
		return orbitMapper.insertRegion(region);
	}

	@Override
	public int insertBatchRegion(List<SatelliteRegion> list) {
		//清空表数据
		orbitMapper.deleteRegion();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchRegion(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchRegion(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
		
		return count;
	}

	@Override
	public SatelliteMulti getRegionCover(String satelliteId, double minLng, double maxLng, double minLat, double maxLat, String start, Integer hours) {
		SatelliteMulti multi = new SatelliteMulti(satelliteId);
		
        int count = 0;
		double duration = 0.0;
		boolean inCover = false;
		// 交换值的顺序
		if (minLng > maxLng) {
			minLng = minLng + maxLng;
			maxLng = minLng - maxLng;
			minLng = minLng - maxLng;
		}
		if (minLat > maxLat) {
			minLat = minLat + maxLat;
			maxLat = minLat - maxLat;
			minLat = minLat - maxLat;
		}
		
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		
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
				
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				// 卫星的星下点位置
				double lng = satPos.getLongitude() * 180 / Math.PI;
				double lat = satPos.getLatitude() * 180 / Math.PI;
				double radius = satPos.getRangeCircleRadiusKm();
				
				// 网格分割，遍历所有的网格点
				boolean gridCover = false;
				for (double u = minLng; u < maxLng; u++) {
					for (double v = minLat ; v < maxLat; v++) {
						// 星下点位置与网格点的距离
						double distance = GisUtil.getDistanceFromPosition(lng, lat, u, v);
						if (distance < radius) {
							gridCover = true;
							break;
						}
					}
					if (gridCover)
						break;
				}
				
				if (gridCover) {
					if (!inCover) {
						count++;
					}
					inCover = true;
					// 过境时间+1
					duration++;
				} else {
					inCover = false;
				}
			}
			if (!inCover && count > 0) {
				count++;
			}
			
			multi.setCount(count);
			multi.setDuration(duration / 60.0);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return multi;
	}
	
	@Override
	public List<SatelliteRegion> listRegionCover(String satelliteId, double minLng, double maxLng, double minLat, double maxLat, String start, Integer hours) {
		List<SatelliteRegion> list = new ArrayList<>();
		
		// 计算开始前标识置为false
		GlobalAccess.linkCoverRegionFinished = false;
		
		// 交换值的顺序
		if (minLng > maxLng) {
			minLng = minLng + maxLng;
			maxLng = minLng - maxLng;
			minLng = minLng - maxLng;
		}
		if (minLat > maxLat) {
			minLat = minLat + maxLat;
			maxLat = minLat - maxLat;
			minLat = minLat - maxLat;
		}
		
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		
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
				
				SatPos satPos = satellite.getPosition(groundStationPosition, date);
				// 卫星的星下点位置
				double lng = satPos.getLongitude() * 180 / Math.PI;
				double lat = satPos.getLatitude() * 180 / Math.PI;
				double radius = satPos.getRangeCircleRadiusKm();
				
				// 网格分割，遍历所有的网格点
				double gridCount = 0;
				boolean gridCover = false;
				// 纬度以cos作为步长
				double stepLat = 1;
				double lngRange = 0;
				double latRange = 0;
				for (double u = minLng; u < maxLng; u++) {
					for (double v = minLat ; v < maxLat; v += stepLat) {
						// 星下点位置与网格点的距离
						double distance = GisUtil.getDistanceFromPosition(lng, lat, u, v);
						if (distance < radius) {
							gridCount++;
							gridCover = true;
						}
						stepLat = Math.cos(v / 180.0 * Math.PI);
						latRange++;
					}
					lngRange++;
				}
				
				if (gridCover) {
					// 覆盖面积占比
					double ratio = gridCount / (lngRange * latRange);
					SatelliteRegion region = new SatelliteRegion(date, ratio);
					list.add(region);
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 计算完成后标识置为true
		GlobalAccess.linkCoverRegionFinished = true;

		return list;
	}
	
	@Override
	public Page listRegionsByPage(Integer currentPage, Integer pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteRegion> list = orbitMapper.listRegions();
		PageInfo<SatelliteRegion> pageInfo = new PageInfo<>(list);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(list);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public List<SatelliteWaveBeam> listWaveBeam(String satelliteId, String datetime) {
		List<SatelliteWaveBeam> list = new ArrayList<>();
		
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 计算的时间
		    Date date = format.parse(datetime);
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的覆盖范围
			SatPos satPos = satellite.getPosition(groundStationPosition, date);
			// 卫星的星下点
			double lng = satPos.getLongitude();
			double lat = satPos.getLatitude();
			double alt = satPos.getAltitude();
			// 最大地心角
			double maxEarthAngle = Math.acos(GlobalConstant.EARTH_RADIUS_KM / (GlobalConstant.EARTH_RADIUS_KM + alt)) / Math.PI * 180;
			// 星下点位置与地心角的弧长
			double radius = GlobalConstant.EARTH_RADIUS_KM * maxEarthAngle * Math.PI / 180.0;
			for (int delta = 0; delta < 360; delta++) {
				double[] position = GisUtil.getLngLat(lng, lat, radius, delta);
				double longitude = new BigDecimal(position[0]).setScale(2, RoundingMode.UP).doubleValue();
				double latitude = new BigDecimal(position[1]).setScale(2, RoundingMode.UP).doubleValue();
				list.add(new SatelliteWaveBeam(datetime, latitude, longitude));
			}
			
		} catch (ParseException e) {
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public List<SatelliteWaveBeam> listWaveBeam(String satelliteId, String datetime, double viewAngle) {
        List<SatelliteWaveBeam> list = new ArrayList<>();
		
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 计算的时间
		    Date date = format.parse(datetime);
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的覆盖范围
			SatPos satPos = satellite.getPosition(groundStationPosition, date);
			// 卫星的星下点
			double lng = satPos.getLongitude();
			double lat = satPos.getLatitude();
			double alt = satPos.getAltitude();
			
			// 最大地心角
			double maxEarthAngle = Math.acos(GlobalConstant.EARTH_RADIUS_KM / (GlobalConstant.EARTH_RADIUS_KM + alt)) / Math.PI * 180;
			if (viewAngle >= maxEarthAngle)
				return null;
			
			// 地心角
			double earthAngle = Math.asin((GlobalConstant.EARTH_RADIUS_KM + alt) / GlobalConstant.EARTH_RADIUS_KM * Math.sin(viewAngle / 180.0 * Math.PI)) / Math.PI * 180 - viewAngle;
			
			// 星下点位置与地心角的弧长
			double radius = GlobalConstant.EARTH_RADIUS_KM * earthAngle * Math.PI / 180.0;
			for (int delta = 0; delta < 360; delta++) {
				double[] position = GisUtil.getLngLat(lng, lat, radius, delta);
				double longitude = new BigDecimal(position[0]).setScale(2, RoundingMode.UP).doubleValue();
				double latitude = new BigDecimal(position[1]).setScale(2, RoundingMode.UP).doubleValue();
				list.add(new SatelliteWaveBeam(datetime, latitude, longitude));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public List<SatelliteWaveBeam> listWaveBeam(String satelliteId, String datetime, double viewAngle, double swayAngle) {
        List<SatelliteWaveBeam> list = new ArrayList<>();
		
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 计算的时间
		    Date date = format.parse(datetime);
			
			// 实例化TLE
			SatelliteTle satelliteTle = orbitMapper.getTle(satelliteId);
			// 实例化SGP4计算的卫星
			Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
			// 使用SGP4计算卫星的覆盖范围
			SatPos satPos = satellite.getPosition(groundStationPosition, date);
			// 卫星的星下点
			double lng = satPos.getLongitude();
			double lat = satPos.getLatitude();
			double alt = satPos.getAltitude();
			
			// 最大地心角
			double maxEarthAngle = Math.acos(GlobalConstant.EARTH_RADIUS_KM / (GlobalConstant.EARTH_RADIUS_KM + alt)) / Math.PI * 180;
			if (viewAngle / 2.0 + swayAngle >= maxEarthAngle)
				return null;
			
			// 地心角
			double earthAngle = Math.asin((GlobalConstant.EARTH_RADIUS_KM + alt) / GlobalConstant.EARTH_RADIUS_KM * Math.sin((viewAngle / 2.0 + swayAngle) / 180.0 * Math.PI)) / Math.PI * 180 - (viewAngle / 2.0 + swayAngle);
			// 星下点位置与地心角的弧长
			double radius = GlobalConstant.EARTH_RADIUS_KM * earthAngle * Math.PI / 180.0;			
			for (int delta = 0; delta < 360; delta++) {
				double[] position = GisUtil.getLngLat(lng, lat, radius, delta);
				double longitude = new BigDecimal(position[0]).setScale(2, RoundingMode.UP).doubleValue();
				double latitude = new BigDecimal(position[1]).setScale(2, RoundingMode.UP).doubleValue();
				list.add(new SatelliteWaveBeam(datetime, latitude, longitude));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Page listWaveBeamsByPage(Integer currentPage, Integer pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteWaveBeam> list = orbitMapper.listWaveBeams();
		PageInfo<SatelliteWaveBeam> pageInfo = new PageInfo<>(list);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(list);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public List<SatelliteWaveBeam> listWaveBeams() {
		
		return orbitMapper.listWaveBeams();
	}

	@Override
	public int deleteWaveBeam() {
		
		return orbitMapper.deleteWaveBeam();
	}

	@Override
	public int insertWaveBeam(SatelliteWaveBeam waveBeam) {
		
		return orbitMapper.insertWaveBeam(waveBeam);
	}

	@Override
	public int insertBatchWaveBeam(List<SatelliteWaveBeam> list) {
		//清空表数据
		orbitMapper.deleteWaveBeam();
	    //批量插入数据
		int count = list.size() / GlobalConstant.BATCH_NUM;
		for (int i = 0; i < count; i++) {
			orbitMapper.insertBatchWaveBeam(list.subList(GlobalConstant.BATCH_NUM * i, GlobalConstant.BATCH_NUM * (i + 1)));
		}
		orbitMapper.insertBatchWaveBeam(list.subList(GlobalConstant.BATCH_NUM * count, list.size()));
		
		return count;
	}

	

	

	

	

	

	

	

}
