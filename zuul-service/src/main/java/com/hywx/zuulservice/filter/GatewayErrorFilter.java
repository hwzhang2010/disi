package com.hywx.zuulservice.filter;

import com.hywx.zuulservice.Exception.AuthResponse;
import com.hywx.zuulservice.Exception.ResponseUtil;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gateway
 * @description: 处理Zuul异常
 * @author: tangjing
 * @create: 2020-03-05 14:04
 **/
@Slf4j
@Component
public class GatewayErrorFilter extends SendErrorFilter {

    @Override
    public Object run() {
        try {
            AuthResponse authResponse = new AuthResponse();
            RequestContext ctx = RequestContext.getCurrentContext();
            String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

            ExceptionHolder exception = findZuulException(ctx.getThrowable());
            String errorCause = exception.getErrorCause();
            Throwable throwable = exception.getThrowable();
            String message = throwable.getMessage();
            message = StringUtils.isBlank(message) ? errorCause : message;
            authResponse = resolveExceptionMessage(message, serviceId, authResponse);

            HttpServletResponse response = ctx.getResponse();
            ResponseUtil.makeResponse(
                    response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, authResponse
            );
            log.error("Zull sendError：{}", authResponse.getMessage());
        } catch (Exception ex) {
            log.error("Zuul sendError", ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    private AuthResponse resolveExceptionMessage(String message, String serviceId,  AuthResponse authResponse) {
        if (StringUtils.containsIgnoreCase(message, "time out")) {
            return authResponse.message("请求" + serviceId + "服务超时");
        }
        if (StringUtils.containsIgnoreCase(message, "forwarding error")) {
            return authResponse.message(serviceId + "服务不可用");
        }
        return authResponse.message("Zuul请求" + serviceId + "服务异常");
    }
}
