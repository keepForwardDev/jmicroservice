package com.jcloud.security.config.component.resourceserver;

import com.jcloud.common.util.JsonUtils;
import com.jcloud.security.config.component.IgnoreUrlProperties;
import com.jcloud.security.interceptor.RequestMatcherIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 资源服务器 认证链的匹配规则
 * 只有匹配了这里，才会进入springSecurityChain
 *
 * @author jiaxm
 * @date 2021/3/30
 */
public class ResourceServerSecurityChainRequestMatcher implements RequestMatcher {

    @Autowired
    private IgnoreUrlProperties ignoreUrlProperties;

    @Autowired
    private List<RequestMatcherIntercepter> requestMatcherIntercepterList;


    @Override
    public boolean matches(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (RequestMatcherIntercepter requestMatcherIntercepter : requestMatcherIntercepterList) {
            if (!requestMatcherIntercepter.intercept(request)) {
                return false;
            }
        }
        return !PatternMatchUtils.simpleMatch(ignoreUrlProperties.getPublicUrls().toArray(new String[]{}), uri);
    }

    @Override
    public String toString() {
        return "ignore urls: " + JsonUtils.toJsonString(ignoreUrlProperties.getPublicUrls());
    }
}
