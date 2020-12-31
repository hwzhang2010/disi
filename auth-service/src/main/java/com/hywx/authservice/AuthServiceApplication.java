package com.hywx.authservice;

import java.util.TimeZone;

import com.hywx.common.security.annotation.EnableCloudSecurity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.TimeZone;
/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-05 15:09
 **/
@SpringBootApplication
@EnableCloudSecurity
@EnableDiscoveryClient
@MapperScan("com.hywx.authservice.mapper")
public class AuthServiceApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
