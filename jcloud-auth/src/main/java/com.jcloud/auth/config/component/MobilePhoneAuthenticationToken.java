package com.jcloud.auth.config.component;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 手机号短信登陆凭证
 * @author jiaxm
 * @date 2021/3/31
 */
public class MobilePhoneAuthenticationToken extends UsernamePasswordAuthenticationToken {


    public MobilePhoneAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MobilePhoneAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
