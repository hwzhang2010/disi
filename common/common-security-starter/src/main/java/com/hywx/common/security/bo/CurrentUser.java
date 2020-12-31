package com.hywx.common.security.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-16 10:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Serializable {

    private static long serialVersionUID = 761748087824726463L;

    @JsonIgnore
    private String password;
    private String username;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String id;
    private String email;
    private String mobile;
    private String sex;
    private String orgId;
    private String organization;
    private String roleId;
    private String roleName;
    private String avatar;
    @JsonIgnore
    private Date lastLoginTime;
    private String status;
}
