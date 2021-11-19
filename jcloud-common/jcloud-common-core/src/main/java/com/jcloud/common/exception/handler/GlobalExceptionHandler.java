package com.jcloud.common.exception.handler;

import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.TypeUtil;
import com.jcloud.common.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author jiaxm
 * @date 2021/3/30
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice
@Order
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData globalExceptionResponse(HttpServletRequest request, Exception e, HttpServletResponse response) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramKey = TypeUtil.toStr(enumeration.nextElement());
            String value = request.getParameter(paramKey);
            if (StringUtils.isBlank(value)) {
                LinkedHashMap jsonMap = JsonUtils.readObject(paramKey, LinkedHashMap.class);
                paramMap.put("传递的JSON数据", jsonMap);
            } else {
                paramMap.put(paramKey, value);
            }
        }
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerKey = TypeUtil.toStr(headers.nextElement());
            String value = request.getHeader(headerKey);
            headerMap.put(headerKey, value);
        }
        log.error("请求错误：{} {} header:{} params:{}", request.getRequestURI(), request.getMethod(), headerMap.toString(), paramMap.toString());
        log.error(ExceptionUtils.getStackTrace(e));
        WebUtil.jsonResponseMIME(response);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ResponseData commonRespon = new ResponseData();
        commonRespon.setMsg(String.format("%s [%s]", Const.INTERNAL_ERROR, e.getMessage()));
        return commonRespon;
    }
}
