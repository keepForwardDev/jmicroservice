package com.jcloud.security.config.component.resourceserver;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.common.config.SystemProperty;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.CookieUtil;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.consts.SecurityConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        WebUtil.jsonResponseMIME(response);
        ResponseData commonRespon = new ResponseData();
        commonRespon.setCode(Const.CODE_NO_LOGIN);
        if (authException.getCause() instanceof InvalidTokenException) {
            CookieUtil.setCookie(SpringUtil.getBean(SystemProperty.class).getDomain(), response, SecurityConstants.TOKEN_COOKIE_NAME, null);
            commonRespon.setMsg(authException.getMessage());
        } else {
            commonRespon.setMsg(authException.getMessage());
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.append(JsonUtils.toJsonString(commonRespon));
    }
}
