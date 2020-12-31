package com.hywx.sirs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SirsApplication {

	public static void main(String[] args) {
		//只使用IPV4, 不使用IPV6
		System.setProperty("java.net.preferIPv4Stack", "true");
				
		SpringApplication.run(SirsApplication.class, args);
		//ConfigurableApplicationContext context = SpringApplication.run(SirsApplication.class, args);
		//context.close();
	}

}
