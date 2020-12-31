package com.hywx.siin.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.config.FileConfig;
import com.hywx.siin.mapper.SatelliteMapper;
import com.hywx.siin.po.GpsFrame;
import com.hywx.siin.po.SatelliteInfo;
import com.hywx.siin.service.SatelliteService;

@Service("satelliteService")
public class SatelliteServiceImpl implements SatelliteService {

	@Resource
    private SatelliteMapper satelliteMapper;
	
	@Autowired
	private FileConfig fileConfig;
	
	@Override
	public List<SatelliteInfo> listAllSatelliteInfos() {
		
		return satelliteMapper.listAllSatellites();
	}
	
	@Override
	public List<String> listAllSatelliteIds() {
		
		return satelliteMapper.listAllSatelliteIds();
	}
	
	@Override
	public int insertBatchGpsFrame(List<GpsFrame> list) {
		
		return satelliteMapper.insertBatchGpsFrame(list);
	}

	@Override
	public List<GpsFrame> listGpsFrames() {
		
		return satelliteMapper.listGpsFrames();
	}

	@Override
	public List<GpsFrame> listGpsFrameFromDataSource() {
		String gpsFileName = fileConfig.getLocalpathGps().concat(File.separator).concat(fileConfig.getFilenameGps());
		
		return readGpsFile(gpsFileName);
	}
	
	
	private List<GpsFrame> readGpsFile(String gpsFileName) {
		List<GpsFrame> frameList = new ArrayList<>();
		
		//读取GPS的txt数据文件
		File file = new File(gpsFileName);
	    BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String line;
	        // 提前读一下，跳过第1行
	        reader.readLine(); 
	        line = reader.readLine(); 
	        while ((line = reader.readLine()) != null) {
	        	String[] splitLine = line.split("\\s+");
	        	if (splitLine.length < 14)
	        		continue;
	        	
	        	//DateTime: 2(year), 3(month), 4(day), 5(hour), 6(minute), 7(second and millisecond)
	        	String time = String.format("%s-%s-%s %s:%s:%s", splitLine[2], splitLine[3], splitLine[4], splitLine[5], splitLine[6], splitLine[7]);
	        	//位置
	        	double sx = Double.parseDouble(splitLine[8]);
	        	double sy = Double.parseDouble(splitLine[9]);
	        	double sz = Double.parseDouble(splitLine[10]);
	        	//速度
	        	double vx = Double.parseDouble(splitLine[11]);
	        	double vy = Double.parseDouble(splitLine[12]);
	        	double vz = Double.parseDouble(splitLine[13]);
	        	
	        	frameList.add(new GpsFrame(time, sx, sy, sz, vx, vy, vz));
	        }
	        
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
		
		
		return frameList;
	}

	@Override
	public Page listSatelliteInfoByPage(Integer currentPage, int pageSize) {
		Page page = new Page();
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<SatelliteInfo> infoList = satelliteMapper.listAllSatellites();
		PageInfo<SatelliteInfo> pageInfo = new PageInfo<>(infoList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(pageInfo.getList());
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public Boolean existSatellite(String satelliteId) {
		Boolean exist = satelliteMapper.existSatellite(satelliteId);
		if (exist == null)
			return false;
		
		return exist;
	}

	@Override
	public int insert(String satelliteId, String satelliteName, String satelliteText) {
		return satelliteMapper.insert(satelliteId, satelliteName, satelliteText);		
	}

	@Override
	public int delete(String satelliteId) {
		
		return satelliteMapper.delete(satelliteId);
	}

	@Override
	public int update(String satelliteName, String satelliteText, String satelliteId) {
		
		return satelliteMapper.update(satelliteName, satelliteText, satelliteId);
	}

	

	

}
