package com.hywx.sirs.net;

import java.nio.ByteOrder;
import java.util.List;

import com.hywx.sirs.global.GlobalVector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//@Component
public class ExchangeServer {
	
	private static final ByteOrder BYTE_ORDER       = ByteOrder.LITTLE_ENDIAN;
    //最大长度
    private static final int MAX_FRAME_LENGTH       = 100000;
    //长度字段所占的字节数
    private static final int LENGTH_FIELD_LENGTH    = 2;
    //长度偏移
    private static final int LENGTH_FIELD_OFFSET    = 30;
    private static final int LENGTH_ADJUSTMENT      = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

	public ExchangeServer() {
	}
	
	public void listen(int port) throws Exception {
		//创建两个事件线程组，boss用于处理服务端接收客户端连接，work进行网络通信（网络读写）。
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		
		try {
			//服务端启动引导器
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, work)  //把事件线程组添加进启动引导器
			      .channel(NioServerSocketChannel.class)  //// 设置通道的建立方式, 采用Nio的通道方式来建立请求连接
			      .option(ChannelOption.SO_BACKLOG, 1024)  // 设置Tcp缓冲区
			      .option(ChannelOption.SO_RCVBUF, 32 * 1024)  // 设置接收缓冲大小
			      //.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 65535, 65535))
			      .childHandler(new ChannelInitializer<SocketChannel>() {
                    //构造一个由通道处理器构成的通道管道流水线
					@Override
					protected void initChannel(SocketChannel ch) {
						//配置具体数据接收方法的处理
						ch.pipeline().addLast("decoder", new MessagePacketByLengthDecoder(BYTE_ORDER,MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP,false));
						ch.pipeline().addLast(new ExchangeServerHandler());
					}
			    	  
			      });
			//服务端绑定端口并且开始接收进来的连接请求
			ChannelFuture future = bootstrap.bind(port).sync();
			if (future.isSuccess()) {
				GlobalVector.updateRecvState(port, "listened");
				System.out.println("exchange server start listen, @port: " + port);
			}
			//关闭服务端
			future.channel().closeFuture().sync();
		} finally {
			//关闭事件线程组
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	
	public void listens(List<Integer> portList) throws Exception {
		//创建两个事件线程组，boss用于处理服务端接收客户端连接，work进行网络通信（网络读写）。
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			//服务端启动引导器
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker)  //把事件线程组添加进启动引导器
			      .channel(NioServerSocketChannel.class)  //// 设置通道的建立方式, 采用Nio的通道方式来建立请求连接
			      .option(ChannelOption.SO_BACKLOG, 1024)  // 设置Tcp缓冲区
			      .option(ChannelOption.SO_RCVBUF, 32 * 1024)  // 设置接收缓冲大小
			      .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 65535, 65535))
			      .childHandler(new ChannelInitializer<SocketChannel>() {
                    //构造一个由通道处理器构成的通道管道流水线
					@Override
					protected void initChannel(SocketChannel ch) {
						//配置具体数据接收方法的处理
						//ch.pipeline().addLast("decoder", new MessagePacketByLengthDecoder(BYTE_ORDER,MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP,false));
						ch.pipeline().addLast(new ExchangeServerHandler());
					}
			    	  
			      });
			
			for (Integer port : portList) {
				//服务端绑定端口并且开始接收进来的连接请求
				ChannelFuture future = bootstrap.bind(port).sync();
				//监听事件
				future.channel().closeFuture().addListener(new ChannelFutureListener() {
	                
					@Override 
	                public void operationComplete(ChannelFuture future) throws Exception {   
	                	//通过回调只关闭自己监听的channel
	                    future.channel().close();
	                }
	            });
				GlobalVector.updateRecvState(port, "listened");
				System.out.println("exchange server start listen, @port: " + port);
			}
			
			//关闭服务端
			//future.channel().closeFuture().sync();
			
		} finally {
			//关闭事件线程组
			//因为上面没有阻塞了，不注释的话，这里会直接关闭的
			//boss.shutdownGracefully();
			//work.shutdownGracefully();
		}
	}
	
}
