package com.hywx.siin.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.config.FileConfig;
import com.hywx.siin.mapper.OrbitMapper;
import com.hywx.siin.mapper.SatelliteMapper;
import com.hywx.siin.po.GpsFrame;
import com.hywx.siin.po.SatelliteBusiness;
import com.hywx.siin.po.SatelliteInfo;
import com.hywx.siin.po.SatelliteTle;
import com.hywx.siin.service.SatelliteService;
import com.hywx.siin.vo.SatelliteBusinessVO;

@Service("satelliteService")
public class SatelliteServiceImpl implements SatelliteService {

	@Resource
    private SatelliteMapper satelliteMapper;
	@Resource
	private OrbitMapper orbitMapper;
	
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
	public SatelliteInfo getSatelliteById(String satelliteId) {
		
		return satelliteMapper.getSatelliteById(satelliteId);
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

	@Override
	public List<SatelliteBusiness> listBusinesses() {
		
		return satelliteMapper.listBusinesses();
	}
	
	@Override
	public Boolean existBusiness(String satelliteId) {
		
		return satelliteMapper.existBusiness(satelliteId);
	}

	@Override
	public int insertBusiness(String satelliteId, double usage, String condition, String health) {
		
		return satelliteMapper.insertBusiness(satelliteId, usage, condition, health);
	}

	@Override
	public int updateBusiness(double usage, String condition, String health, String satelliteId) {
		
		return satelliteMapper.updateBusiness(usage, condition, health, satelliteId);
	}

	@Override
	public List<SatelliteBusinessVO> listSatelliteBusinesses(Long timeStamp) {
		List<SatelliteBusinessVO> list = new ArrayList<>();
		
		Date date = new Date(timeStamp);
		// 使用北京的地理坐标作为地面站进行计算卫星的位置
		GroundStationPosition groundStationPosition = new GroundStationPosition(116, 39, 0);
		List<SatelliteBusiness> businessList = satelliteMapper.listBusinesses();
		for (int i = 0; i < businessList.size(); i++) {
			SatelliteBusiness business = businessList.get(i);
			// 实例化TLE
		    SatelliteTle satelliteTle = orbitMapper.getTle(business.getSatelliteId());
		    // 实例化SGP4计算的卫星
		    Satellite satellite = SatelliteFactory.createSatellite(new TLE(satelliteTle.getTle()));
		    SatPos satPos = satellite.getPosition(groundStationPosition, date);
		    // 卫星的星下点位置
			double lng = satPos.getLongitude() * 180 / Math.PI;
			double lat = satPos.getLatitude() * 180 / Math.PI;
			SatelliteBusinessVO vo = new SatelliteBusinessVO(business.getSatelliteId(), lng, lat, business);
			list.add(vo);
		}
		
		return list;
	}

	@Override
	public int updateSatelliteBusiness(String satelliteId, double usage, String condition, String health) {
		int result = 0;
		
		if (satelliteMapper.existBusiness(satelliteId)) {
			result = satelliteMapper.updateBusiness(usage, condition, health, satelliteId);
		} else {
			result = satelliteMapper.insertBusiness(satelliteId, usage, condition, health);
		}
		
		return result;
	}


	

	

	

}
