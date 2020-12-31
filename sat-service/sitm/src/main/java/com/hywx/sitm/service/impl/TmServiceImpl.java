package com.hywx.sitm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hywx.sitm.mapper.SatelliteMapper;
import com.hywx.sitm.mapper.TmMapper;
import com.hywx.sitm.po.Satellite;
import com.hywx.sitm.po.TmRsltFrame;
import com.hywx.sitm.redis.RedisFind;
import com.hywx.sitm.service.TmService;
import com.hywx.sitm.vo.SitmSatelliteRunningVO;
import com.hywx.sitm.vo.SitmSatelliteVO;
import com.hywx.sitm.vo.TmRsltFrameVO;

@Service("tmService")
public class TmServiceImpl implements TmService {
	private final String tableNamePrefix = "RTFRAMEPARAMETER_";
	//private final int SATELLITE_COUNT_IN_GROUP = 48;
	
	@Resource
    private TmMapper tmMapper;
	@Resource
	private SatelliteMapper satelliteMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<TmRsltFrame> listTmRsltFrames(String tableName) {
		
		return tmMapper.listTmRsltFrames(tableName);
	}

	@Override
	public void redisSitm(List<String> satelliteIdList, String group) {
//		int size = satelliteIdList.size();
//		int count = size / SATELLITE_COUNT_IN_GROUP;
//		for (int i = 0; i < count; i++) {
//			String groupKey = RedisFind.keyBuilder("tm", "group", String.valueOf(i));
//			for (int j = 0; j < SATELLITE_COUNT_IN_GROUP; j++) {
//				redisTemplate.opsForList().rightPush(groupKey, satelliteIdList.get(SATELLITE_COUNT_IN_GROUP * i + j));
//			}
//		}
//		//最后1组，卫星个数<=48
//		int remain = size % SATELLITE_COUNT_IN_GROUP;
//		if (remain != 0) {
//			String groupKey = RedisFind.keyBuilder("tm", "group", String.valueOf(count));
//			for (int j = 0; j < remain; j++) {
//				redisTemplate.opsForList().leftPush(groupKey, satelliteIdList.get(size - 1 - j));
//			}
//		}
		
		//遥测仿真分组标识
		String groupKey = RedisFind.keyBuilder("tm", "group", group);
		redisTemplate.delete(groupKey);
		//把卫星分组，每组个数为48，用于Quartz的Job的处理
		redisTemplate.opsForList().rightPushAll(groupKey, satelliteIdList.toArray());
		
		//遥测仿真自动发送的标识
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		//redisTemplate.delete(autoKey);
		//遥测仿真外测驱动的标识
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		//redisTemplate.delete(extKey);
	
		//清空之前的Redis信息
	    for (String satelliteId : satelliteIdList) {
	    	if (redisTemplate.opsForSet().isMember(autoKey, satelliteId))
	    		redisTemplate.opsForSet().remove(autoKey, satelliteId);
	    	if (redisTemplate.opsForSet().isMember(extKey, satelliteId))
	    		redisTemplate.opsForSet().remove(extKey, satelliteId);
	    	
	    	String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
	    	if (redisTemplate.hasKey(rawKey))
	    		redisTemplate.delete(rawKey);
	    	
	    	String gpsKey = RedisFind.keyBuilder("tm", "gps", satelliteId);
	    	if (redisTemplate.hasKey(gpsKey))
	    		redisTemplate.delete(gpsKey);
	    	
	    	String cmdKey = RedisFind.keyBuilder("tm", "cmd", satelliteId);
	    	if (redisTemplate.hasKey(cmdKey))
	    		redisTemplate.delete(cmdKey);
	    	String cmdFlagKey = RedisFind.keyBuilder("tm", "cmdflag", satelliteId);
	    	if (redisTemplate.hasKey(cmdFlagKey)) {
	    		redisTemplate.delete(cmdFlagKey);
	    		redisTemplate.opsForValue().set(cmdFlagKey, 0);
	    	}
			
			String countKey = RedisFind.keyBuilder("tm", "count", satelliteId);
			if (redisTemplate.hasKey(countKey)) {
				redisTemplate.delete(countKey);
				redisTemplate.opsForValue().set(countKey, 0);
			}
				
	    }
		
		
		//把组内卫星的遥测仿真参数放入Redis
		for (String satelliteId : satelliteIdList) {
			//所有卫星列表信息
			List<TmRsltFrame> frameList = listTmRsltFrames(tableNamePrefix.concat(satelliteId));
			if (frameList.isEmpty())
				return;
			
			List<TmRsltFrameVO> voList = new ArrayList<>();
			for (TmRsltFrame frame : frameList) 
				voList.add(new TmRsltFrameVO(frame));
			
			//卫星遥测仿真参数
			String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
			for (TmRsltFrameVO vo : voList)
			    redisTemplate.opsForList().rightPush(rawKey, vo);
		}
		
		//手动添加自动发送的卫星ID到Redis
		//redisTemplate.opsForSet().add(autoKey, "0101");
		//redisTemplate.opsForSet().add(extKey, "0101");
		redisTemplate.opsForSet().add(autoKey, "0103");
		redisTemplate.opsForSet().add(autoKey, "0104");
//		for (String satelliteId : satelliteIdList) {
//			redisTemplate.opsForSet().add(autoKey, satelliteId);
//		}
		
	}

