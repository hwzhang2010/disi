package com.hywx.sirs.service;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hywx.sirs.config.FileConfig;
import com.hywx.sirs.global.GlobalConstant;
import com.hywx.sirs.global.GlobalMap;
import com.hywx.sirs.global.GlobalVector;
import com.hywx.sirs.net.SimuReceiver;
import com.hywx.sirs.util.CollectionUtil;
import com.hywx.sirs.util.FileUtil;
import com.hywx.sirs.vo.StationVO;

/*
 * 1.InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * 2.ApplicationRunner和CommandLineRunner接口，当springboot的main方法快要执行结束时会调用afterRefresh然后再调用callRunners来加载所有的实现ApplicationRunner和CommandLineRunner的类然后执行run方法来初始化所写的内容。
 * 3.ApplicationContext初始化或刷新完成后触发的事件：ContextRefreshedEvent类型ApplicationListener接口ApplicationListener<ContextRefreshedEvent>,然后重写onApplicationEvent方法。
 * 执行顺序：（spring bean初始化） –> spring事件ContextRefreshedEvent–> CommandLineRunner/ApplicationRunner
 * 实现ApplicationRunner和CommandLineRunner接口（建议）
 */
@Service
public class SimuService implements ApplicationRunner {
	@Autowired
	private FileConfig fileConfig;
	
	@Autowired
	private ThreadService threadService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
 
		//UDP组播接收
		threadService.executeSimuRecv();
		//UDP组播接收数据 -> TCP客户端发送数据
		threadService.executeSimuShift();
		//TCP服务端接收数据 -> UDP组播发送数据
		threadService.executeSimuSend();
		
		
		String stationFileName = fileConfig.getLocalpathStation().concat(File.separator).concat(fileConfig.getFilenameStation());
		List<StationVO> stationList = readStationList(stationFileName);
		if (stationList.isEmpty())
			return;
		
		System.out.println(Arrays.toString(stationList.toArray()));
		
		//前端显示数据列表
		GlobalVector.putExchange(stationList);
		
		List<Integer> portList = new ArrayList<>();
		for (StationVO station : stationList) {
			//构造站-数据Map
			GlobalMap.putStation(station.getScode());
			//构造站-址Map
			//GlobalMap.putStationAddress(new InetSocketAddress(station.getSendIP(), station.getSendPort()), station.getScode());
			//构造站-连接状态Map
			GlobalMap.putStationConnection(station.getScode(), false);
			
			//数据交互TCP服务端监听端口
			portList.add(station.getRecvPort());
		}
		
		//去掉重复的监听端口
		//List<Integer> listenPortList = CollectionUtil.removeDuplicate(portList);
		
		//启动数据交互TCP服务端
		threadService.executeExchangeServer(portList);
		
		//在线程池中启动数据交互TCP客户端
		for (StationVO station : stationList) {
			threadService.executeExchangeClient(station.getScode(), station.getSendIP(), station.getSendPort());
		}
		
		
	}
	
	private List<StationVO> readStationList(String fileName) {
        List<StationVO> list = new ArrayList<>();
		
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return list;
		
		JSONArray stationList = JSONArray.parseArray(jsonString);
		for (int i = 0; i < stationList.size(); i++) {
			String name = stationList.getJSONObject(i).getString("name");
			//去掉16进制的0x字符
			String scode = stationList.getJSONObject(i).getString("scode").replaceAll("^0[x|X]", "");
			String recvIP = stationList.getJSONObject(i).getString("recvIP");
			int recvPort = stationList.getJSONObject(i).getInteger("recvPort");
			String sendIP = stationList.getJSONObject(i).getString("sendIP");
			int sendPort = stationList.getJSONObject(i).getInteger("sendPort");
			
			list.add(new StationVO(name, scode, recvIP, recvPort, sendIP, sendPort));
		}
		
		return list;
	}


}
