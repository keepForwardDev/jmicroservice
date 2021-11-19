package com.jcloud.security.config.component.localsecurity;

import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException || authException instanceof InternalAuthenticationServiceException) { // basic 验证失败
            WebUtil.jsonResponseMIME(response);
            ResponseData commonRespon = new ResponseData();
            commonRespon.setMsg(authException.getMessage());
            PrintWriter printWriter = response.getWriter();
            printWriter.append(JsonUtils.toJsonString(commonRespon));
            return;
        }
        if (WebUtil.isAjaxRequest(request)) {
            request.setAttribute("fromUrl", request.getRequestURI());
            request.getRequestDispatcher("/noLogin").forward(request, response);
            return;
        }
        super.commence(request, response, authException);
    }
}
