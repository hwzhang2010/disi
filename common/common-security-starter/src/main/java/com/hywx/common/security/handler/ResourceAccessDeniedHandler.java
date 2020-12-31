package com.hywx.common.security.handler;

import com.hywx.common.core.util.Exception.CommonResponse;
import com.hywx.common.core.util.Exception.ResponseUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: common
 * @description: 用户无权限返回403
 * @author: tangjing
 * @create: 2020-03-04 17:26
 **/
@Configuration
public class ResourceAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        ResponseUtil.makeResponse(
                response, MediaType.APPLICATION_JSON_VALUE,
                HttpServletResponse.SC_FORBIDDEN, commonResponse.message("没有权限访问该资源"));
    }
}
