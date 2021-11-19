package com.jcloud.auth.config.component;

import com.jcloud.common.consts.Const;
import com.jcloud.security.bean.ShiroUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

/**
 * 为了输出code = 1
 * @author jiaxm
 * @date 2021/8/5
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CustomOAuth2AccessTokenJackson2Serializer.class)
public class CustomUserOAuth2AccessToken extends DefaultOAuth2AccessToken {

    private Integer code = Const.CODE_SUCCESS;

    /**
     * 登录成功才会返回用户信息，在序列化输出的时候调用
     */
    private ShiroUser info = null;

    public CustomUserOAuth2AccessToken(ShiroUser info, String value) {
        super(value);
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ShiroUser getInfo() {
        return info;
    }
}
