package com.hywx.sirs.service;


import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hywx.sirs.bo.StationData;
import com.hywx.sirs.bo.bid.AbstractBidOperation;
import com.hywx.sirs.bo.bid.BidOperationFactory;
import com.hywx.sirs.config.FileConfig;
import com.hywx.sirs.global.GlobalConstant;
import com.hywx.sirs.global.GlobalQueue;
import com.hywx.sirs.net.ExchangeClient;
import com.hywx.sirs.net.ExchangeServer;
import com.hywx.sirs.net.SimuReceiver;
import com.hywx.sirs.net.SimuSender;
import com.hywx.sirs.util.ByteUtil;
import com.hywx.sirs.util.FileUtil;

@Service
public class ThreadService {
//    @Async
//	  public void executeTask(String host, int port) {
//	  }
//    
//    @Async
//    public Future<String> excuteValueTask(String param) throws InterruptedException {
//    	Future<String> future = new AsyncResult<String>(param);
//    	return future;
//    }
	
	@Autowired
	private FileConfig fileConfig;
	
	/**
	 * 仿真UDP组播接收
	 * @param address
	 */
	@Async
	public void executeSimuRecv() {
		String multicastFileName = fileConfig.getLocalpathMulticast().concat(File.separator).concat(fileConfig.getFilenameMulticast());
		InetSocketAddress address = readMulticast(multicastFileName);
		if (address == null) {
		    address = new InetSocketAddress("224.1.2.3", 30001);
		}
		String local = readLocal(multicastFileName);
		if (local == null) {
			local = "127.0.0.1";
		}
		System.out.println("******************UDP multicast address(recv): " + address + ", " + local);
		SimuReceiver simuRecv = new SimuReceiver(address);
		//SimuReceiver simuRecv = new SimuReceiver(address, local);
		
		while (true) {
			try {
				//UDP组播接收
				simuRecv.recv();
				
				//挂起1ms, 避免CPU长时间被占用
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 把UDP组播接收的数据放入TCP发送缓冲队列
	 */
	@Async
	public void executeSimuShift() {
		while (true) {
			try {
	        	if (GlobalQueue.getSimuQueue().isEmpty()) {  //当没有数据的时候
	        		//挂起1ms, 避免CPU长时间占用
					Thread.sleep(1); 
					continue;
	        	}
	        	
	        	byte[] data = GlobalQueue.getSimuQueue().take();
        		long bid = ByteUtil.toUInt(data, 11, false);
        		//MID, 2字节16进制
        		int mid = ByteUtil.toUShort(data, 1, false);
        		//SID, 4字节16进制
        		String sid = String.format("%08X", ByteUtil.toUInt(data, 3, false));
        		//if (bid == GlobalConstant.BID_TM)
        		//    System.out.println("*******************statiod bid, mid, sid: *********" + Long.toHexString(bid) + ", " + Long.toHexString(mid) + ", " + sid);
        		
        		
        		AbstractBidOperation bidOperation = BidOperationFactory.getInstance(bid);
        		if (bidOperation != null) {
        			//构造站的数据，并放入站的缓冲区
        			bidOperation.operate(new StationData(sid, data));
        		}
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	/**
	 * 把TCP服务端接收的数据通过UDP组播发送
	 */
	@Async
	public void executeSimuSend() {
		String multicastFileName = fileConfig.getLocalpathMulticast().concat(File.separator).concat(fileConfig.getFilenameMulticast());
		InetSocketAddress address = readMulticast(multicastFileName);
		if (address == null) {
		    address = new InetSocketAddress("224.1.2.3", 30001);
		}
		String local = readLocal(multicastFileName);
		if (local == null) {
			local = "127.0.0.1";
		}
		System.out.println("******************UDP multicast address(send): " + address + ", " + local);
		SimuSender simuSend = new SimuSender(address.getHostString(), address.getPort());
		//SimuSender simuSend = new SimuSender(address.getHostString(), address.getPort(), local);
		
		while (true) {
			try {
	        	if (GlobalQueue.getExchangeQueue().isEmpty()) {  //当没有数据的时候
	        		//挂起1ms, 避免CPU长时间占用
					Thread.sleep(1); 
					continue;
	        	}
	        	
	        	byte[] data = GlobalQueue.getExchangeQueue().take();
	        	//UDP组播发送
                simuSend.send(data);
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
    /**
     * 数据交互TCP服务端监听	
     * @param portList
     */
	@Async
	public void executeExchangeServer(List<Integer> portList) {
		try {
			new ExchangeServer().listens(portList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	/**
//	 * 数据交互TCP客户端连接
//	 * @param host
//	 * @param port
//	 */
//	@Async
//	public void executeExchangeClient(String host, int port) {
//		new ExchangeClient().connect(host, port);
//	}
	
	/**
	 * 数据交互TCP客户端连接
	 * @param scode
	 * @param host
	 * @param port
	 */
	@Async
	public void executeExchangeClient(String scode, String host, int port) {
		new ExchangeClient(scode).connect(host, port);
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
