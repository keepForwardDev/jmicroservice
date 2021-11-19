package com.jcloud.security.exception;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

/**
 * 账号登出异常
 * @author jiaxm
 * @date 2021/3/29
 */
public class KickedOutException extends InvalidTokenException {
    public KickedOutException(String msg) {
        super(msg);
    }
}
