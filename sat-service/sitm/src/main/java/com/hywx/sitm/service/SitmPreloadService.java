package com.hywx.sitm.service;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sitm.bo.TlePredictionFactory;
import com.hywx.sitm.config.FileConfig;
import com.hywx.sitm.global.GlobalAccess;
import com.hywx.sitm.global.GlobalConstant;
import com.hywx.sitm.orbit.tle.prediction.Tle;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.po.Satellite;
import com.hywx.sitm.po.SatelliteTle;
import com.hywx.sitm.quartz.CronSchedulerJob;
import com.hywx.sitm.util.FileUtil;

/*
 * 1.InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * 2.ApplicationRunner和CommandLineRunner接口，当springboot的main方法快要执行结束时会调用afterRefresh然后再调用callRunners来加载所有的实现ApplicationRunner和CommandLineRunner的类然后执行run方法来初始化所写的内容。
 * 3.ApplicationContext初始化或刷新完成后触发的事件：ContextRefreshedEvent类型ApplicationListener接口ApplicationListener<ContextRefreshedEvent>,然后重写onApplicationEvent方法。
 * 执行顺序：（spring bean初始化） –> spring事件ContextRefreshedEvent–> CommandLineRunner/ApplicationRunner
 * 实现ApplicationRunner和CommandLineRunner接口（建议）
 */
@Service("sitmPreloadService")
@Order(0)
public class SitmPreloadService implements ApplicationRunner {
	//private final int SATELLITE_COUNT_IN_GROUP = 1024;
	private int groupCount = 0;
	
	@Autowired
	private FileConfig fileConfig;
	@Autowired
	private TmService tmService;
	@Autowired
	private SatelliteService satelliteService;
	
	@Autowired
    public CronSchedulerJob scheduleJobs;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String tmFileName = fileConfig.getLocalpathTm().concat(File.separator).concat(fileConfig.getFilenameTm());
		groupCount = readTm(tmFileName);
		System.out.println(String.format("************卫星分组个数: %d.************", groupCount));
		if (groupCount <= 0) {
			System.out.println("************卫星分组个数错误.************");
			return;
		}
		
		List<String> groupList = args.getOptionValues("group");
		if (groupList == null || groupList.isEmpty()) {
			System.out.println("************卫星分组参数未输入或错误.************");
		    return;
		}
		
		//卫星分组参数
		String group = groupList.get(0);
		
		//获取卫星列表
		List<Satellite> satelliteList = satelliteService.listSatellites();
		//获取分组的卫星列表
		List<String> satelliteIdList = getSatelliteIdList(satelliteList, group);
		if (satelliteIdList == null) {
			System.out.println("************卫星分组参数错误,预加载失败.************");
			return;
		}
		
		//把所有卫星的遥测描述表放入Redis
		tmService.redisSitm(satelliteIdList, group);
		
		//把本组内的卫星ID放入内存Vector
		GlobalAccess.putSatellite2Group(satelliteIdList);
		
		//初始化外测驱动的GPS数据, 保存在全局唯一的Map
		GlobalAccess.setSitmGpsFromExternal(satelliteIdList);
		//GlobalAccess.setSitmGpsArrived(satelliteIdList);
		
//		//GPS数据帧，保存在全局唯一的Vector
//		List<GpsFrame> gpsFrameList = satelliteService.listGpsFrames();
//		GlobalAccess.setGpsFrameVector(gpsFrameList);
//		for (Satellite satellite : satelliteList) {
//			//把所有卫星的GPS数据帧索引初始化为0
//			GlobalAccess.setGpsFrameCountMap(satellite.getSatelliteId(), 0);
//		}
		
		// 卫星两行根数
		for (String satelliteId : satelliteIdList) {
			// 卫星两行根数
			SatelliteTle tle = satelliteService.getSatelliteTle(satelliteId);
		    // 把两行根数提供给SGP4计算(GPS)位置和速度
		    TlePredictionFactory.registerTle(satelliteId, new Tle(tle.getTleLine1(), tle.getTleLine2()));
		}
		
		
		System.out.println("******************预加载******************");
	}
	
	
	private List<String> getSatelliteIdList(List<Satellite> satelliteList, String group) {
		List<String> satelliteIdList = new ArrayList<>();
		
		int groupNumber = Integer.parseInt(group);
		int size = satelliteList.size();
		int count = size / groupCount;
		int remain = size % groupCount;
		if (groupNumber < count) {
			for (int i = 0; i < groupCount; i++) {
				String satelliteId = satelliteList.get(groupNumber * groupCount  + i).getSatelliteId();
				satelliteIdList.add(satelliteId);
			}
		} else {
			if (remain > 0) {
				for (int i = 0; i < remain; i++) {
					String satelliteId = satelliteList.get(size - 1 - i).getSatelliteId();
					satelliteIdList.add(satelliteId);
				}
			} else {
				return null;
			}
		}
		
		// 如果数据库表中不存在遥测参数表，则先移除
		Iterator<String> iterator = satelliteIdList.iterator();
	    while (iterator.hasNext()) {
	        String satelliteId = iterator.next();
	        StringBuilder tableName = new StringBuilder(GlobalConstant.TM_TABLENAME_PREFIX);
			tableName.append(satelliteId);
	         
			// 使用迭代器的删除方法删除不存在遥测参数表的卫星ID
	        if (tmService.existTmRsltFrame(tableName.toString()) == 0) {
	            iterator.remove();
	        } 
	    }
		
		return satelliteIdList;
	}
	
	
	/**
	 * 遥测配置文件: 每组卫星的遥测仿真个数
	 * @param fileName
	 * @return
	 */
	private Integer readTm(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return 0;
		
		JSONObject tm = JSONObject.parseObject(jsonString);
		Integer groupCount = tm.getInteger("group_count");
		
		return groupCount;
	}
	
	
	

}
