package com.jcloud.configure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 请求拦截器，保证非web环境下可以直接使用feign 但如果要鉴权需要把token 传入
 * @author laiguowei
 * @date 2021/8/25/025 17:05
 */
@Component
@Slf4j
public class FeignConfig implements RequestInterceptor {
    public FeignConfig() {
        System.out.println("重写请求拦截器，保证非web环境下可以直接使用feign");
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 5);
    }

    @Bean
    public RequestContextListener requestContextListenerBean() {
        return new RequestContextListener();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            RequestContextHolder.setRequestAttributes(requestAttributes, true);
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
            } catch (Exception e) {
                log.info("定时任务介入，授权异常");
            }

        } else {
            RequestContextHolder.setRequestAttributes(new NonWebRequestAttributes(), Boolean.TRUE);
            HttpServletRequest httpRequest = this.getHttpServletRequestSafely();
            if (null != httpRequest && null != httpRequest.getAttribute("X-Request-No")) {
                requestTemplate.header("X-Request-No", httpRequest.getAttribute("X-Request-No").toString());
            }
        }

    }

    public HttpServletRequest getHttpServletRequestSafely() {
        try {
            RequestAttributes requestAttributesSafely = this.getRequestAttributesSafely();
            return requestAttributesSafely instanceof NonWebRequestAttributes ? null : ((ServletRequestAttributes) requestAttributesSafely).getRequest();
        } catch (Exception var2) {
            return null;
        }
    }

    public RequestAttributes getRequestAttributesSafely() {
        RequestAttributes requestAttributes = null;
        try {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException var3) {
            requestAttributes = new NonWebRequestAttributes();
        }

        return requestAttributes;
    }
}
