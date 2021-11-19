package com.jcloud.security.exception;

import com.jcloud.security.consts.SecurityConstants;
import org.springframework.security.core.AuthenticationException;

/**
 * 手机验证码不正确
 * @author jiaxm
 * @date 2021/3/31
 */
public class InvalidCodeException extends AuthenticationException {

    public InvalidCodeException() {
        super(SecurityConstants.INVALID_MOBILE_CODE_MSG);
    }
}
