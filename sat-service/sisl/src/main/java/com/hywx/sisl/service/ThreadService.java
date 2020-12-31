package com.hywx.sisl.service;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sisl.config.FileConfig;
import com.hywx.sisl.global.GlobalQueue;
import com.hywx.sisl.net.SislSender;
import com.hywx.sisl.util.FileUtil;

/**
 * 创建多线程任务
 * @author zhang.huawei
 *
 */
@Service
@EnableAsync
//@DependsOn("sislPreloadService")
public class ThreadService {

	@Autowired
	private FileConfig fileConfig;
    
	
	

	/**
	 * UDP组播发送
	 * 当前做法：把所有的卫星和地面站的外测(测距测速、测角)仿真加入到一个队列中，只要队列中有数据就进行发送
	 * @throws IOException 
	 */
	@Async  //标注为异步任务，在执行时会单独开启线程来执行
	public void executeSend() throws IOException {
		String multicastFileName = fileConfig.getLocalpathMulticast().concat(File.separator).concat(fileConfig.getFilenameMulticast());
		InetSocketAddress address = readMulticast(multicastFileName);
		if (address == null) {
		    address = new InetSocketAddress("224.1.2.3", 30001);
		}
		String local = readLocal(multicastFileName);
		if (local == null) {
			local = "127.0.0.1";
		}
		System.out.println("******************UDP multicast address: " + address + ", " + local);
		SislSender sislSend = new SislSender(address.getHostString(), address.getPort());
		//SislSender sislSend = new SislSender(address.getHostString(), address.getPort(), local);
		
		while (true) {
			try {
				//sislSend.send(new byte[] { (byte)0xEB, (byte)0x90 });
				ByteBuffer buffer = GlobalQueue.getSendQueue().take();
				sislSend.send(buffer.array());
				
				//挂起1ms, 节省CPU
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	/**
	 * UDP组播配置文件: 组播地址和端口
	 * @param fileName
	 * @return
	 */
	private InetSocketAddress readMulticast(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return null;
		
		JSONObject multicast = JSONObject.parseObject(jsonString);
		String address = multicast.getString("address");
		int port = multicast.getInteger("port");
		
		return new InetSocketAddress(address, port);
	}
	
	/**
	 * UDP组播配置文件: 本地地址(网卡)
	 * @param fileName
	 * @return
	 */
	private String readLocal(String fileName) {
		String jsonString = FileUtil.getJson(fileName);
		if (jsonString == null || jsonString.isEmpty())
			return null;
		
		JSONObject multicast = JSONObject.parseObject(jsonString);
		String local = multicast.getString("local");
		
		return local;
	}
	
	
}
