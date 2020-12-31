package com.hywx.siin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.mapper.TcMapper;
import com.hywx.siin.po.InjectionData;
import com.hywx.siin.po.TcData;
import com.hywx.siin.service.TcService;
import com.hywx.siin.vo.CmdVO;
import com.hywx.siin.vo.InjectionVO;

@Service("tcService")
public class TcServiceImpl implements TcService {
	@Resource
	private TcMapper tcMapper;

	@Override
	public int insertTc(long sendTime, String satelliteId, String stationId, String data) {
		
		return tcMapper.insertTc(sendTime, satelliteId, stationId, data);
	}
	
	@Override
	public int deleteTc(long sendTime) {
		
		return tcMapper.deleteTc(sendTime);
	}

	@Override
	public List<TcData> listTcBySatelliteId(String satelliteId, long startTime, long endTime) {
		
		return tcMapper.listTcBySatelliteId(satelliteId, startTime, endTime);
	}

	@Override
	public List<TcData> listTcBySatelliteIdAndStationId(String satelliteId, String stationId, long startTime, long endTime) {
		
		return tcMapper.listTcBySatelliteIdAndStationId(satelliteId, stationId, startTime, endTime);
	}
	

	@Override
	public Page listTcsByPage(String satelliteId, String groundStationId, String start, String end, Integer currentPage, int pageSize) {
		Page page = new Page();
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    try {
	    	// 开始时间
			Date startDate = format.parse(start);
			long startTime = startDate.getTime();
			// 结束时间
			Date endDate = format.parse(end);
			long endTime = endDate.getTime();
			
			// 分页
			PageHelper.startPage(currentPage, pageSize);
			List<TcData> tcList = tcMapper.listTcBySatelliteIdAndStationId(satelliteId, groundStationId, startTime, endTime);
			PageInfo<TcData> pageInfo = new PageInfo<>(tcList);
			
			List<CmdVO> voList = new ArrayList<>();
			List<TcData> tempList = pageInfo.getList();
			for (int i = 0; i < tempList.size(); i++) {
				CmdVO vo = new CmdVO(tempList.get(i).getSendTime(), tempList.get(i).getData());
				voList.add(vo);
			}
			
			page.setCurrentPage(currentPage);
			page.setPageSize(pageSize);
			page.setTotalPage(pageInfo.getPages());
			page.setDataList(voList);
			page.setTotal(pageInfo.getTotal());
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return page;
	}


	@Override
	public int insertInjection(long sendTime, String satelliteId, String stationId, String data) {
		
		return tcMapper.insertInjection(sendTime, satelliteId, stationId, data);
	}

	@Override
	public int deleteInjection(long sendTime) {
		
		return tcMapper.deleteInjection(sendTime);
	}

	@Override
	public List<InjectionData> listInjectionBySatelliteId(String satelliteId, long startTime, long endTime) {
		
		return tcMapper.listInjectionBySatelliteId(satelliteId, startTime, endTime);
	}

	@Override
	public List<InjectionData> listInjectionBySatelliteIdAndStationId(String satelliteId, String stationId, long startTime, long endTime) {
		
		return tcMapper.listInjectionBySatelliteIdAndStationId(satelliteId, stationId, startTime, endTime);
	}

	@Override
	public Page listInjectionsByPage(String satelliteId, String groundStationId, String start, String end, Integer currentPage, int pageSize) {
        Page page = new Page();
		
		// 时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    try {
	    	// 开始时间
			Date startDate = format.parse(start);
			long startTime = startDate.getTime();
			// 结束时间
			Date endDate = format.parse(end);
			long endTime = endDate.getTime();
			
			// 分页
			PageHelper.startPage(currentPage, pageSize);
			List<InjectionData> injectionList = tcMapper.listInjectionBySatelliteIdAndStationId(satelliteId, groundStationId, startTime, endTime);
			PageInfo<InjectionData> pageInfo = new PageInfo<>(injectionList);
			
			List<InjectionVO> voList = new ArrayList<>();
			List<InjectionData> tempList = pageInfo.getList();
			for (int i = 0; i < tempList.size(); i++) {
				InjectionVO vo = new InjectionVO(tempList.get(i).getSendTime(), tempList.get(i).getData());
				voList.add(vo);
			}
			
			page.setCurrentPage(currentPage);
			page.setPageSize(pageSize);
			page.setTotalPage(pageInfo.getPages());
			page.setDataList(voList);
			page.setTotal(pageInfo.getTotal());
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return page;
	}

	

	
	
	
	

}
