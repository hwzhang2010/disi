package com.hywx.common.security.configure;

import com.hywx.common.security.utils.Md5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-23 16:08
 **/
public class Md5PasswordEncoder implements PasswordEncoder{

    public Md5PasswordEncoder() {
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return  encodedPassword.equals(Md5Util.getMd5Pwd("sbdp",rawPassword.toString(),2));
    }

    @Override
    public String encode(CharSequence pwd) {
        return  Md5Util.getMd5Pwd("sbdp",pwd.toString(),2);
    }
}