package com.hywx.sisl.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import com.hywx.sisl.global.GlobalQueue;

/**
 * UDP组播接收
 * @author zhang.huawei
 *
 */
public class SislReceiver {
	//UDP组播socket
	private MulticastSocket socket;
	
	//接收缓冲区
    private byte[] recvBuf; 
    //接收缓冲区大小
    private final int RECV_BUFFER_SIZE = 4096;
    //帧头字节长度
    private final int FRAME_HEADER_LENGTH = 32;
	
	public SislReceiver() {
    }

	public SislReceiver(InetSocketAddress groupAddress) {
		this.socket = createMulticast(groupAddress);
		this.recvBuf = new byte[RECV_BUFFER_SIZE];
	}
	
	public SislReceiver(InetSocketAddress groupAddress, String local) {
		this.socket = createMulticast(groupAddress, local);
		this.recvBuf = new byte[RECV_BUFFER_SIZE];
	}
	
	private MulticastSocket createMulticast(InetSocketAddress groupAddress) {
		try {
			//初始化组播类并关联端口
			MulticastSocket socket = new MulticastSocket(groupAddress.getPort());
			//设置组播数据报的发送范围为本地网络
			socket.setTimeToLive(4); 
			//设置端口复用
			socket.setReuseAddress(true);
			//禁用多播数据报的本地回送
			//socket.setLoopbackMode(false);
			//加入组播组
			socket.joinGroup(groupAddress.getAddress());
			
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private MulticastSocket createMulticast(InetSocketAddress groupAddress, String local) {
		try {
			//初始化组播类并关联端口, 同时指定网卡
			MulticastSocket socket = new MulticastSocket(new InetSocketAddress(local, groupAddress.getPort()));
			//设置组播数据报的发送范围为本地网络
			socket.setTimeToLive(4); 
			//设置端口复用
			socket.setReuseAddress(true);
			//禁用多播数据报的本地回送
			//socket.setLoopbackMode(false);
			//加入组播组
			socket.joinGroup(groupAddress.getAddress());
			
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void closeMulticast() {
		if (this.socket != null)
			this.socket.close();
	}
	
	public void recv() {
		
		//接收缓冲区清零
		Arrays.fill(recvBuf, (byte)0);
		//接收数据报
		DatagramPacket packet = new DatagramPacket(recvBuf, RECV_BUFFER_SIZE); 
		try {
			this.socket.receive(packet);
			byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
			//System.out.println("*************udp multicast recv: " + packet.getLength() + ", " + ByteUtil.toHex(data));
			
			if (packet.getLength() > FRAME_HEADER_LENGTH) {
				//接收的数据放入队列
				GlobalQueue.getRecvQueue().put(data); 
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	
	public void send(byte[] data, InetSocketAddress groupAddress) {
		DatagramPacket packet = new DatagramPacket(data, data.length, groupAddress);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
