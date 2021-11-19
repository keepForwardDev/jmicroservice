package com.jcloud.security.interceptor;

import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.consts.Const;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.WebUtil;
import com.jcloud.remote.admin.ApiPrivilegesRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * api 权限拦截
 *
 * @author jiaxm
 * @date 2021/9/17
 */
@Slf4j
public class OpenApiInterceptor implements HandlerInterceptor {

    private String serviceId;

    private ApiPrivilegesRemoteService apiPrivilegesRemoteService;

    public OpenApiInterceptor(String serviceId, ApiPrivilegesRemoteService apiPrivilegesRemoteService) {
        this.serviceId = serviceId;
        this.apiPrivilegesRemoteService = apiPrivilegesRemoteService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String openApiHeader = request.getHeader(Const.OPEN_API_HEADER);
        if (StringUtils.isNotBlank(openApiHeader) && openApiHeader.equals(serviceId)) {
            try {
                // api权限判断
                ApiResult apiResult = apiPrivilegesRemoteService.getOpenApiPrivileges(exactHeaders(request));
                request.setAttribute(Const.OPEN_API_RESULT, apiResult);
                if (apiResult.getCode().intValue() == ApiResult.API_RESULT_SUCCESS.intValue()) {
                    return true;
                } else {
                    WebUtil.jsonResponseMIME(response);
                    PrintWriter printWriter = response.getWriter();
                    printWriter.append(JsonUtils.toJsonString(apiResult));
                    return false;
                }
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                ApiResult apiResult = new ApiResult();
                apiResult.setCode(ApiResult.API_ERROR);
                apiResult.setMsg(ApiResult.codeMap.get(ApiResult.API_ERROR));
                WebUtil.jsonResponseMIME(response);
                PrintWriter printWriter = response.getWriter();
                printWriter.append(JsonUtils.toJsonString(apiResult));
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private ApiRequest exactHeaders(HttpServletRequest request) {
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setToken(request.getHeader("Token"));
        apiRequest.setAccessToken(request.getHeader("AccessToken"));
        apiRequest.setAppKey(request.getHeader("AppKey"));
        apiRequest.setNonce(request.getHeader("Nonce"));
        apiRequest.setTimeStamp(request.getHeader("TimeStamp"));
        apiRequest.setApiPath(request.getRequestURI());
        apiRequest.setServiceId(serviceId);
        return apiRequest;
    }

}
