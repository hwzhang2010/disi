package com.hywx.siin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.service.GroundStationService;

@Service("groundStationService")
public class GroundStationServiceImpl implements GroundStationService {

	@Resource
	private GroundStationMapper groundStationMapper;
	
	@Override
	public List<GroundStationInfo> listAllGroundStations() {
		
		return groundStationMapper.listAllGroundStations();
	}

	@Override
	public List<String> listAllGroundStationIds() {
		
		return groundStationMapper.listAllGroundStationIds();
	}

	@Override
	public GroundStationInfo getGroundStationById(String groundStationId) {
		
		return groundStationMapper.getGroundStationById(groundStationId);
	}

	@Override
	public Boolean existGroundStation(String groundStationId) {
		Boolean exist = groundStationMapper.existGroundStation(groundStationId);
		if (exist == null)
			return false;
		
		return exist;
	}

	@Override
	public int insert(String groundStationId, String groundStationName, String groundStatonText, double groundStationLng, double groundStationLat, double groundStationAlt) {
		
		return groundStationMapper.insert(groundStationId, groundStationName, groundStatonText, groundStationLng, groundStationLat, groundStationAlt);
	}

	@Override
	public int delete(String groundStationId) {
		
		return groundStationMapper.delete(groundStationId);
	}

	@Override
	public int update(String groundStationName, String groundStationText, double groundStationLng, double groundStationLat, double groundStationAlt, String groundStationId) {
		
		return groundStationMapper.update(groundStationName, groundStationText, groundStationLng, groundStationLat, groundStationAlt, groundStationId);
	}

	@Override
	public Page listGroundStationInfoByPage(Integer currentPage, int pageSize) {
        Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<GroundStationInfo> infoList = groundStationMapper.listAllGroundStations();
		PageInfo<GroundStationInfo> pageInfo = new PageInfo<>(infoList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}
	
	
	

}
