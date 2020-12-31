package com.hywx.sirs.global;

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
	private static LinkedBlockingQueue<byte[]> simuQueue;
	public static LinkedBlockingQueue<byte[]> getSimuQueue() {
		if (simuQueue == null)
		    simuQueue = new LinkedBlockingQueue<byte[]>(QUEUE_SIZE);
		return simuQueue;
	}
	
	//TCP服务端接收数据队列
	private static LinkedBlockingQueue<byte[]> exchangeQueue;
	public static LinkedBlockingQueue<byte[]> getExchangeQueue() {
		if (exchangeQueue == null)
			exchangeQueue = new LinkedBlockingQueue<byte[]>(QUEUE_SIZE);
		return exchangeQueue;
	}
}
