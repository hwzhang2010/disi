package com.hywx.sirs.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

//@Component
public class ExchangeClient {
	//线程事件组
	private EventLoopGroup worker = new NioEventLoopGroup();
	
	//服务端主机地址
	private String host;
	//服务端端口
	private int port;
	//站地址
	private String scode;

	public String getScode() {
		return scode;
	}

	public ExchangeClient() {
	}
	
	public ExchangeClient(String scode) {
		this.scode = scode;
	}

	public ExchangeClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	
	public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup group) {
		//客户端启动引导器
		if (bootstrap != null) {
			//客户端通道处理器
			final ExchangeClientHandler handler = new ExchangeClientHandler(this);
			bootstrap.group(group)  //把事件线程组添加进启动引导器
			         .channel(NioSocketChannel.class)  // 设置通道的建立方式, 采用Nio的通道方式来建立请求连接
			         .option(ChannelOption.TCP_NODELAY, true)
			         .option(ChannelOption.SO_KEEPALIVE, true)
			         .handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(handler);
							
						}});
			
			//客户端绑定端口并且开始发起连接请求，并注册客户端连接监听事件
			bootstrap.connect(host, port)
			         .addListener(new ExchangeClientConnectionListener(this));
		}
		
		return bootstrap;
	}
	
	
	public void connect(String host, int port) {
		//服务端主机地址和端口
		this.host = host;
		this.port = port;
		
		//客户端使用1个线程事件组请求连接服务端，和进行数据收发处理
		createBootstrap(new Bootstrap(), worker);
	}
	
	public void close() {
		//关闭事件线程组
		worker.shutdownGracefully();
	}
	
	
}
