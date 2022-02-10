package com.hywx.siin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hywx.siin.common.Page;
import com.hywx.siin.global.GlobalConstant;
import com.hywx.siin.mapper.GroundStationMapper;
import com.hywx.siin.mapper.SatelliteMapper;
import com.hywx.siin.mapper.TmMapper;
import com.hywx.siin.po.SatelliteInfo;
import com.hywx.siin.po.TmRsltFrame;
import com.hywx.siin.redis.RedisFind;
import com.hywx.siin.service.TmService;
import com.hywx.sitm.vo.SitmGroundStationFaultVO;
import com.hywx.sitm.vo.SitmSatelliteFaultVO;
import com.hywx.sitm.vo.SitmSatelliteRunningVO;
import com.hywx.sitm.vo.SitmSatelliteVO;
import com.hywx.sitm.vo.TmRsltFrameVO;


@Service("tmService")
public class TmServiceImpl implements TmService {
	//private final String tableNamePrefix = "RTFRAMEPARAMETER_";
	
	@Resource
    private TmMapper tmMapper;
	@Resource
	private SatelliteMapper satelliteMapper;
	@Resource
	private GroundStationMapper groundStationMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<TmRsltFrame> listTmRsltFrames(String tableName) {
		
		return tmMapper.listTmRsltFrames(tableName);
	}
	
	@Override
	public int existTmRsltFrame(String tableName) {
		
		return tmMapper.existTmRsltFrame(tableName);
	}

	@Override
	public int dropTmRsltFrame(String tableName) {
		
		return tmMapper.dropTmRsltFrame(tableName);
	}

	@Override
	public int createTmRsltFrame(String tableName) {
		
		return tmMapper.createTmRsltFrame(tableName);
	}
	
	@Override
	public int insertTmRslt(String tableName, String codeName, String name, int id, String srcType, String rsltType,
			String bd, String bitRange, int byteOrder, String coefficient, String algorithm, String range,
			String preCondition, String validFrameCnt, int frameId, int subsystemId, int objId) {
		
		return tmMapper.insertTmRslt(tableName, codeName, name, id, srcType, rsltType, bd, bitRange, byteOrder, coefficient, algorithm, range, preCondition, validFrameCnt, frameId, subsystemId, objId);
	}

	@Override
	public int deleteTmRslt(String tableName, int id) {
		
		return tmMapper.deleteTmRslt(tableName, id);
	}

	@Override
	public int updateTmRslt(String tableName, String codeName, String name, String srcType, String rsltType, String bd, String bitRange,
			int byteOrder, String coefficient, String algorithm, String range, String preCondition,
			String validFrameCnt, int frameId, int subsystemId, int objId, int id) {
		
		return tmMapper.updateTmRslt(tableName, codeName, name, srcType, rsltType, bd, bitRange, byteOrder, coefficient, algorithm, range, preCondition, validFrameCnt, frameId, subsystemId, objId, id);
	}
	
	@Override
	public int existTmRsltTable2(String satelliteId) {
		//String tableName = tableNamePrefix.concat(satelliteId);
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return existTmRsltFrame(tableName.toString());
	}
	
	@Override
	public int dropTmRsltFrame2(String satelliteId) {
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return tmMapper.dropTmRsltFrame(tableName.toString());
	}

	@Override
	public int createTmRsltFrame2(String satelliteId) {
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return tmMapper.createTmRsltFrame(tableName.toString());
	}
	
	@Override
	public int insertTmRslt(String satelliteId, TmRsltFrame rslt) {
		//String tableName = tableNamePrefix.concat(satelliteId);
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return insertTmRslt(tableName.toString(), rslt.getCodeName(), rslt.getName(), rslt.getId(), rslt.getSrcType(), rslt.getRsltType(), rslt.getBd(), rslt.getBitRange(), rslt.getByteOrder(), rslt.getCoefficient(), rslt.getAlgorithm(), rslt.getRange(), rslt.getPreCondition(), rslt.getValidFrameCnt(), rslt.getFrameId(), rslt.getSubsystemId(), rslt.getObjId());
	}
	
	@Override
	public int deleteTmRslt2(String satelliteId, int id) {
		//String tableName = tableNamePrefix.concat(satelliteId);
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return deleteTmRslt(tableName.toString(), id);
	}

	@Override
	public int updateTmRslt(String satelliteId, TmRsltFrame rslt) {
		//String tableName = tableNamePrefix.concat(satelliteId);
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
		return updateTmRslt(tableName.toString(), rslt.getCodeName(), rslt.getName(), rslt.getSrcType(), rslt.getRsltType(), rslt.getBd(), rslt.getBitRange(), rslt.getByteOrder(), rslt.getCoefficient(), rslt.getAlgorithm(), rslt.getRange(), rslt.getPreCondition(), rslt.getValidFrameCnt(), rslt.getFrameId(), rslt.getSubsystemId(), rslt.getObjId(), rslt.getId());
	}
	
