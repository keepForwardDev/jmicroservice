package com.jcloud.security.exception.handler;

import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.security.consts.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jiaxm
 * @date 2021/4/21
 */
@ControllerAdvice
@Order(value = 10)
@Slf4j
public class AccessDenyHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    public void globalExceptionResponse(Exception e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        WebUtil.jsonResponseMIME(response);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ResponseData commonRespon = new ResponseData();
        commonRespon.setMsg(SecurityConstants.ACCESS_DENY_MSG);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JsonUtils.toJsonString(commonRespon));
    }
}
