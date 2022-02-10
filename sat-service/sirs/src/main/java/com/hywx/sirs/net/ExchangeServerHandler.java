package com.hywx.sirs.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hywx.sirs.global.GlobalQueue;
import com.hywx.sirs.global.GlobalVector;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ExchangeServerHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//连接成功后，自动执行该方法
		SocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		System.out.println("exchange server handler channel active: " + address);
		
		GlobalVector.updateRecvState(port, "listening");
    }
	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		System.out.println("exchange server handler channel inactive: " + address);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
		GlobalVector.updateRecvCount(port);
		
		try {
			//读取消息数据
			ByteBuf buf = (ByteBuf) msg;
			byte[] request = new byte[buf.readableBytes()];
			buf.readBytes(request);
			//String response = new String(request, "utf-8");
			//消息格式必须是ByteBuf
			//ctx.writeAndFlush(Unpooled.copiedBuffer(request));
//			if (request.length == 2) {  // 心跳, 0x55, 0xcc
//				ctx.writeAndFlush(Unpooled.copiedBuffer(request));
//			}
			
//			if (request.length > 2) {
//			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
//			    System.out.println("************tcp server recv:" + formatter.format(LocalDateTime.now()) + request.length + ", " + ByteBufUtil.hexDump(request));
//			}	
			
			
			GlobalQueue.getExchangeQueue().put(request);
			//GlobalQueue.getExchangeQueue().poll();
			
			
		} finally {
			//buf.release();
     		ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("exchange server handler channel read complete.");
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//System.out.println("exchange server handler channel exception");
		
		//异常捕获
		cause.printStackTrace();
		ctx.close();
	}
}
