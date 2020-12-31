package com.hywx.authservice.service.impl;

import com.hywx.authservice.dao.CasUser;
import com.hywx.authservice.dao.User;
import com.hywx.authservice.mapper.UserMapper;
import com.hywx.authservice.provider.CasAuthenticationToken;
import com.hywx.authservice.service.CasLoginService;
import com.hywx.authservice.util.CommonConstant;
import com.hywx.common.core.util.Exception.CommonException;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-22 14:56
 **/
@Service
@RequiredArgsConstructor
public class CasLoginServiceImpl implements CasLoginService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private UserMapper userManager;
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisClientDetailsServiceImpl redisClientDetailsService;
    @Autowired
    private final ResourceOwnerPasswordTokenGranter granter;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public OAuth2AccessToken casLogin(CasUser casUser, String clientId, String clientSercert) throws CommonException {
        User user = this.userManager.findUserByUname(casUser.getUsername());
        if (user != null) {
            return this.getOAuth2AccessToken(user,clientId,clientSercert);
        }else {
            String encryptPassword = passwordEncoder.encode("12345678");
            User systemUser = new User();
            systemUser.setUsername(casUser.getUsername());
            systemUser.setPassword(casUser.getPassword());
            systemUser.setMobile(casUser.getMobile());
            systemUser.setCreateTime(new Date());
            systemUser.setStatus(1);
            systemUser.setRemark("单点登录系统用户");
            this.userManager.insert(systemUser);
            return this.getOAuth2AccessToken(systemUser,clientId,clientSercert);
        }
    }

//    private OAuth2AccessToken getOAuth2AccessToken(User user,String clientId,String clientSercert) throws CommonException {
//         ClientDetails clientDetails = null;
//       try {
//           clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
//       } catch (Exception e) {
//            throw new CommonException("获取可用的Client失败");
//       }
//        if (clientDetails == null) {
//            throw new CommonException("未找到可用的Client");
//        }
//        Map<String, String> requestParameters = new HashMap<>(5);
//        requestParameters.put(CommonConstant.GRANT_TYPE, CommonConstant.PASSWORD);
//        requestParameters.put(USERNAME, user.getUsername());
//        requestParameters.put(PASSWORD, user.getPassword());
//
//        String grantTypes = String.join(",", clientDetails.getAuthorizedGrantTypes());
//        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(),grantTypes);
//        return granter.grant(CommonConstant.PASSWORD, tokenRequest);
//    }

    private OAuth2AccessToken getOAuth2AccessToken(User user,String clientId,String clientSercert) throws CommonException {
        ClientDetails clientDetails = null;
        try {
            clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
        } catch (Exception e) {
            throw new CommonException("获取可用的Client失败");
        }
        if (clientDetails == null) {
            throw new CommonException("未找到可用的Client");
        }
        CasAuthenticationToken  token = new CasAuthenticationToken(user.getUsername());
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "customer");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        return oAuth2AccessToken;
       // return granter.grant(CommonConstant.PASSWORD, tokenRequest);
    }
}
