package com.hywx.authservice.controller;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hywx.authservice.bo.Oauth2TokenInfo;
import com.hywx.authservice.dao.OAuthClientDetails;
import com.hywx.authservice.dao.User;
import com.hywx.authservice.mapper.UserMapper;
import com.hywx.authservice.service.OAuthClientDetailsService;
import com.hywx.authservice.service.impl.RedisClientDetailsServiceImpl;
import com.hywx.authservice.service.impl.RedisTemplateOps;
import com.hywx.authservice.service.impl.ValidateCodeServiceImpl;
import com.hywx.authservice.vo.UserTokenVo;
import com.hywx.authservice.vo.UserVo;
import com.hywx.common.core.util.Exception.CommonException;
import com.hywx.common.core.util.results.Resp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-04 13:19
 **/
@Slf4j
@RestController
public class SecurityController {

    @Autowired
    private ValidateCodeServiceImpl validateCodeService;

    @Autowired
    private OAuthClientDetailsService clientService;

    @Autowired
    private RedisTemplateOps redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisClientDetailsServiceImpl redisClientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Resource
    private ObjectMapper objectMapper;

    @GetMapping("/user")
    public Principal currentUser(Principal principal) {
        return principal;
    }


    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, CommonException {
        validateCodeService.create(request, response);
    }

    @RequestMapping(value = "/user/authorize", method = RequestMethod.POST)
    @ApiOperation(value = "获取认证获取code")
    public Resp authorize(@RequestBody UserVo userVo) throws Exception{

          // 校验验证码
        validateCodeService.check(userVo.getKey(), userVo.getCode());

        OAuthClientDetails client = clientService.findById(userVo.getClientId());


        if(client==null){
            return Resp.getInstantiationError("客户端client_id不存在", null, null);
        }
        User user = userMapper.findUserByUname(userVo.getUsername());
        if(user==null){
            return Resp.getInstantiationError("用户名不存在", null, null);
        }
        if(!new BCryptPasswordEncoder().matches(userVo.getPassword(), user.getPassword())){
            return Resp.getInstantiationError("用户密码不正确", null, null);
        }
        // 判断回调地址
        if(!client.getWebServerRedirectUri().equals(userVo.getRedirectUri())){
            return Resp.getInstantiationError("回调地址redirect_uri不正确", null, null);
        }
        // 生成code
        String code = UUID.randomUUID().toString().replace("-", "");
        // 存入用户及clientId信息300s
        redisService.set("oauthCode:"+code, new Gson().toJson(new Oauth2TokenInfo(userVo.getClientId(),userVo.getClientSecret(), userVo.getUsername(),userVo.getPassword())));
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", code);
        map.put("redirect_uri", userVo.getRedirectUri());
        map.put("isLogin", false);
        return Resp.getInstantiationSuccess("获取认证获取code", Resp.LIST, map);
    }

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;


    @RequestMapping(value = "/user/authorized", method = RequestMethod.GET)
    @ApiOperation(value = "已认证过获取新token")
    public Resp authorized(@ApiParam("用户名") @RequestParam String username,
                           @ApiParam("客户端id") @RequestParam String client_id,
                           @ApiParam("成功授权后回调地址") @RequestParam String redirect_uri,HttpServletRequest request){
        // 校验回调地址
        OAuthClientDetails client = clientService.findById(client_id);
        if(client==null){
            return Resp.getInstantiationError("客户端client_id不存在", null, null);
        }
        // 判断回调地址是否正确
        if(!client.getWebServerRedirectUri().equals(redirect_uri)){
            return Resp.getInstantiationError("回调地址redirect_uri不正确", null, null);
        }
        // 生成code 5分钟内有效
        String code = UUID.randomUUID().toString().replace("-", "");
        String authorization =request.getHeader(HttpHeaders.AUTHORIZATION);
        String token=authorization.split(" ")[1];
        // 判断回调地址是否正确
        if(authorization==null || authorization.split(" ").length<1){
            return Resp.getInstantiationError("Token为空", null, null);
        }
        // 判断原token是否失效
        OAuth2AccessToken tokenInfo =resourceServerTokenServices.readAccessToken(token);
        if (tokenInfo == null) {
            throw new InvalidTokenException("Token 错误");
        } else if (tokenInfo.isExpired()) {
            throw new InvalidTokenException("Token 过期");
        }
        // 存入用户及clientId信息
        redisService.set("oauthCode:"+code,token );
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", code);
        map.put("redirect_uri", redirect_uri);
        map.put("isLogin", true);
        return Resp.getInstantiationSuccess("操作成功", Resp.LIST, map);
    }



    @ApiOperation(value = "根据code获取token")
    @PostMapping("/user/token")
    public Resp getUserTokenInfo(@RequestBody UserTokenVo userVo,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userVo.getAuthorizeCode() == null || "".equals(userVo.getAuthorizeCode())) {
            throw new UnapprovedClientAuthenticationException("授权码为空");
        }

        // 判断code 获取用户信息
        String codeValue = redisService.get("oauthCode:"+userVo.getAuthorizeCode()).toString();
        // 获取token移除授权码
        redisService.setRemove("oauthCode:"+userVo.getAuthorizeCode(),codeValue);
        if(StrUtil.isBlank(codeValue)){
            throw new UnapprovedClientAuthenticationException("授权码已过期");
        }
       // 第一次登录
        if(!userVo.isLogin()) {
            Oauth2TokenInfo tokenInfo = new Gson().fromJson(codeValue, Oauth2TokenInfo.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tokenInfo.getUsername(), tokenInfo.getPassword());
           return writerToken(request, response, token);
        }else
        {
            // 第二次登录直接给
            return Resp.getInstantiationSuccess("获取token成功", Resp.LIST, codeValue);
        }
    }

    /**
     * 生成token
     * @param request
     * @param response
     * @param token
     * @throws IOException
     */
    private Resp writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token) throws IOException {
        try {
            String authorization =request.getHeader(HttpHeaders.AUTHORIZATION);
           //  authorization="Basic aHl3eDoxMjM0NTY=";
            String client=new String(Base64Utils.decodeFromString(authorization.split(" ")[1]),"UTF-8");
            if(client == null || "".equals(client)){
                throw new UnapprovedClientAuthenticationException("请求头中无client_id信息");
            }
            String clientId =client.split(":")[0];
            String clientSecret = client.split(":")[1];
            if (clientId == null || "".equals(clientId)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client_id信息");
            }

            if (clientSecret == null || "".equals(clientSecret)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client_secret信息");
            }

            ClientDetails clientDetails = getClient(clientId, clientSecret);
            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "password");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            oAuth2Authentication.setAuthenticated(true);;
           return   Resp.getInstantiationSuccess("获取token成功", Resp.LIST, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
           // exceptionHandler(response, badCredenbtialsMsg);
            e.printStackTrace();
            return Resp.getInstantiationError("获取token失败" + e.getMessage(), Resp.LIST, null);
        } catch (Exception e) {
            //exceptionHandler(response, e);
            return Resp.getInstantiationError("获取token失败" + e.getMessage(), Resp.LIST, null);
        }
    }



    /**
     * 获取clientId
     * @param clientId
     * @param clientSecret
     * @return
     */
    private ClientDetails getClient(String clientId, String clientSecret) {
        ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }

    private void writerObj(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (
                Writer writer = response.getWriter()
        ) {
            writer.write(objectMapper.writeValueAsString(obj));
            writer.flush();
        }
    }

    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writerObj(response, Resp.getInstantiationError(msg, Resp.LIST, null));
    }
}

