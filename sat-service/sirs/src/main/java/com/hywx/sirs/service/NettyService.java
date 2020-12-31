package com.hywx.sirs.service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.hywx.sirs.net.ExchangeClient;
import com.hywx.sirs.net.ExchangeServer;
import com.hywx.sirs.net.SimuReceiver;
import com.hywx.sirs.net.SimuServer;

/*
 * 1.InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * 2.ApplicationRunner和CommandLineRunner接口，当springboot的main方法快要执行结束时会调用afterRefresh然后再调用callRunners来加载所有的实现ApplicationRunner和CommandLineRunner的类然后执行run方法来初始化所写的内容。
 * 3.ApplicationContext初始化或刷新完成后触发的事件：ContextRefreshedEvent类型ApplicationListener接口ApplicationListener<ContextRefreshedEvent>,然后重写onApplicationEvent方法。
 * 执行顺序：（spring bean初始化） –> spring事件ContextRefreshedEvent–> CommandLineRunner/ApplicationRunner
 * 实现ApplicationRunner和CommandLineRunner接口（建议）
 */
//@Service
public class NettyService implements ApplicationRunner {
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//数据交互服务端：Tcp
//		ExchangeServer exchangeServer = new ExchangeServer();
//		exchangeServer.listen(8801);
//		List<Integer> portList = new ArrayList<>();
//		portList.add(8801);
//		portList.add(8802);
//	    ExchangeServer exchangeServer = new ExchangeServer();
//	    exchangeServer.listens(portList);
		
		//数据交互客户端：Tcp
//		ExchangeClient exchangeClient = new ExchangeClient();
//      exchangeClient.connect("192.168.1.122", 8802);
//      ExchangeClient exchangeClient2 = new ExchangeClient();
//      exchangeClient2.connect("192.168.1.122", 8801);
		
		
		InetSocketAddress address = new InetSocketAddress("224.1.2.3", 30001);
		SimuServer simuServer = new SimuServer(address);
		simuServer.start();
		
//		InetSocketAddress address = new InetSocketAddress("224.1.2.3", 30001);
//		SimuMulticast simuMulticast = new SimuMulticast(address); 
//		//simuMulticast.send(new byte[] { (byte)0xEB, (byte)0x90 });

		
	}
}
