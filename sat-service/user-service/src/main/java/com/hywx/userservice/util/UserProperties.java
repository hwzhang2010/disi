package com.hywx.userservice.util;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-12 13:59
 **/
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:user.properties"})
@ConfigurationProperties(prefix = "gw.user")
public class UserProperties {
    /**
     * 免认证 URI，多个值的话以逗号分隔
     */
    private String anonUrl;
    /**
     * 批量插入当批次可插入的最大值
     */
    private Integer batchInsertMaxNum = 1000;
}

