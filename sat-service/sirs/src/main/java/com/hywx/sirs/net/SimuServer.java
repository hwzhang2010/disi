package com.hywx.sirs.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.NetUtil;

public class SimuServer {
	//加入的组播地址
	private InetSocketAddress groupAddress;
	
	public SimuServer() {
	}

	public SimuServer(InetSocketAddress groupAddress) {
		this.groupAddress = groupAddress;
	}
	
	public void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
		    NetworkInterface ni = NetUtil.LOOPBACK_IF;
		    //InetAddress localAddress = InetAddress.getByName("192.168.1.122");
		    Enumeration<InetAddress> addresses = ni.getInetAddresses();
		    InetAddress localAddress = null;
		    while (addresses.hasMoreElements()) {
		    	InetAddress address = addresses.nextElement();
		    	if (address instanceof Inet4Address) {
		    		localAddress = address;
		    	}
		    }
		    System.out.println("***************" + localAddress);
		    
		    Bootstrap bootstrap = new Bootstrap();
		    bootstrap.group(group)
		             .channel(NioDatagramChannel.class)
		             .localAddress(localAddress, groupAddress.getPort())
		             .option(ChannelOption.IP_MULTICAST_ADDR, localAddress)
		             .option(ChannelOption.IP_MULTICAST_IF, ni)
		             .option(ChannelOption.SO_REUSEADDR, true)
		             .handler(new ChannelInitializer<NioDatagramChannel>() {

						@Override
						protected void initChannel(NioDatagramChannel ch) throws Exception {
							ch.pipeline().addLast(new SimuServerHandler());
						}});
		    
			NioDatagramChannel channel = (NioDatagramChannel) bootstrap.bind(groupAddress.getPort()).sync().channel();
			System.out.println("-----------------" + groupAddress);
			channel.joinGroup(groupAddress, ni).sync();
			channel.closeFuture().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
}