	@Override
	public List<TmRsltFrameVO> listSitms(String satelliteId) {
		List<TmRsltFrameVO> list = new ArrayList<>();
		
		//卫星遥测仿真参数
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		List<Object> voList = redisTemplate.opsForList().range(rawKey, 0, -1);
		for (int i = 0; i < voList.size(); i++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) voList.get(i);
			list.add(vo);
		}
		
		return list;
	}

	@Override
	public SitmSatelliteVO getSatellite(String satelliteId) {
		//自动发送的遥测仿真
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			return new SitmSatelliteVO(satelliteId, true, "autoSend");
		}
		
		//外测驱动的遥测仿真
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			return new SitmSatelliteVO(satelliteId, true, "extDrive");
		}
		
		return new SitmSatelliteVO(satelliteId, false);
	}
	
	@Override
	public boolean getSatelliteIsRunning(String satelliteId) {
		//自动发送的遥测仿真
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			return true;
		}
		
		//外测驱动的遥测仿真
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public SitmSatelliteRunningVO getSatelliteRunning() {
		int count = 0;
		List<String> runningList = new ArrayList<>();
		
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		
		Set<Object> autoSends = redisTemplate.opsForSet().members(autoKey);
		count += redisTemplate.opsForSet().size(autoKey);
		for (Object obj : autoSends) {  
		    if (obj instanceof String) {
		    	runningList.add(obj.toString().concat("  自动发送"));
		    }
		} 
		
		Set<Object> extDrives = redisTemplate.opsForSet().members(extKey);
		count += redisTemplate.opsForSet().size(extKey);
		for (Object obj : extDrives) {  
			if (obj instanceof String) {
		    	runningList.add(obj.toString().concat("  外测驱动"));
		    }  
		} 
		
		Collections.sort(runningList);
		
		return new SitmSatelliteRunningVO(count, runningList);
	}

	@Override
	public void updateSatelliteRun(String satelliteId, String sendType, boolean isRun) {
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");

		if ("autoSend".equals(sendType)) {  //自动发送
			if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) 
			    redisTemplate.opsForSet().remove(extKey, satelliteId);
			
			if (isRun) {  //启动
				if (!redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			 	    redisTemplate.opsForSet().add(autoKey, satelliteId);
				}
			} else {  //停止
				if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
				    redisTemplate.opsForSet().remove(autoKey, satelliteId);
				}
			}
		} else {  //外测驱动
			if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) 
			    redisTemplate.opsForSet().remove(autoKey, satelliteId);
			
			if (isRun) {  //启动
				if (!redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			 	    redisTemplate.opsForSet().add(extKey, satelliteId);
				}
			} else {  //停止
				if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
				    redisTemplate.opsForSet().remove(extKey, satelliteId);
				}
			}
		}
	
	}
	
	@Override
	public void updateSatelliteRunBatch(List<String> satelliteIdList, String sendType) {
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		
		if ("autoSend".equals(sendType)) {  //自动发送	
			for (String satelliteId : satelliteIdList) {
				if (!redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			 	    redisTemplate.opsForSet().add(autoKey, satelliteId);
				}
			}
		} else {  //外测驱动
			for (String satelliteId : satelliteIdList) {
				if (!redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			 	    redisTemplate.opsForSet().add(extKey, satelliteId);
				}
			}
		}
	}
	
	@Override
	public void updateSatelliteRunAll(String sendType, boolean isRun) {
		List<Satellite> satelliteList = satelliteMapper.listSatellites();
		
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("tm", "sign", "ext");
		
		if ("autoSend".equals(sendType)) {  //自动发送
			redisTemplate.delete(extKey);
			
			if (isRun) {  //启动
				for (Satellite satellite : satelliteList) {
					if (!redisTemplate.opsForSet().isMember(autoKey, satellite.getSatelliteId())) {
				 	    redisTemplate.opsForSet().add(autoKey, satellite.getSatelliteId());
					}
				}
			} else {  //停止
				for (Satellite satellite : satelliteList) {
					if (redisTemplate.opsForSet().isMember(autoKey, satellite.getSatelliteId())) {
					    redisTemplate.opsForSet().remove(autoKey, satellite.getSatelliteId());
					}
				}
			}
		} else {  //外测驱动
			redisTemplate.delete(autoKey);
			
			if (isRun) {  //启动
				for (Satellite satellite : satelliteList) {
					if (!redisTemplate.opsForSet().isMember(extKey, satellite.getSatelliteId())) {
				 	    redisTemplate.opsForSet().add(extKey, satellite.getSatelliteId());
					}
				}
			} else {  //停止
				for (Satellite satellite : satelliteList) {
					if (redisTemplate.opsForSet().isMember(extKey, satellite.getSatelliteId())) {
					    redisTemplate.opsForSet().remove(extKey, satellite.getSatelliteId());
					}
				}
			}
		}
	}

	@Override
	public boolean updateSatelliteSendType(String satelliteId, String sendType) {
        boolean isUpdate = false;
		
		String autoKey = RedisFind.keyBuilder("tm", "sign", "auto");
		String extkey = RedisFind.keyBuilder("tm", "sign", "ext");
		if ("autoSend".equals(sendType)) {  //自动发送
			//停止外测驱动
			if (redisTemplate.opsForSet().isMember(extkey, satelliteId))
			    redisTemplate.opsForSet().remove(extkey, satelliteId);
			//开始自动发送
			if (!redisTemplate.opsForSet().isMember(autoKey, satelliteId))
			    redisTemplate.opsForSet().add(autoKey, satelliteId);
			
			isUpdate = true;
		} else {  //外测驱动
			//停止自动发送
			if (redisTemplate.opsForSet().isMember(autoKey, satelliteId))
			    redisTemplate.opsForSet().remove(autoKey, satelliteId);
			//开始外测驱动
			if (!redisTemplate.opsForSet().isMember(extkey, satelliteId))
			    redisTemplate.opsForSet().add(extkey, satelliteId);
			
			isUpdate = true;
		}
		
		return isUpdate;
	}

	@Override
	public boolean updateSatelliteParamType(String satelliteId, int paramId, String paramType) {
		//卫星遥测仿真参数
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			if (vo.getId() != paramId)
				continue;
			
			vo.setParamType(paramType);
			redisTemplate.opsForList().set(rawKey, index, vo);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean updateSatelliteCoefficient(String satelliteId, int paramId, double coefficient) {
		//卫星遥测仿真参数
		String rawKey = RedisFind.keyBuilder("tm", "raw", satelliteId);
		long size = redisTemplate.opsForList().size(rawKey);
		for (int index = 0; index < size; index++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) redisTemplate.opsForList().index(rawKey, index);
			if (vo.getId() != paramId)
				continue;
			
			vo.setCoefficient(coefficient);;
			redisTemplate.opsForList().set(rawKey, index, vo);
			
			return true;
		}
		
		return false;
	}

}