	@Override
	public Page listTmRstlFramesByPage(String satelliteId, Integer currentPage, int pageSize) {
		//String tableName = tableNamePrefix.concat(satelliteId);
		StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
		tableName.append(satelliteId);
		
        Page page = new Page();
		
		// 分页
		PageHelper.startPage(currentPage, pageSize);
		List<TmRsltFrame> tmList = tmMapper.listTmRsltFrames(tableName.toString());
		PageInfo<TmRsltFrame> pageInfo = new PageInfo<>(tmList);
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageInfo.getPages());
		page.setDataList(tmList);
		page.setTotal(pageInfo.getTotal());
		
		return page;
	}

	@Override
	public List<TmRsltFrameVO> listSitms(String satelliteId) {
		List<TmRsltFrameVO> list = new ArrayList<>();
		
		//卫星遥测仿真参数
		String rawKey = RedisFind.keyBuilder("sitm", "tm", "raw", satelliteId);
		List<Object> voList = redisTemplate.opsForList().range(rawKey, 0, -1);
		for (int i = 0; i < voList.size(); i++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) voList.get(i);
			
			//JSONObject jsonObject = JSON.parseObject(voList.get(i).toString());
			//TmRsltFrameVO vo = JSON.toJavaObject(jsonObject, TmRsltFrameVO.class);
			
			list.add(vo);
		}
		
		return list;
	}
	
	@Override
	public Page listSitmsByPage(String satelliteId, Integer currentPage, Integer pageSize) {
        Page page = new Page();
		
		//卫星遥测仿真参数
		String rawKey = RedisFind.keyBuilder("sitm", "tm", "raw", satelliteId);
		if (!redisTemplate.hasKey(rawKey)) {
			return page;
		}
		
		long size = redisTemplate.opsForList().size(rawKey);
		
		// 页数
		int pageCount = 0; 
		if (size % pageSize == 0) {
	        pageCount = (int) (size / pageSize);
	    } else {
	        pageCount = (int) (size / pageSize + 1);
	    }
		
		// 开始索引
		long fromIndex = (currentPage - 1) * pageSize; 
		// 结束索引
        long toIndex = 0; 

        if (currentPage != pageCount) {
            toIndex = fromIndex + pageSize - 1;
        } else {
            toIndex = size - 1;
        }
		
        List<TmRsltFrameVO> list = new ArrayList<>();
		List<Object> voList = redisTemplate.opsForList().range(rawKey, fromIndex, toIndex);
		for (int i = 0; i < voList.size(); i++) {
			TmRsltFrameVO vo = (TmRsltFrameVO) voList.get(i);
			list.add(vo);
		}
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalPage(pageCount);
		page.setDataList(list);
		page.setTotal(size);
		
		return page;
	}

	@Override
	public SitmSatelliteVO getSatellite(String satelliteId) {
		//自动发送的遥测仿真
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			return new SitmSatelliteVO(satelliteId, true, "autoSend");
		}
		
		//外测驱动的遥测仿真
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
		if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			return new SitmSatelliteVO(satelliteId, true, "extDrive");
		}
		
		return new SitmSatelliteVO(satelliteId, false);
	}
	
	@Override
	public boolean getSatelliteIsRunning(String satelliteId) {
		//自动发送的遥测仿真
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		if (redisTemplate.opsForSet().isMember(autoKey, satelliteId)) {
			return true;
		}
		
		//外测驱动的遥测仿真
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
		if (redisTemplate.opsForSet().isMember(extKey, satelliteId)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public SitmSatelliteRunningVO getSatelliteRunning() {
		int count = 0;
		List<String> runningList = new ArrayList<>();
		
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
		
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
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");

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
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
		
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
		List<SatelliteInfo> satelliteList = satelliteMapper.listAllSatellites();
		
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		String extKey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
		
		if ("autoSend".equals(sendType)) {  //自动发送
			redisTemplate.delete(extKey);
			
			if (isRun) {  //启动
				for (SatelliteInfo satellite : satelliteList) {
					if (!redisTemplate.opsForSet().isMember(autoKey, satellite.getSatelliteId())) {
				 	    redisTemplate.opsForSet().add(autoKey, satellite.getSatelliteId());
					}
				}
			} else {  //停止
				for (SatelliteInfo satellite : satelliteList) {
					if (redisTemplate.opsForSet().isMember(autoKey, satellite.getSatelliteId())) {
					    redisTemplate.opsForSet().remove(autoKey, satellite.getSatelliteId());
					}
				}
			}
		} else {  //外测驱动
			redisTemplate.delete(autoKey);
			
			if (isRun) {  //启动
				for (SatelliteInfo satellite : satelliteList) {
					if (!redisTemplate.opsForSet().isMember(extKey, satellite.getSatelliteId())) {
				 	    redisTemplate.opsForSet().add(extKey, satellite.getSatelliteId());
					}
				}
			} else {  //停止
				for (SatelliteInfo satellite : satelliteList) {
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
		
		String autoKey = RedisFind.keyBuilder("sitm", "tm", "sign", "auto");
		String extkey = RedisFind.keyBuilder("sitm", "tm", "sign", "ext");
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
		String rawKey = RedisFind.keyBuilder("sitm", "tm", "raw", satelliteId);
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
		String rawKey = RedisFind.keyBuilder("sitm", "tm", "raw", satelliteId);
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

	@Override
	public int getSatelliteSendCount(String satelliteId) {
		String countKey = RedisFind.keyBuilder("sitm", "tm", "count", satelliteId);
		int count = 0;
		if (redisTemplate.hasKey(countKey))
		    count = (Integer) redisTemplate.opsForValue().get(countKey);
		
		return count;
	}

	@Override
	public void updateSatelliteSendCountZero(String satelliteId) {
		String countKey = RedisFind.keyBuilder("sitm", "tm", "count", satelliteId);
		redisTemplate.opsForValue().set(countKey, 0);;
		
	}
	
	@Override
	public String getGroundStationOfAutoSend(String satelliteId) {
		// 默认北京站
		String groundStationId = String.format("%08X", GlobalConstant.SID);
		
		String autoGroundStationKey = RedisFind.keyBuilder("sitm", "tm", "groundstation", satelliteId);
		if (redisTemplate.hasKey(autoGroundStationKey)) {
			groundStationId = (String) redisTemplate.opsForValue().get(autoGroundStationKey);
		}
		
		return groundStationId;
	}

	@Override
	public void updateGroundStationOfAutoSend(String satelliteId, String groundStationId) {
		String autoGroundStationKey = RedisFind.keyBuilder("sitm", "tm", "groundstation", satelliteId);
		// 更新redis中自动发送的信关站ID
		redisTemplate.opsForValue().set(autoGroundStationKey, groundStationId);
	}

	@Override
	public void updateSatelliteFault(List<String> satelliteIdList) {
		String satelliteKey = RedisFind.keyBuilder("sitm", "tm", "fault", "satellite");
		
		// 首先清空
		Set<Object> satelliteFaults = redisTemplate.opsForSet().members(satelliteKey);
		for (Object obj : satelliteFaults) {  
			redisTemplate.opsForSet().remove(satelliteKey, obj);
		} 
		// 然后添加
		for (String satelliteId : satelliteIdList) {
			if (!redisTemplate.opsForSet().isMember(satelliteKey, satelliteId)) {
		 	    redisTemplate.opsForSet().add(satelliteKey, satelliteId);
			}
		}
		
	}

	@Override
	public void updateGroundStationFault(List<String> groundStationIdList) {
        String groundStationKey = RedisFind.keyBuilder("sitm", "tm", "fault", "groundstation");
		
		// 首先清空
		Set<Object> groundStationFaults = redisTemplate.opsForSet().members(groundStationKey);
		for (Object obj : groundStationFaults) {  
			redisTemplate.opsForSet().remove(groundStationKey, obj);
		} 
		// 然后添加
		for (String groundStationId : groundStationIdList) {
			if (!redisTemplate.opsForSet().isMember(groundStationKey, groundStationId)) {
		 	    redisTemplate.opsForSet().add(groundStationKey, groundStationId);
			}
		}
		
	}

	@Override
	public SitmSatelliteFaultVO getSatelliteFaulting() {
		int count = 0;
		List<String> faultList = new ArrayList<>();
		
		String satelliteKey = RedisFind.keyBuilder("sitm", "tm", "fault", "satellite");
		
		Set<Object> satelliteFaults = redisTemplate.opsForSet().members(satelliteKey);
		count += redisTemplate.opsForSet().size(satelliteKey);
		for (Object obj : satelliteFaults) {  
		    if (obj instanceof String) {
		    	faultList.add(obj.toString());
		    }
		} 
		
		Collections.sort(faultList);
		
		return new SitmSatelliteFaultVO(count, faultList);
	}

	@Override
	public SitmGroundStationFaultVO getGroundStationFaulting() {
		int count = 0;
		List<String> faultList = new ArrayList<>();
		
		String groundStationKey = RedisFind.keyBuilder("sitm", "tm", "fault", "groundstation");
		
		Set<Object> groundStationFaults = redisTemplate.opsForSet().members(groundStationKey);
		count += redisTemplate.opsForSet().size(groundStationKey);
		for (Object obj : groundStationFaults) {  
		    if (obj instanceof String) {
		    	faultList.add(obj.toString());
		    }
		}  
		
		Collections.sort(faultList);
		
		return new SitmGroundStationFaultVO(count, faultList);
	}

	

	

	

	
	
	
	

}
