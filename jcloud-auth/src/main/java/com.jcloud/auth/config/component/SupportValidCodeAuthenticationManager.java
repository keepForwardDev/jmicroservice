package com.jcloud.auth.config.component;

import com.jcloud.common.config.SystemProperty;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.exception.InvalidCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 支持验证码验证登录
 * @author jiaxm
 * @date 2021/8/4
 */
public class SupportValidCodeAuthenticationManager implements AuthenticationManager {

    private AuthenticationManager delegate;

    private StringRedisTemplate stringRedisTemplate;

    private SystemProperty systemProperty;

    public SupportValidCodeAuthenticationManager(AuthenticationManager delegate, StringRedisTemplate stringRedisTemplate, SystemProperty systemProperty) {
        this.delegate = delegate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.systemProperty = systemProperty;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!systemProperty.getOpenLoginCode()) {
            return delegate.authenticate(authentication);
        }
        String code = WebUtil.getRequest().getParameter("code");
        String requestSn = WebUtil.getRequest().getParameter("requestSn");
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String storeCode = valueOperations.get(StringUtils.defaultString(requestSn));
        if (StringUtils.isBlank(code) || StringUtils.isBlank(storeCode) || !code.equals(storeCode)) {
            throw new InvalidCodeException();
        } else {
            stringRedisTemplate.delete(requestSn);
        }

        return delegate.authenticate(authentication);
    }
}
