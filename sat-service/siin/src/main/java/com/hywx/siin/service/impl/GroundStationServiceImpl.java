package com.hywx.siin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.po.GroundStationBusiness;
import com.hywx.siin.po.GroundStationInfo;
import com.hywx.siin.service.GroundStationService;
import com.hywx.siin.vo.GroundStationBusinessVO;

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

	@Override
	public List<GroundStationBusiness> listBusinesses() {
		
		return groundStationMapper.listBusinesses();
	}
	
	@Override
	public Boolean existBusiness(String groundStationId) {
		
		return groundStationMapper.existBusiness(groundStationId);
	}

	@Override
	public int insertBusiness(String groundStationId, double usage, String equipment, String carrier, String health) {
		
		return groundStationMapper.insertBusiness(groundStationId, usage, equipment, carrier, health);
	}

	@Override
	public int updateBusiness(double usage, String equipment, String carrier, String health, String groundStationId) {
		
		return groundStationMapper.updateBusiness(usage, equipment, carrier, health, groundStationId);
	}

	@Override
	public List<GroundStationBusinessVO> listGroundStationBusinesses() {
		List<GroundStationBusinessVO> list = new ArrayList<>();
		
		List<GroundStationBusiness> businessList = groundStationMapper.listBusinesses();
		for (int i = 0; i < businessList.size(); i++) {
			GroundStationBusiness business = businessList.get(i);
			
			GroundStationInfo info = groundStationMapper.getGroundStationById(business.getGroundStationId());
			GroundStationBusinessVO vo = new GroundStationBusinessVO(business.getGroundStationId(), info.getGroundStationLng(), info.getGroundStationLat(), business);
			list.add(vo);
		}
		
		return list;
	}

	

	@Override
	public int updateGroundStationBusiness(String groundStationId, double usage, String equipment, String carrier, String health) {
		int result = 0;
		
		if (groundStationMapper.existBusiness(groundStationId)) {
			result = groundStationMapper.updateBusiness(usage, equipment, carrier, health, groundStationId);
		} else {
			result = groundStationMapper.insertBusiness(groundStationId, usage, equipment, carrier, health);
		}
		
		return result;
	}
	
	
	

}
