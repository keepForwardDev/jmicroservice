package com.jcloud.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jcloud.security.config.component.Oauth2ExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 对授权服务器的错误进行输出
 *
 * @author jiaxm
 * @date 2021/3/29
 */
@JsonSerialize(using = Oauth2ExceptionSerializer.class)
public class Oauth2AuthenticationException extends OAuth2Exception {

    private Integer code;

    private Object data;

    public Oauth2AuthenticationException(String msg, Integer code, Object data) {
        super(msg);
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
