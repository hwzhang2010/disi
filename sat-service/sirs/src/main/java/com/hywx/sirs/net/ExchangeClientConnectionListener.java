package com.hywx.sirs.net;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

/**
 * TCP客户端，监测启动时是否连接成功，如果不成功进行重连
 * @author zhang.huawei
 *
 */
public class ExchangeClientConnectionListener implements ChannelFutureListener {
	private ExchangeClient client;
	
	public ExchangeClientConnectionListener(ExchangeClient client) {
		this.client = client;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (future.isSuccess()) {
			System.out.println(LocalDateTime.now().toString() + ": exchange client is established.");
		} else {
			System.out.println(LocalDateTime.now().toString() + ": exchange client not established, try to reconnect...");
			
			final EventLoop loop = future.channel().eventLoop();
			loop.schedule(new Runnable() {

				@Override
				public void run() {
					//启动时断线重连
					client.createBootstrap(new Bootstrap(), loop);
				}}, 
					
				60L, 
				TimeUnit.SECONDS);
		}

	}

}
