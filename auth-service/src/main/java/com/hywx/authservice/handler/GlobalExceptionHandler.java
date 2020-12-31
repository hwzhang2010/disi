package com.hywx.authservice.handler;

import com.hywx.common.core.handler.BaseExceptionHandler;
import com.hywx.common.core.util.Exception.CommonResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * @author tangjing
 * @Description: 
 * @Date 2020/6/5 下午 04:34
 * @Param null
 * @return 
 * @throws: 
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResponse handleAccessDeniedException() {
        return new CommonResponse().message("没有权限访问该资源");
    }
}
