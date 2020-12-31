package com.hywx.userservice.config.mybatisPlusConfig;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-12 15:13
 **/
@EnableTransactionManagement
@Configuration
public class MybatisPlusPageConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}