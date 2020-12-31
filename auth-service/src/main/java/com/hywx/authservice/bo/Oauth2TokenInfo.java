package com.hywx.authservice.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-04-21 15:21
 **/
@Data
@AllArgsConstructor
public class Oauth2TokenInfo implements Serializable {

    private String clientId;

    private String clientSecret;

    private String username;

    private String password;

}