package com.hywx.common.cas.annotation;

import com.hywx.common.cas.configure.CasFilterAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-23 10:27
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CasFilterAutoConfigure.class)
public @interface  EnableCasClient {

}
