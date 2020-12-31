package com.hywx.common.security.annotation;

import com.hywx.common.security.configure.CloudResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author MrBird
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CloudResourceServerConfigure.class)
public @interface EnableCloudSecurity {
}
