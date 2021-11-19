package com.jcloud.security.config.component.localsecurity;

import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.consts.SecurityConstants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功，但是无权访问处理
 * @author jiaxm
 * @date 2021/3/30
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        WebUtil.jsonResponseMIME(response);
        ResponseData commonRespon = new ResponseData();
        commonRespon.setMsg(SecurityConstants.ACCESS_DENY_MSG + ":" + request.getRequestURI());
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JsonUtils.toJsonString(commonRespon));
    }
}
