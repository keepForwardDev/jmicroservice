package com.jcloud.auth.config.component;

import com.jcloud.common.consts.Const;
import com.jcloud.security.consts.SecurityConstants;
import com.jcloud.security.exception.InvalidCodeException;
import com.jcloud.security.exception.Oauth2AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        if (e instanceof InvalidGrantException) { // 密码错误
            return handleInvalidGrantException(e);
        } else if (e instanceof InvalidTokenException) { // token 错误
            return new ResponseEntity(new Oauth2AuthenticationException(e.getMessage().equals(SecurityConstants.KICKED_OUT_MSG) ? e.getMessage() : SecurityConstants.INVALID_TOKEN_MSG, Const.CODE_NO_LOGIN, null), HttpStatus.OK);
        } else if (e instanceof UnsupportedGrantTypeException) { // 不支持的授权类型
            return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.NOT_SUPPORTED_GRANT_TYPE, Const.CODE_ERROR, null), HttpStatus.OK);
        } else if (e instanceof InvalidScopeException || e instanceof UnsupportedResponseTypeException || e instanceof RedirectMismatchException || e instanceof InvalidCodeException) {
            return new ResponseEntity(new Oauth2AuthenticationException(e.getMessage(), Const.CODE_ERROR, null), HttpStatus.OK);
        }
        Throwable throwable = e.getCause();
        if (throwable instanceof BadCredentialsException) { // 账号错误
            return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.BAD_CREDENTIALS, Const.CODE_ERROR, null), HttpStatus.OK);
        }
        return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.INVALID_TOKEN_MSG, Const.CODE_ERROR, null), HttpStatus.OK);
    }

    private ResponseEntity handleInvalidGrantException(Exception e) {
        if (e.getMessage().contains("refresh token")) {
            return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.INVALID_REFRESH_TOKEN, Const.CODE_ERROR, null), HttpStatus.OK);
        } else if (e.getMessage().contains("Invalid authorization")) {
            return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.INVALID_CODE_MSG, Const.CODE_ERROR, null), HttpStatus.OK);
        }
        return new ResponseEntity(new Oauth2AuthenticationException(SecurityConstants.BAD_CREDENTIALS, Const.CODE_ERROR, null), HttpStatus.OK);
    }
}
