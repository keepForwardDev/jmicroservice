package com.jcloud.security.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.WebUtil;
import com.jcloud.remote.admin.LogRemoteService;
import com.jcloud.remote.config.EnableFeignClientCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于拦截api 响应，只有在api 调用成功才会扣减次数
 *
 * @author jiaxm
 * @date 2021/9/18
 */
@ConditionalOnProperty(name = "system.openApi", havingValue = "true")
@Conditional(value = EnableFeignClientCondition.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice
public class ApiResultResponseAdvice implements ResponseBodyAdvice {

    @Value(value = "${spring.application.name}")
    private String serviceId;

    @Autowired
    private LogRemoteService logRemoteService;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        HttpServletRequest request = WebUtil.getRequest();
        String openApiHeader = request.getHeader(Const.OPEN_API_HEADER);
        return StringUtils.isNotBlank(openApiHeader) && openApiHeader.equals(serviceId);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest request1 = WebUtil.getRequest();
        ApiResult apiResult = (ApiResult) request1.getAttribute(Const.OPEN_API_RESULT);
        request1.removeAttribute(Const.OPEN_API_RESULT);
        if (body instanceof ResponseData) {
            // 执行成功
            ResponseData responseData = (ResponseData) body;
            if (responseData.getCode() == 1) {
                // 记录日志
                ResponseData apiLogRes = saveApiLog(request1, apiResult.getOrderNumber());
                // 日志处理成功
                if (apiLogRes.getCode() == 1) {
                    apiResult.setData(responseData.getData());
                    apiResult.setReserveData(responseData.getReserveData());
                    apiResult.setOrderNumber(apiLogRes.getData().toString());
                } else {
                    apiResult.setCode(ApiResult.API_OUT_OF_BOUNDS);
                    apiResult.setMsg(ApiResult.codeMap.get(ApiResult.API_OUT_OF_BOUNDS));
                }
            } else {
                apiResult.setOrderNumber(null);
                apiResult.setCode(ApiResult.API_ERROR);
                apiResult.setMsg(responseData.getMsg());
            }
            return apiResult;
        }
        apiResult.setData(body);
        return apiResult;
    }

    private ResponseData saveApiLog(HttpServletRequest request, String apiInfo) {
        // apiLimit:2:data-center:jcloud-admin:/user/checkPhone/{phone}
        String[] apiInfoArray = apiInfo.split(":");
        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);
        params.put("title", "api 日志");
        params.put("content", apiInfo);
        params.put("remoteAddr", ServletUtil.getClientIP(request, null));
        params.put("userAgent", request.getHeader("user-agent"));
        params.put("requestUri", request.getRequestURI());
        params.put("clientId", apiInfoArray[2]);
        params.put("apiPath", apiInfoArray[4]);
        params.put("queryString", StringUtils.defaultIfBlank(request.getQueryString(), null));
        ResponseData logMap = logRemoteService.saveApiOrder(params);
        return logMap;
    }

}
