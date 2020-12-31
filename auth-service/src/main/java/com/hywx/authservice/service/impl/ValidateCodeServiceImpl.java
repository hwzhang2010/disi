package com.hywx.authservice.service.impl;


import com.hywx.authservice.properties.AuthProperties;
import com.hywx.authservice.properties.ValidateCodeProperties;
import com.hywx.authservice.util.CommonConstant;
import com.hywx.common.core.util.Exception.CommonException;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-06 08:45
 **/
@Service
public class ValidateCodeServiceImpl {

    @Autowired
    private RedisTemplateOps redisService;
    @Autowired
    private AuthProperties properties;

    /**
     * 生成验证码
     *
     * @param request  HttpServletRequest  key前端上送一定算法生成的随机字符串来和验证码一一对应
     * @param response HttpServletResponse
     */
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, CommonException {
        String key = request.getParameter("key");
        if (StringUtils.isBlank(key)) {
            throw new CommonException("验证码key不能为空");
        }
        ValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
        redisService.set(CommonConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        captcha.out(response.getOutputStream());
    }

    /**
     * 校验验证码
     *
     * @param key   前端上送一定算法生成的随机字符串来和验证码一一对应
     * @param value 前端上送待校验值
     */
    public void check(String key, String value) throws CommonException {
        Object codeInRedis = redisService.get(CommonConstant.CODE_PREFIX + key);
        if (StringUtils.isBlank(value)) {
            throw new CommonException("请输入验证码");
        }
        if (codeInRedis == null) {
            throw new CommonException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
            throw new CommonException("验证码不正确");
        }
    }

    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if (StringUtils.equalsIgnoreCase(code.getType(), CommonConstant.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, CommonConstant.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
