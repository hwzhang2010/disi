package com.hywx.sitm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.hywx.sitm.global.GlobalAccess;
import com.hywx.sitm.po.GpsFrame;
import com.hywx.sitm.po.Satellite;
import com.hywx.sitm.quartz.CronSchedulerJob;

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
	private final int SATELLITE_COUNT_IN_GROUP = 48;
	
	@Autowired
	private TmService tmService;
	@Autowired
	private SatelliteService satelliteService;
	
	@Autowired
    public CronSchedulerJob scheduleJobs;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> groupList = args.getOptionValues("group");
		if (groupList == null || groupList.isEmpty())
		    return;
		
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
		
		//GPS数据帧，保存在全局唯一的Vector
		List<GpsFrame> gpsFrameList = satelliteService.listGpsFrames();
		GlobalAccess.setGpsFrameVector(gpsFrameList);
		for (Satellite satellite : satelliteList) {
			//把所有卫星的GPS数据帧索引初始化为0
			GlobalAccess.setGpsFrameCountMap(satellite.getSatelliteId(), 0);
		}
		
		
		System.out.println("******************预加载完成**************");
	}
	
	
	private List<String> getSatelliteIdList(List<Satellite> satelliteList, String group) {
		List<String> satelliteIdList = new ArrayList<>();
		
		int groupNumber = Integer.parseInt(group);
		int size = satelliteList.size();
		int count = size / SATELLITE_COUNT_IN_GROUP;
		int remain = size % SATELLITE_COUNT_IN_GROUP;
		if (groupNumber < count) {
			for (int i = 0; i < SATELLITE_COUNT_IN_GROUP; i++) {
				String satelliteId = satelliteList.get(groupNumber * SATELLITE_COUNT_IN_GROUP  + i).getSatelliteId();
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
		
		return satelliteIdList;
	}
	

}
