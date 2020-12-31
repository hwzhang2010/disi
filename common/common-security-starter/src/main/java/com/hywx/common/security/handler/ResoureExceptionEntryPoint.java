package com.hywx.common.security.handler;

import com.hywx.common.core.util.Exception.CommonResponse;
import com.hywx.common.core.util.Exception.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: common
 * @description: 处理资源服务器异常 令牌不正确返回401
 * @author: tangjing
 * @create: 2020-03-04 17:18
 **/
@Slf4j
@Configuration
public class ResoureExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "token不合法";
        if (StringUtils.containsIgnoreCase(message, "Invalid access token")) {
            message = "访问令牌不正确";
        }
        if (StringUtils.containsIgnoreCase(message, "Full authentication is required to access this resource")) {
            message = "请求头client信息不正确，烦请核对";
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        ResponseUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE, status, new CommonResponse().message(message));
    }
}
