package com.jcloud.security.interceptor;

import com.jcloud.common.consts.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 忽略 api 和 内部调用的权限校验
 * api权限校验只有赋值给用户才能有权限访问
 * @author jiaxm
 * @date 2021/11/2
 */
@Component
public class HeaderRequestMatcherIntercepter implements RequestMatcherIntercepter {

    @Value("${spring.application.name}")
    private String application;

    @Override
    public boolean intercept(HttpServletRequest request) {
        String openApiHeader = request.getHeader(Const.OPEN_API_HEADER);
        String apiInnerCall = request.getHeader(Const.API_INNER_CALL_HEADER);
        if (StringUtils.equalsAny(application, openApiHeader, apiInnerCall)) {
            return false;
        }
        return true;
    }
}
