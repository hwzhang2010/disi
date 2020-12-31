package com.hywx.sisn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;



/*
 * 1.InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。
 * 2.ApplicationRunner和CommandLineRunner接口，当springboot的main方法快要执行结束时会调用afterRefresh然后再调用callRunners来加载所有的实现ApplicationRunner和CommandLineRunner的类然后执行run方法来初始化所写的内容。
 * 3.ApplicationContext初始化或刷新完成后触发的事件：ContextRefreshedEvent类型ApplicationListener接口ApplicationListener<ContextRefreshedEvent>,然后重写onApplicationEvent方法。
 * 执行顺序：（spring bean初始化） –> spring事件ContextRefreshedEvent–> CommandLineRunner/ApplicationRunner
 * 实现ApplicationRunner和CommandLineRunner接口（建议）
 */
@Service
@Order(1)
public class SisnLoadService  implements ApplicationRunner {
	private static final Logger log = LoggerFactory.getLogger(SisnLoadService.class);
	
	@Autowired
	private ThreadService threadService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		//UDP组播接收
		threadService.executeRecv();
		//UDP组播接收后的判断提取处理
		threadService.executeJudge();
		
		//UDP组播发送
		threadService.executeSend();
		
		System.out.println("******************多线程(UDP组播接收 + 判断提取、UDP组播发送)启动**************");
	}
	


}
