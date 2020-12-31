package com.hywx.authservice.properties;

import lombok.Data;

/**
 * @program: gateway
 * @description:
 * @author: tangjing
 * @create: 2020-03-04 16:42
 **/
@Data
public class ClientsProperties {

    private String client;
    private String secret;
    private String grantType = "password,authorization_code,refresh_token";
    private String scope = "all";
}
