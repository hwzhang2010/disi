package com.hywx.sitm;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Order(3)
public class SitmApplication implements ApplicationRunner {

	public static void main(String[] args) {
		//只使用IPV4, 不使用IPV6
		System.setProperty("java.net.preferIPv4Stack", "true");
				
		//SpringApplication.run(SitmApplication.class, args);
		//ConfigurableApplicationContext context = SpringApplication.run(SitmApplication.class, args);
		//context.close();
		
		new SpringApplicationBuilder(SitmApplication.class).web(WebApplicationType.NONE) // .REACTIVE, .SERVLET 
                                                           .bannerMode(Banner.Mode.OFF) 
                                                           .run(args); 
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		List<String> groupList = args.getOptionValues("group");
		if (groupList == null || groupList.isEmpty()) {
		    System.out.println("************卫星分组参数未配置, 应用程序退出.************");
		    return;
		} else {
			System.out.println("************卫星分组参数: --group=" + groupList.get(0));
			//阻塞主线程
			Thread.currentThread().join();
        }
 
        System.exit(0);
	}

}
