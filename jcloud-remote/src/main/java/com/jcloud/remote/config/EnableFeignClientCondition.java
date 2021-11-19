package com.jcloud.remote.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 是否有开启feign client
 * @author jiaxm
 * @date 2021/11/2
 */
public class EnableFeignClientCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> map = context.getBeanFactory().getBeansWithAnnotation(EnableFeignClients.class);
        return !map.isEmpty();
    }
}
