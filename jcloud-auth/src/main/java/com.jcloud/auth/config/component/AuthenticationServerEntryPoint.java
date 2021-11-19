package com.jcloud.auth.config.component;

import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.consts.SecurityConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 授权服务器登录错误处理类，basic验证失败
 * @author jiaxm
 * @date 2021/8/5
 */
public class AuthenticationServerEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        WebUtil.jsonResponseMIME(response);
        ResponseData commonRespon = new ResponseData();
        commonRespon.setCode(Const.CODE_ERROR);
        commonRespon.setMsg(SecurityConstants.BAD_CLIENT_CREDENTIALS);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JsonUtils.toJsonString(commonRespon));
    }
}
