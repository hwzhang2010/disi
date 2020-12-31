package com.hywx.authservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hywx.authservice.dao.CasUser;
import com.hywx.authservice.properties.AuthProperties;
import com.hywx.authservice.provider.CasAuthenticationToken;
import com.hywx.authservice.service.CasLoginService;
import com.hywx.authservice.service.impl.RedisTemplateOps;
import com.hywx.common.core.util.CommonUtil;
import com.hywx.common.core.util.Exception.CommonException;
import com.hywx.common.core.util.URLParser;
import com.hywx.common.core.util.results.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-22 14:49
 **/
@Slf4j
@Controller
@RequestMapping("/sso")
public class CasLoginController {

    private static String queryString = "";
    static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    @Autowired
    private CasLoginService casLoginService;

    @Autowired
    private AuthProperties properties;

    @Autowired
    private RedisTemplateOps redisService;

    /**
     * 登录
     *
     */
    @ResponseBody
    @GetMapping("/login")
    public void ssoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, CommonException {
        // AuthRequest authRequest = socialLoginService.renderAuth(oauthType);
        this.queryString=request.getQueryString();
        HttpSessionRequestCache requestCache= new HttpSessionRequestCache();
        if(StringUtils.isNotEmpty(this.queryString) && this.queryString.contains("redirect_uri")){
            requestCache.saveRequest(request,null);
        }
        response.sendRedirect(properties.getCasLoginUrl());
    }

    /**
     * 登录成功后的回调
     * @return String
     */
    @GetMapping("/callback")
    public String ssoCallback(String code,String ticket, Model model, HttpServletRequest currentRequest) {
        try {
            if(StringUtils.isNotEmpty(ticket)){
                model.addAttribute("frontUrl", properties.getCasLoginUrl());
            }else {
                HttpSession session = currentRequest.getSession(false);
                if (session != null) {
                    DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) session.getAttribute(SAVED_REQUEST);
                    String frontUrl = defaultSavedRequest.getQueryString();
                    String redirctUrl = URLParser.fromQueryString(frontUrl).compile().getParameter("redirect_uri");
                    if (StringUtils.isNotEmpty(redirctUrl)) {
                        model.addAttribute("frontUrl", redirctUrl + "?code=" + code);
                    } else {
                        model.addAttribute("message", "登录成功");
                    }
                } else {
                    model.addAttribute("message", "登录成功");
                }

            }
            return "callback";
        } catch (Exception e) {
            String errorMessage = CommonUtil.containChinese(e.getMessage()) ? e.getMessage() : "单点登录失败";
            model.addAttribute("error", errorMessage);
            return "error";
        }
    }

    /**
     * 获取token
     * @return String
     */
    @ResponseBody
    @PostMapping("/profile")
    public Resp ssoProfile(String code, String client, String clientSercert){
        long startTime = System.currentTimeMillis();
        try {
            long endTime = System.currentTimeMillis();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            String url=properties.getCasTokenUrl()+"&code="+code;
            String resultMap = restTemplate.getForObject(url, String.class);
            String token=URLParser.fromQueryString(resultMap).compile().getParameter("access_token");
           // String token="AT-1-TBWXjuwq1lnEx8cROZZbU8N49vdyFkwX";
            String userUrl=properties.getCasProfileUrl()+"?access_token="+token;
            Map<String, Object> imageResultMap = restTemplate.getForObject(userUrl, Map.class);
            String objJson = JSONObject.toJSONString(imageResultMap.get("attributes"));
            CasUser casUser = JSON.parseObject(objJson, CasUser.class);
            if(casUser==null || StringUtils.isEmpty(casUser.getUsername())){
                casUser=new CasUser();
                casUser.setUsername(imageResultMap.get("id").toString());
            }
           OAuth2AccessToken tokenInfo =casLoginService.casLogin(casUser, client, clientSercert);

            redisService.set(tokenInfo.getValue(),token,(long)tokenInfo.getExpiresIn());

            log.info("获取token信息成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("获取token信息成功", Resp.LIST, tokenInfo);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("获取token信息失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("列表查询失败" + e.getMessage(), Resp.LIST, null);
        }

    }

    @RequestMapping("/logout")
    public void loginOut(HttpSession session, HttpServletResponse response)throws IOException {
        session.invalidate();
        //这个是直接退出，走的是默认退出方式
        response.sendRedirect(properties.getCasLogoutUrl());
    }

    /**
     * 获取token
     * @return String
     */
    @ResponseBody
    @GetMapping("/checkToken")
    public Resp checkToken(String token){
        long startTime = System.currentTimeMillis();
        try {
            long endTime = System.currentTimeMillis();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            // String token="AT-1-TBWXjuwq1lnEx8cROZZbU8N49vdyFkwX";
            String userUrl=properties.getCasProfileUrl()+"?access_token="+token;
            Map<String, Object> imageResultMap = restTemplate.getForObject(userUrl, Map.class);
            log.info("获取token信息成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("获取token信息成功", Resp.LIST, imageResultMap);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("获取token信息失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("列表查询失败" + e.getMessage(), Resp.LIST, false);
        }

    }

}
