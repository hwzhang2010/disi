package com.hywx.authservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hywx.authservice.bo.BindUser;
import com.hywx.authservice.dao.User;
import com.hywx.authservice.dao.UserConnection;
import com.hywx.authservice.mapper.UserMapper;
import com.hywx.authservice.properties.AuthProperties;
import com.hywx.authservice.service.SocialLoginService;
import com.hywx.authservice.service.UserConnectionService;
import com.hywx.authservice.util.CommonConstant;
import com.hywx.common.core.util.CommonUtil;
import com.hywx.common.core.util.Exception.CommonException;
import com.hywx.common.core.util.Exception.CommonResponse;
import com.xkcoding.justauth.AuthRequestFactory;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-06 11:41
 **/
@Service
public class SocialLoginServiceImpl implements SocialLoginService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String NOT_BIND = "not_bind";
    private static final String SOCIAL_LOGIN_SUCCESS = "social_login_success";

    @Autowired
    private UserMapper userManager;
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private AuthProperties properties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserConnectionService userConnectionService;
    @Autowired
    private ResourceOwnerPasswordTokenGranter granter;
    @Autowired
    private RedisClientDetailsServiceImpl redisClientDetailsService;

    @Override
    public AuthRequest renderAuth(String oauthType) throws CommonException {
        return factory.get(getAuthSource(oauthType));
    }

    @Override
    public CommonResponse resolveBind(String oauthType, AuthCallback callback) throws CommonException {
        CommonResponse febsResponse = new CommonResponse();
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));
        if (response.ok()) {
            febsResponse.data(response.getData());
        } else {
            throw new CommonException(String.format("第三方登录失败，%s", response.getMsg()));
        }
        return febsResponse;
    }

    @Override
    public CommonResponse resolveLogin(String oauthType, AuthCallback callback) throws CommonException {
        CommonResponse febsResponse = new CommonResponse();
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));
        if (response.ok()) {
            AuthUser authUser = (AuthUser) response.getData();
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection == null) {
                febsResponse.message(NOT_BIND).data(authUser);
            } else {
                User user = userManager.findUserByUname(userConnection.getUserName());
                if (user == null) {
                    throw new CommonException("系统中未找到与第三方账号对应的账户");
                }
                OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(user);
                febsResponse.message(SOCIAL_LOGIN_SUCCESS).data(oAuth2AccessToken);
                febsResponse.put(USERNAME, user.getUsername());
            }
        } else {
            throw new CommonException(String.format("第三方登录失败，%s", response.getMsg()));
        }
        return febsResponse;
    }

    @Override
    public OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws CommonException {
        User systemUser = userManager.findUserByUname(bindUser.getBindUsername());
        if (systemUser == null || !passwordEncoder.matches(bindUser.getBindPassword(), systemUser.getPassword())) {
            throw new CommonException("绑定系统账号失败，用户名或密码错误！");
        }
        this.createConnection(systemUser, authUser);
        return this.getOAuth2AccessToken(systemUser);
    }

    @Override
    public OAuth2AccessToken signLogin(BindUser registUser, AuthUser authUser) throws CommonException {
        User user = this.userManager.findUserByUname(registUser.getBindUsername());
        if (user != null) {
            throw new CommonException("该用户名已存在！");
        }
        String encryptPassword = passwordEncoder.encode(registUser.getBindPassword());
        User systemUser = new User();
        systemUser.setUsername(registUser.getBindUsername());
        systemUser.setPassword(encryptPassword);
        systemUser.setCreateTime(new Date());
        systemUser.setStatus(1);
        systemUser.setRemark("注册用户");
        this.userManager.insert(systemUser);
        this.createConnection(systemUser, authUser);
        return this.getOAuth2AccessToken(systemUser);
    }

    @Override
    public void bind(BindUser bindUser, AuthUser authUser) throws CommonException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection != null) {
                throw new CommonException("绑定失败，该第三方账号已绑定" + userConnection.getUserName() + "系统账户");
            }
            User systemUser = new User();
            systemUser.setUsername(username);
            this.createConnection(systemUser, authUser);
        } else {
            throw new CommonException("绑定失败，您无权绑定别人的账号");
        }
    }

    @Override
    public void unbind(BindUser bindUser, String oauthType) throws CommonException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            this.userConnectionService.deleteByCondition(username, oauthType);
        } else {
            throw new CommonException("解绑失败，您无权解绑别人的账号");
        }
    }

    @Override
    public List<UserConnection> findUserConnections(String username) {
        return this.userConnectionService.selectByCondition(username);
    }

    private void createConnection(User systemUser, AuthUser authUser) {
        UserConnection userConnection = new UserConnection();
        userConnection.setUserName(systemUser.getUsername());
        userConnection.setProviderName(authUser.getSource().toString());
        userConnection.setProviderUserId(authUser.getUuid());
        userConnection.setProviderUserName(authUser.getUsername());
        userConnection.setImageUrl(authUser.getAvatar());
        userConnection.setNickName(authUser.getNickname());
        userConnection.setLocation(authUser.getLocation());
        this.userConnectionService.createUserConnection(userConnection);
    }

    private AuthCallback resolveAuthCallback(AuthCallback callback) {
        String state = callback.getState();
        String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(state, "::");
        if (strings.length == 3) {
            callback.setState(strings[0] + "::" + strings[1]);
        }
        return callback;
    }

    private AuthSource getAuthSource(String type) throws CommonException {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new CommonException(String.format("暂不支持%s第三方登录", type));
        }
    }

    private boolean isCurrentUser(String username) {
        // String currentUsername = CommonUtil.getCurrentUsername();
       // return StringUtils.equalsIgnoreCase(username, currentUsername);
        return false;
    }

    private OAuth2AccessToken getOAuth2AccessToken(User user) throws CommonException {
        final HttpServletRequest httpServletRequest = CommonUtil.getHttpServletRequest();
        httpServletRequest.setAttribute(CommonConstant.LOGIN_TYPE, CommonConstant.SOCIAL_LOGIN);
        String socialLoginClientId = properties.getSocialLoginClientId();
        ClientDetails clientDetails = null;
        try {
            clientDetails = redisClientDetailsService.loadClientByClientId(socialLoginClientId);
        } catch (Exception e) {
            throw new CommonException("获取第三方登录可用的Client失败");
        }
        if (clientDetails == null) {
            throw new CommonException("未找到第三方登录可用的Client");
        }
        Map<String, String> requestParameters = new HashMap<>(5);
        requestParameters.put(CommonConstant.GRANT_TYPE, CommonConstant.PASSWORD);
        requestParameters.put(USERNAME, user.getUsername());
        requestParameters.put(PASSWORD, CommonConstant.SOCIAL_LOGIN_PASSWORD);

        String grantTypes = String.join(",", clientDetails.getAuthorizedGrantTypes());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(), grantTypes);
        return granter.grant(CommonConstant.PASSWORD, tokenRequest);
    }
}