package com.jcloud.security.config.component.resourceserver;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.common.config.SystemProperty;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.CookieUtil;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.consts.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseData commonRespon = new ResponseData();
        commonRespon.setCode(Const.CODE_NO_LOGIN);
        if (authException.getCause() instanceof InvalidTokenException) {
            // 如果携带无效cookie 并且不是ajax请求时，应该设置cookie为空，重定向到该页面
            if (!WebUtil.isAjaxRequest(request) && StringUtils.isNotBlank(CookieUtil.getCookieValue(request, SecurityConstants.TOKEN_COOKIE_NAME))) {
                CookieUtil.setCookie(SpringUtil.getBean(SystemProperty.class).getDomain(), response, SecurityConstants.TOKEN_COOKIE_NAME, null);
                RemoveCookieHttpServletRequestWrapper wrapper = new RemoveCookieHttpServletRequestWrapper(request, Arrays.asList(SecurityConstants.TOKEN_COOKIE_NAME));
                request.getRequestDispatcher(request.getRequestURI()).forward(wrapper, response);
                return;
            }
        }
        // 输出json异常信息
        printJson(response, commonRespon, authException.getMessage());
    }


    private void printJson(HttpServletResponse response, ResponseData commonRespon, String msg) throws IOException {
        WebUtil.jsonResponseMIME(response);
        commonRespon.setMsg(msg);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JsonUtils.toJsonString(commonRespon));
    }

}
