package com.hywx.sisn.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

/**
 * UDP组播发送
 * @author zhang.huawei
 *
 */
public class SisnSender {
	//UDP组播socket
	private MulticastSocket socket;
	//UDP组播地址
	private String host;
	//UDP组播端口
	private int port;
	
	public SisnSender() {
	}
 
	public SisnSender(String host, int port) {
		this.host = host;
		this.port = port;
		
		this.socket = createMulticast();
	}
	
	public SisnSender(String host, int port, String local) {
		this.host = host;
		this.port = port;
		
		this.socket = createMulticast(local, port);
	}
	
	private MulticastSocket createMulticast() {
		try {
			//初始化组播类并关联端口
			MulticastSocket socket = new MulticastSocket(this.port);
			//设置组播数据报的发送范围为本地网络
			socket.setTimeToLive(4); 
			//设置端口复用
			socket.setReuseAddress(true);
			//禁用多播数据报的本地回送
			//socket.setLoopbackMode(false);
			//加入组播组
			//socket.joinGroup(groupAddress.getAddress());
			
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private MulticastSocket createMulticast(String local, int port) {
		try {
			//初始化组播类并关联端口, 同时指定网卡
			MulticastSocket socket = new MulticastSocket(new InetSocketAddress(local, port));
			//设置组播数据报的发送范围为本地网络
			socket.setTimeToLive(4); 
			//设置端口复用
			socket.setReuseAddress(true);
			//禁用多播数据报的本地回送
			//socket.setLoopbackMode(false);
			//加入组播组
			//socket.joinGroup(groupAddress.getAddress());
			
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public void send(byte[] data) {
		try {
			InetAddress address = InetAddress.getByName(this.host);
			DatagramPacket packet = new DatagramPacket(data, data.length, address, this.port);
			this.socket.send(packet);
			//System.out.println("****************udp multicast send:" + data.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}