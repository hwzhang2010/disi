package com.hywx.sisl.global;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 数据队列：LinkedBlockingQueue使用两个独立的锁控制数据同步，队列会自动平衡负载。
 * LinkedBlockingQueue可以不设置队列容量，默认为Integer.MAX_VALUE.其容易造成内存溢出，一般要设置其值。
 * @author zhang.huawei
 *
 */
public class GlobalQueue {
	//队列容量
	private static final int QUEUE_SIZE = 1024;
	
	//组播接收数据队列
	private static LinkedBlockingQueue<byte[]> recvQueue;
	public static LinkedBlockingQueue<byte[]> getRecvQueue() {
		if (recvQueue == null)
			recvQueue = new LinkedBlockingQueue<byte[]>(QUEUE_SIZE);
		return recvQueue;
	}
	
	//组播发送数据队列
	private static LinkedBlockingQueue<ByteBuffer> sendQueue;
	public static LinkedBlockingQueue<ByteBuffer> getSendQueue() {
		if (sendQueue == null)
			sendQueue = new LinkedBlockingQueue<ByteBuffer>(QUEUE_SIZE);
		return sendQueue;
	}

	
	
}
