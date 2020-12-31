package com.hywx.sitm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hywx.sitm.config.FileConfig;
import com.hywx.sitm.mapper.SatelliteMapper;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.po.Satellite;
import com.hywx.sitm.service.SatelliteService;

@Service("satelliteService")
public class SatelliteServiceImpl implements SatelliteService {

	@Resource
    private SatelliteMapper satelliteMapper;
	
	@Autowired
	private FileConfig fileConfig;
	
	@Override
	public List<Satellite> listSatellites() {
		
		return satelliteMapper.listSatellites();
	}
	
	@Override
	public List<String> listSatelliteIds() {
		
		return satelliteMapper.listSatelliteIds();
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

	

	

}
