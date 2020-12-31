package com.hywx.sisl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @ClassName:  SwaggerConfig
 * @Description:  Swagger API 配置文档类
 * @author zy
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String controllerStr = "com.hywx.sisl.controller";
    private String titleStr = "元数据微服务 RESTful API";
    private String descriptionStr = "RESTful API 文档(数字仿真分系统--外测接口)";
    private String versionStr = "0.0.1";
    private String autherStr = "zhang.huawei";
    private String autherUrl = "";
    private String autherEmail = "";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("sisl")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(controllerStr))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(titleStr)
                .contact(new Contact(autherStr, autherUrl, autherEmail))
                .description(descriptionStr)
                .version(versionStr)
                .build();

    }
}
