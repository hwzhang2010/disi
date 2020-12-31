package com.hywx.authservice.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-16 10:14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthServerUser extends User {
    private static final long serialVersionUID = -1748289340320186418L;
    private String id;
    private String mobile;
    private int status;
    private String orgId;
    private String organization;
    private String roleId;
    private String roleName;
    private Integer sex;
    private Date lastLoginTime;
    private String avatar;
    private String email;

    public AuthServerUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthServerUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}