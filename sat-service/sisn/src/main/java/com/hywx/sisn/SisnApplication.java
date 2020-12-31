package com.hywx.sisn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SisnApplication {

	public static void main(String[] args) {
		//只使用IPV4, 不使用IPV6
		System.setProperty("java.net.preferIPv4Stack", "true");
				
		SpringApplication.run(SisnApplication.class, args);
	}

}
