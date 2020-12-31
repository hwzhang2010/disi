package com.hywx.sisn.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hywx.sisn.bo.rc.RemoteCommand;
import com.hywx.sisn.bo.rc.RemoteCommandBack;
import com.hywx.sisn.config.FileConfig;
import com.hywx.sisn.config.MyConfig;
import com.hywx.sisn.global.GlobalConstant;
import com.hywx.sisn.global.GlobalQueue;
import com.hywx.sisn.net.SisnSender;
import com.hywx.sisn.struct.JavaStruct;
import com.hywx.sisn.struct.StructException;
import com.hywx.sisn.struct.data.BhHead;
import com.hywx.sisn.struct.data.RemoteControlData;
import com.hywx.sisn.net.SisnReceiver;
import com.hywx.sisn.util.ByteUtil;
import com.hywx.sisn.util.FileUtil;
import com.hywx.sisn.util.StringUtils;

/**
 * 创建多线程任务
 * @author zhang.huawei
 *
 */
@Service
@EnableAsync
public class ThreadService {
	
	@Autowired
	private FileConfig fileConfig;
	@Autowired
	private MyConfig myConfig;
    
	/**
	 * UDP组播接收
	 */
	@Async  //标注为异步任务，在执行时会单独开启线程来执行
	public void executeRecv() {
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
		SisnReceiver sisnRecv = new SisnReceiver(address);
		//SitmReceiver sitmRecv = new SitmReceiver(address, local);
		
		while (true) {
			try {
				//UDP组播接收
				sisnRecv.recv();
				
				//挂起1ms, 避免CPU长时间被占用
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 判断提取UDP组播接收的数据
	 */
	@Async
	public void executeJudge() {
		
		
		while (true) {
			try {
	        	if (GlobalQueue.getRecvQueue().isEmpty()) {  //当没有数据的时候
	        		//挂起1ms, 避免CPU长时间占用
					Thread.sleep(1); 
					continue;
	        	}
	        	
	        	byte[] data = GlobalQueue.getRecvQueue().take();
	        	
	        	//得到MID
	        	//int mid = ByteUtil.toUShort(data, 1, myConfig.isNet());
	        	//得到接收数据的卫星ID
	    		//String satelliteId = String.format("%04X", ByteUtil.toUShort(data, 1, myConfig.isNet()));
	        	
	        	//得到DID
	        	//long did = ByteUtil.toUInt(data, 7, myConfig.isNet());
	    		
	        	//得到BID
	        	long bid = ByteUtil.toUInt(data, 11, myConfig.isNet());
	        	//根据BID判断数据类别，并进行处理
	        	judgeAndProcess(bid, data);
	        	
	        	//System.out.println("----------did, bid: " + String.format("%08X, %08X", did, bid));
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}

    /**
     * 根据数据来源进行处理
     * @param bid
     * @param data
     */
	private void judgeAndProcess(long bid, byte[] data) {
		
		//判断BID
		if (bid == GlobalConstant.BID_RC) {  //外测提供的GPS数据，以此作为遥测源码发送的驱动。	
		    processRemoteCommand(data);
		} else {
		}
		
	}
	
	
	/**
     * 远控指令
     * @param data
     */
	private void processRemoteCommand(byte[] data) {
		//信息处理标识, 自己发送的固定填写0x7E, 避免UDP组播收到自己发送的后进行处理.
		byte flag = data[19];
		if (flag == 0x7E)
			return;
		
		if (data.length < GlobalConstant.FRAME_HEADER_LENGTH)
			return;
		
		byte[] header = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
        byte[] body = new byte[GlobalConstant.FRAME_RC_LENGTH];
        System.arraycopy(data, 0, header, 0, GlobalConstant.FRAME_HEADER_LENGTH);
        System.arraycopy(data, GlobalConstant.FRAME_HEADER_LENGTH, body, 0, data.length - GlobalConstant.FRAME_HEADER_LENGTH);
        
        //信息处理标识，固定填写0x7E
        header[19] = 0x7E;
        //把DID放到SID的位置, 把DID置为0
        header[3] = header[7];
        header[4] = header[8];
        header[5] = header[9];
        header[6] = header[10];
        header[7] = 0x0;
        header[8] = 0x0;
        header[9] = 0x0;
        header[10] = 0x0;
        
		try {
			String msg = new String(body, "utf-8");
			
			RemoteCommand remoteCommand = JSON.parseObject(msg, RemoteCommand.class);
            //BhHead bhHead = new BhHead();
            //JavaStruct.unpack(bhHead, header, ByteOrder.LITTLE_ENDIAN);
            //System.out.println("*******************" + ByteUtil.toHex(JavaStruct.pack(bhHead, ByteOrder.LITTLE_ENDIAN)));
            
            RemoteCommandBack remoteCommandBack = new RemoteCommandBack(remoteCommand.getId(), remoteCommand.getStationId(), remoteCommand.getEquipId(), remoteCommand.getCommandType(), "ECH", "");
            String remoteCommandBackJson = JSON.toJSONString(remoteCommandBack);
            byte[] back = StringUtils.string2byte(remoteCommandBackJson);
            //有效字节长度
            int length = back.length;
            header[30] = (byte) (length & 0xFF);
            header[31] = (byte) ((length >> 8) & 0xFF);
            
            ByteBuffer backBuffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + length);
            backBuffer.put(header);
            backBuffer.put(back);
           
            Thread.sleep(2000);
            
            //把生成的应答放入UDP组播发送队列
			GlobalQueue.getSendQueue().put(backBuffer);
			
			
			//int reply = new Random().nextInt(2) + 1;
            
            RemoteCommandBack remoteCommandBack2 = new RemoteCommandBack(remoteCommand.getId(), remoteCommand.getStationId(), remoteCommand.getEquipId(), remoteCommand.getCommandType(), "OK", "");
            String remoteCommandBackJson2 = JSON.toJSONString(remoteCommandBack2);
            byte[] back2 = StringUtils.string2byte(remoteCommandBackJson2);
            //有效字节长度
            int length2 = back2.length;
            header[30] = (byte) (length2 & 0xFF);
            header[31] = (byte) ((length2 >> 8) & 0xFF);
            
            ByteBuffer backBuffer2 = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + length2);
            backBuffer2.put(header);
            backBuffer2.put(back2);
            
            Thread.sleep(2000);
            
            //把生成的应答放入UDP组播发送队列
			GlobalQueue.getSendQueue().put(backBuffer2);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
		
		
		
		
			
		
		
	}

	
	
	
	

	/**
	 * UDP组播发送
	 * 当前做法：把所有的卫星遥测源码仿真加入到一个队列中，只要队列中有数据就进行发送
	 * 后续修改：可以考虑每个卫星一个队列，每隔1秒遍历发送队列，依次进行发送，以此保证时间间隔的精确性
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
		System.out.println("******************UDP multicast address(send): " + address + ", " + local);
		SisnSender sisnSend = new SisnSender(address.getHostString(), address.getPort());
		//SisnSender sisnSend = new SisnSender(address.getHostString(), address.getPort(), local);
		
		while (true) {
			try {
				//sitmSend.send(new byte[] { (byte)0xEB, (byte)0x90 });
				ByteBuffer buffer = GlobalQueue.getSendQueue().take();
				
				sisnSend.send(buffer.array());
				
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
