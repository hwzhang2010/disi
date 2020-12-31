package com.hywx.sirs.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.hywx.sirs.config.FileConfig;
import com.hywx.sirs.net.ExchangeClient;
import com.hywx.sirs.util.FileUtil;
import com.hywx.sirs.vo.StationVO;

//@Component
public class ExchangeClientMap {
	@Autowired
	private FileConfig fileConfig;
	
	private Map<Integer, String> exchangeSendPortMap;
	private Map<String, Boolean> exchangeConnectMap;
	private Map<String, ExchangeClient> exchangeClientMap;

	public Map<Integer, String> getExchangeSendPortMap() {
		return exchangeSendPortMap;
	}

	public void setExchangeSendPortMap(Map<Integer, String> exchangeSendPortMap) {
		this.exchangeSendPortMap = exchangeSendPortMap;
	}

	public Map<String, Boolean> getExchangeConnectMap() {
		return exchangeConnectMap;
	}

	public void setExchangeConnectMap(Map<String, Boolean> exchangeConnectMap) {
		this.exchangeConnectMap = exchangeConnectMap;
	}

	public Map<String, ExchangeClient> getExchangeClientMap() {
		return exchangeClientMap;
	}

	public ExchangeClientMap() {
	}
	
	public void initialize() {
		String localFileName = fileConfig.getLocalpathStation().concat(File.separator).concat(fileConfig.getFilenameStation());
		List<StationVO> stationList = readStationList(localFileName);
		if (stationList == null || stationList.isEmpty())
			return;
		
		System.out.println(Arrays.toString(stationList.toArray()));
		exchangeSendPortMap = new HashMap<>();
		exchangeConnectMap = new HashMap<>();
		exchangeClientMap = new HashMap<>();
		for (int i = 0; i < stationList.size(); i++) {
			//数据交互发送端口
			exchangeSendPortMap.put(stationList.get(i).getSendPort(), "");
			//数据交互客户端连接状态：默认false
			exchangeConnectMap.put(stationList.get(i).getScode(), false);
			//数据交互客户端：Tcp
			ExchangeClient exchangeClient = new ExchangeClient(stationList.get(i).getSendIP(), stationList.get(i).getSendPort());
			exchangeClientMap.put(stationList.get(i).getScode(), exchangeClient);
		}
	}


	private List<StationVO> readStationList(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return null;
		
		List<StationVO> list = new ArrayList<>();
		
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
