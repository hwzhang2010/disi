package com.hywx.siin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.mapper.FaultMapper;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.po.Device;
import com.hywx.siin.po.Fault;
import com.hywx.siin.po.FaultSend;
import com.hywx.siin.redis.RedisFind;
import com.hywx.siin.service.FaultService;
import com.hywx.sisn.vo.FaultGroundStationVO;
import com.hywx.sisn.vo.FaultLevelVO;
import com.hywx.sisn.vo.FaultSatelliteVO;
import com.hywx.sisn.vo.FaultVO;

@Service("faultService")
public class FaultServiceImpl implements FaultService {
	@Resource
	private FaultMapper faultMapper;
	@Resource
	private GroundStationMapper groundStationMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<Device> listDevices() {
		
		return faultMapper.listDevices();
	}
	
	@Override
	public List<Device> listSatelliteDevices() {
		
		return faultMapper.listSatelliteDevices();
	}

	@Override
	public List<Device> listGroundStationDevices() {
		
		return faultMapper.listGroundStationDevices();
	}
	
	@Override
	public String getDeviceNameById(Integer id) {
		
		return faultMapper.getDeviceNameById(id);
	}
	
	@Override
	public Integer getMainById(Integer id) {
		
		return faultMapper.getMainById(id);
	}
	
	@Override
	public Page listDevicesByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<Device> list = faultMapper.listDevices();
		PageInfo<Device> pageInfo = new PageInfo<>(list);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public Fault getFaultById(Integer mainId, Integer subId) {
		
		return faultMapper.getFaultById(mainId, subId);
	}

	@Override
	public List<Fault> listFaultByMainId(Integer mainId) {
		
		return faultMapper.listFaultByMainId(mainId);
	}
	
	@Override
	public int insertFault(Integer type, String satelliteId, String groundStationId, Integer deviceId, Integer mainId, String mainName, Integer subId, String subName, Integer level, Boolean waiting) {
		
		return faultMapper.insertFault(type, satelliteId, groundStationId, deviceId, mainId, mainName, subId, subName, level, waiting);
	}

	@Override
	public int insertBatchFault(List<FaultSend> list) {
		
		return faultMapper.insertBatchFault(list);
	}

	@Override
	public int updateFault(Boolean waiting) {
		
		return faultMapper.updateFault(waiting);
	}

	@Override
	public int deleteFaultSatellite(String satelliteId) {
		
		return faultMapper.deleteFaultSatellite(satelliteId);
	}

	@Override
	public int deleteFaultGroundStation(String groundStationId) {
		
		return faultMapper.deleteFaultGroundStation(groundStationId);
	}
	
	@Override
	public int deleteFault() {
		
		return faultMapper.deleteFault();
	}
	
	@Override
	public Boolean existFaultSatellite(String satelliteId) {
		
		return faultMapper.existFaultSatellite(satelliteId);
	}
	
	@Override
	public FaultSend getFaultSatelliteById(String satelliteId) {
		
		return faultMapper.getFaultSatelliteById(satelliteId);
	}
	
	@Override
	public Boolean existFaultGroundStation(String groundStationId) {
		
		return faultMapper.existFaultGroundStation(groundStationId);
	}

	@Override
	public FaultSend getFaultGroundStationById(String groundStationId) {
		
		return faultMapper.getFaultGroundStationById(groundStationId);
	}
	
	@Override
	public List<String> listFaultSatelliteIds() {
		
		return faultMapper.listFaultSatelliteIds();
	}
	
	@Override
	public List<String> listFaultGroundStationIds() {
		
		return faultMapper.listFaultGroundStationIds();
	}
	
	@Override
	public List<FaultSend> listFaults() {
		
		return faultMapper.listFaults();
	}
	
	@Override
	public List<FaultSend> listFaultWaiting(Boolean waiting) {
		
		return faultMapper.listFaultWaiting(waiting);
	}
	
	@Override
	public List<FaultLevelVO> listFaultLevels() {
		List<FaultLevelVO> list = new ArrayList<>();
		list.add(new FaultLevelVO(0, "轻微"));
		list.add(new FaultLevelVO(1, "一般"));
		list.add(new FaultLevelVO(2, "严重"));
		list.add(new FaultLevelVO(3, "致命"));
		
		return list;
	}
	
