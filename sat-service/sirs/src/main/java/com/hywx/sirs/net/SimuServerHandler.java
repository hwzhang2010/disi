package com.hywx.sirs.net;

import java.net.InetSocketAddress;
import java.util.Arrays;

import com.hywx.sirs.global.GlobalQueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

public class SimuServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    //帧头字节长度
    private final int FRAME_HEADER_LENGTH = 32;
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		InetSocketAddress address = msg.sender();
		System.out.println("*************" + address.getAddress() + ", " + address.getPort());
		ByteBuf buf = msg.content();
		System.out.println("*************" + ByteBufUtil.hexDump(buf));
		
//		ByteBuf buf = msg.content();
//		byte[] packet = new byte[buf.readableBytes()];
//		buf.readBytes(packet);
//		if (packet.length > FRAME_HEADER_LENGTH) {
//			//接收的数据放入队列
//			GlobalQueue.getSimuQueue().put(packet); 
//		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//异常捕获
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
