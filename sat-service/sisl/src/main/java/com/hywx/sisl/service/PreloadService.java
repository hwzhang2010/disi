package com.hywx.sisl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.github.amsacode.predict4java.GroundStationPosition;
import com.github.amsacode.predict4java.TLE;
import com.hywx.sisl.bo.PredictFactory;
import com.hywx.sisl.bo.TlePredictionFactory;
import com.hywx.sisl.orbit.tle.prediction.Tle;
import com.hywx.sisl.po.GroundStationInfo;
import com.hywx.sisl.po.SatelliteTle;

/*
 * 1.InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * 2.ApplicationRunner和CommandLineRunner接口，当springboot的main方法快要执行结束时会调用afterRefresh然后再调用callRunners来加载所有的实现ApplicationRunner和CommandLineRunner的类然后执行run方法来初始化所写的内容。
 * 3.ApplicationContext初始化或刷新完成后触发的事件：ContextRefreshedEvent类型ApplicationListener接口ApplicationListener<ContextRefreshedEvent>,然后重写onApplicationEvent方法。
 * 执行顺序：（spring bean初始化） –> spring事件ContextRefreshedEvent–> CommandLineRunner/ApplicationRunner
 * 实现ApplicationRunner和CommandLineRunner接口（建议）
 */
@Service("preloadService")
@Order(0)
public class PreloadService implements ApplicationRunner {
	@Autowired
	private SatelliteService satelliteService;
	@Autowired
	private GroundStationService groundStationService;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		
		// 预先把需要计算的卫星和地面站放入redis缓存，以便进行SGP4计算时加快访问速度
		satelliteService.redisSgp4();
		groundStationService.redisSgp4();
		
		// 卫星
		List<String> satelliteIdList = satelliteService.listSatelliteId();
		for (String satelliteId : satelliteIdList) {
			// 卫星两行根数
			SatelliteTle tle = satelliteService.getSatelliteTle(satelliteId);
			// 把两行根数提供给SGP4计算使用
		    PredictFactory.registerSatellite(satelliteId, new TLE(tle.getTle()));
		    // 把两行根数提供给SGP4计算(GPS)位置和速度
		    TlePredictionFactory.registerTle(satelliteId, new Tle(tle.getTleLine1(), tle.getTleLine2()));
		}
		
		// 地面站
		List<String> groundStationIdList = groundStationService.listGroundStationId();
		for (String groundStationId : groundStationIdList) {
			// 地面站地理位置信息
			GroundStationInfo info = groundStationService.getGroundStation(groundStationId);
			GroundStationPosition groundStationPosition = new GroundStationPosition(info.getGroundStationLat(), info.getGroundStationLng(), info.getGroundStationAlt());
		    PredictFactory.registerGroundStation(info.getGroundStationId(), groundStationPosition);
		}
		
		
		System.out.println("******************预加载完成***********************************");
		
	}

}
