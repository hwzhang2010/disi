package com.hywx.common.security.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.Exception.CommonResponse;
import com.hywx.common.security.constant.SecurityConstant;
import com.hywx.common.security.properties.CloudSecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MrBird
 */
public class ServerProtectInterceptor implements HandlerInterceptor {

    private CloudSecurityProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!properties.getOnlyFetchByGateway()) {
            return true;
        }
        String token = request.getHeader(CommonConstant.ZUUL_TOKEN_HEADER);
        String gatewayToken = new String(Base64Utils.encode(CommonConstant.ZUUL_TOKEN_VALUE.getBytes()));
        if (StringUtils.equals(gatewayToken, token)) {
            return true;
        } else {
            CommonResponse commonResponse = new CommonResponse();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(JSONObject.toJSONString(commonResponse.message("请通过网关获取资源")));
            return false;
        }
    }

    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }
}
