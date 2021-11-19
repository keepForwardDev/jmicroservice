package com.jcloud.security.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * 是否需要资源服务器进行鉴权拦截
 * @author jiaxm
 * @date 2021/11/2
 */
public interface RequestMatcherIntercepter {

    /**
     * 返回true需要鉴权， false 不需要鉴权
     * @param request
     * @return
     */
    boolean intercept(HttpServletRequest request);
}
