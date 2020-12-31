package com.hywx.common.cas.configure;

import com.hywx.common.cas.common.CASUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @program: sso
 * @description:
 * @author: tangjing
 * @create: 2020-07-07 17:03
 **/
public class LocalUserInfoFilter implements Filter {

    Logger logger =  LoggerFactory.getLogger(LocalUserInfoFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request_ = (HttpServletRequest)request;
        String loginName = CASUtil.getAccountNameFromCas(request_);
        if(!StringUtils.isEmpty(loginName)){
            request_.getSession().setAttribute("loginName", loginName);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

