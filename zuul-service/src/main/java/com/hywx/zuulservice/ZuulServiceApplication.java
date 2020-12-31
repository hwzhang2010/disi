package com.hywx.zuulservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import java.util.*;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.hywx.zuulservice.mapper")
public class ZuulServiceApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(ZuulServiceApplication.class, args);
	}
}
