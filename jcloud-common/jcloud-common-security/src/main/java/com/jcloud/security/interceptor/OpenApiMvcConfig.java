package com.jcloud.security.interceptor;

import com.jcloud.remote.admin.ApiPrivilegesRemoteService;
import com.jcloud.remote.config.EnableFeignClientCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用于对api进行权限拦截，日志等操作
 * 服务需要开启open feign
 * @author jiaxm
 * @date 2021/9/7
 */
@ConditionalOnProperty(name = "system.openApi", havingValue = "true")
@Conditional(value = EnableFeignClientCondition.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@Slf4j
public class OpenApiMvcConfig implements WebMvcConfigurer {

    @Value(value = "${spring.application.name}")
    private String serviceId;

    @Lazy
    @Autowired(required = false)
    private ApiPrivilegesRemoteService apiPrivilegesRemoteService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new OpenApiInterceptor(serviceId, apiPrivilegesRemoteService));
        interceptorRegistration.addPathPatterns("/**");
    }
}
