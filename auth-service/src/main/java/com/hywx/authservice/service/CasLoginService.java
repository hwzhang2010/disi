package com.hywx.authservice.service;

import com.hywx.authservice.dao.CasUser;
import com.hywx.common.core.util.Exception.CommonException;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-22 14:56
 **/
public interface CasLoginService {

    OAuth2AccessToken casLogin(CasUser casUser, String clientId, String clientSercert) throws CommonException;
}