	@Override
	public List<FaultSatelliteVO> listFaultSatellites() {
		List<FaultSatelliteVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultSatellites();
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
		    FaultSatelliteVO vo = new FaultSatelliteVO(faultSend);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultSatelliteVO> listFaultSatellitesById(String satelliteId) {
		List<FaultSatelliteVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultSatellitesById(satelliteId);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
		    FaultSatelliteVO vo = new FaultSatelliteVO(faultSend);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultSatelliteVO> listFaultSatellitesByLevel(Integer level) {
		List<FaultSatelliteVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultSatellitesByLevel(level);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
		    FaultSatelliteVO vo = new FaultSatelliteVO(faultSend);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultSatelliteVO> listFaultSatellitesByIdLevel(String satelliteId, Integer level) {
		List<FaultSatelliteVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultSatellitesByIdLevel(satelliteId, level);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
		    FaultSatelliteVO vo = new FaultSatelliteVO(faultSend);
		    list.add(vo);
		}
		
		return list;
	}

	@Override
	public List<FaultGroundStationVO> listFaultGroundStations() {
		List<FaultGroundStationVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultGroundStations();
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
			String groundStationId = faultSend.getGroundStationId();
			String groundStationText = groundStationMapper.getGroundStationById(groundStationId).getGroundStationText();
			FaultGroundStationVO vo = new FaultGroundStationVO(faultSend, groundStationText);
			list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultGroundStationVO> listFaultGroundStationsById(String groundStationId) {
		List<FaultGroundStationVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultGroundStationsById(groundStationId);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
			String groundStationText = groundStationMapper.getGroundStationById(groundStationId).getGroundStationText();
		    FaultGroundStationVO vo = new FaultGroundStationVO(faultSend, groundStationText);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultGroundStationVO> listFaultGroundStationsByLevel(Integer level) {
		List<FaultGroundStationVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultGroundStationsByLevel(level);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
			String groundStationId = faultSend.getGroundStationId();
			String groundStationText = groundStationMapper.getGroundStationById(groundStationId).getGroundStationText();
		    FaultGroundStationVO vo = new FaultGroundStationVO(faultSend, groundStationText);
		    list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public List<FaultGroundStationVO> listFaultGroundStationsByIdLevel(String groundStationId, Integer level) {
		List<FaultGroundStationVO> list = new ArrayList<>();
		
		List<FaultSend> faultSendList = faultMapper.listFaultGroundStationsByIdLevel(groundStationId, level);
		for (int i = 0; i < faultSendList.size(); i++) {
			FaultSend faultSend = faultSendList.get(i);
			String groundStationText = groundStationMapper.getGroundStationById(groundStationId).getGroundStationText();
		    FaultGroundStationVO vo = new FaultGroundStationVO(faultSend, groundStationText);
		    list.add(vo);
		}
		
		return list;
	}

	@Override
	public void sendFault(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level) {
		FaultVO vo = new FaultVO(satelliteId, groundStationId, id, fault, level);
		String faultKey = RedisFind.keyBuilder("sisn", "fault", "send", "");
		redisTemplate.opsForValue().set(faultKey, vo);
		
		String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
		redisTemplate.opsForValue().set(faultSignKey, 1);
	}

	@Override
	public void sendFaultSatellite(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level) {
		faultMapper.deleteFaultSatellite(satelliteId);
		
		FaultSend faultSend = new FaultSend(0, satelliteId, groundStationId, id, fault.getMainId(), fault.getMainName(), fault.getSubId(), fault.getSubName(), level, true);
		
		faultMapper.insertFault(faultSend.getType(), faultSend.getSatelliteId(), faultSend.getGroundStationId(), faultSend.getDeviceId(), faultSend.getMainId(), faultSend.getMainName(), faultSend.getSubId(), faultSend.getSubName(), faultSend.getLevel(), true);
		
		String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
		redisTemplate.opsForValue().set(faultSignKey, 1);
		
	}
	
	@Override
	public void sendFaultSatellites(List<String> satelliteIdList, String groundStationId, Integer id, Fault fault, Integer level) {
		if (satelliteIdList.isEmpty())
			return;
		
		Integer newLevel = level;
		List<FaultSend> list = new ArrayList<>();
		for (int i = 0; i < satelliteIdList.size(); i++) {
			String satelliteId = satelliteIdList.get(i);
			Boolean exist = faultMapper.existFaultSatellite(satelliteId);
			if (exist != null && exist) {
				FaultSend faultSendTemp = faultMapper.getFaultSatelliteById(satelliteId);
				newLevel = faultSendTemp.getLevel();
			}
			
			faultMapper.deleteFaultSatellite(satelliteId);
			
			FaultSend faultSend = new FaultSend(0, satelliteId, groundStationId, id, fault.getMainId(), fault.getMainName(), fault.getSubId(), fault.getSubName(), newLevel, true);
			list.add(faultSend);
		}
		
		faultMapper.insertBatchFault(list);
		
		String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
		redisTemplate.opsForValue().set(faultSignKey, 1);
		
	}


	@Override
	public void sendFaultGroundStations(String satelliteId, List<String> groundStationIdList, Integer id, Fault fault, Integer level) {
		if (groundStationIdList.isEmpty())
			return;
		
		Integer newLevel = level;
		List<FaultSend> list = new ArrayList<>();
		for (int i = 0; i < groundStationIdList.size(); i++) {
			String groundStationId = groundStationIdList.get(i);
			Boolean exist = faultMapper.existFaultGroundStation(groundStationId);
			if (exist != null && exist) {
				FaultSend faultSendTemp = faultMapper.getFaultGroundStationById(groundStationId);
				newLevel = faultSendTemp.getLevel();
			}
			
			faultMapper.deleteFaultGroundStation(groundStationId);
			FaultSend faultSend = new FaultSend(1, satelliteId, groundStationId, id, fault.getMainId(), fault.getMainName(), fault.getSubId(), fault.getSubName(), newLevel, true);
			list.add(faultSend);
		}
		
		faultMapper.insertBatchFault(list);
		
		String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
		redisTemplate.opsForValue().set(faultSignKey, 1);
		
	}

	@Override
	public void sendFaultGroundStation(String satelliteId, String groundStationId, Integer id, Fault fault, Integer level) {
		faultMapper.deleteFaultGroundStation(groundStationId);
		FaultSend faultSend = new FaultSend(1, satelliteId, groundStationId, id, fault.getMainId(), fault.getMainName(), fault.getSubId(), fault.getSubName(), level, true);
		
		faultMapper.insertFault(faultSend.getType(), faultSend.getSatelliteId(), faultSend.getGroundStationId(), faultSend.getDeviceId(), faultSend.getMainId(), faultSend.getMainName(), faultSend.getSubId(), faultSend.getSubName(), faultSend.getLevel(), true);
		
		String faultSignKey = RedisFind.keyBuilder("sisn", "fault", "sign", "");
		redisTemplate.opsForValue().set(faultSignKey, 1);
		
	}

	

	

	

	

	

	

	

	

	

	

}
