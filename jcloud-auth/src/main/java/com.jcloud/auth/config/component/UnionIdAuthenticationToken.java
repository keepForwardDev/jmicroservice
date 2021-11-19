package com.jcloud.auth.config.component;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * 微信unionId 认证凭证
 * principal 为微信用户信息
 * @author jiaxm
 * @date 2021/3/31
 */
public class UnionIdAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public UnionIdAuthenticationToken(Map<String, Object> principal, Object credentials) {
        super(principal, credentials);
    }

    public UnionIdAuthenticationToken(Map<String, Object> principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }



}
