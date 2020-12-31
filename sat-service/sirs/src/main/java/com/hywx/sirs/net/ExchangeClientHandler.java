package com.hywx.sirs.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.hywx.sirs.global.GlobalMap;
import com.hywx.sirs.global.GlobalVector;
import com.hywx.sirs.util.ByteUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

public class ExchangeClientHandler extends ChannelInboundHandlerAdapter {
	private ExchangeClient client;
	private boolean isActived;
	
	public ExchangeClientHandler(ExchangeClient client) {
		this.client = client;
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		System.out.println("exchange client handler channel active: " + address);
		
    	//连接成功后，自动执行该方法
    	//ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[] { (byte)0xAA, (byte)0xBB });
        //ctx.channel().writeAndFlush(byteBuf);
			
		isActived = true;
		GlobalMap.putStationConnection(client.getScode(), true);
		GlobalVector.updateSendState(client.getScode(), "established");
		channelWrite(ctx, address);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
    	System.out.println("exchange client handler channel inactive: " + address);
    	
    	isActived = false;
    	GlobalMap.putStationConnection(client.getScode(), false);
    	GlobalVector.updateSendState(client.getScode(), "reconnecting");
    	final EventLoop loop = ctx.channel().eventLoop();
    	loop.schedule(new Runnable() {

			@Override
			public void run() {
				//运行过程中断线重连
				client.createBootstrap(new Bootstrap(), loop);
			}}, 
    			
    		60L, 
    		TimeUnit.SECONDS);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	//消息转换成ByteBuf
//    	ByteBuf buf = (ByteBuf) msg;
//    	try {
//    		byte[] request = new byte[buf.readableBytes()];
//    		buf.readBytes(request);
//    		ctx.writeAndFlush(Unpooled.wrappedBuffer(request));
//    	} finally {
//    		//buf.release();
//    		ReferenceCountUtil.release(msg);
//    	}
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	//System.out.println("exchange client handler channel read complete.");
		ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	System.out.println("exchange client handler channel exception");
    	
    	//异常捕获
    	cause.printStackTrace();
    	ctx.close();
    	
    }
    
    
    public void channelWrite(ChannelHandlerContext ctx, InetSocketAddress address) {
    	Executors.newSingleThreadExecutor().submit(new MsgRunnable(ctx, client.getScode()));
    }
    
    class MsgRunnable implements Runnable {
    	ChannelHandlerContext ctx;
    	String scode;

		public MsgRunnable(ChannelHandlerContext ctx, String scode) {
			this.ctx = ctx;
			this.scode = scode;
		}

		@Override
		public void run() {
			while (isActived) {
				try {
					//休眠1ms, 避免一直占用CPU
					TimeUnit.MILLISECONDS.sleep(1);
					
					byte[] data = GlobalMap.getStationData(scode);
					if (data == null)
						continue;
					//System.out.println("********sending***" + ByteUtil.toHex(data, 0, 32));
					ByteBuf msg = Unpooled.buffer(data.length);
					msg.writeBytes(data);
					ctx.writeAndFlush(msg);
					
					//GlobalVector.updateSendCount(scode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
				 
			}
		}
    	
    }
    
    
}
