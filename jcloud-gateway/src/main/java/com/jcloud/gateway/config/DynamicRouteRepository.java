package com.jcloud.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 动态路由配置
 * @author jiaxm
 * @date 2021/4/2
 */
@Configuration
public class DynamicRouteRepository implements RouteDefinitionRepository, InitializingBean {
    /**
     * nacos配置dataId
     */
    @Value("${nacos.gateway.route.dataId:gateway-route}")
    private String nacosConfigDataId;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * nacos配置 组id
     */
    @Value("${nacos.gateway.route.groupId:DEFAULT_GROUP}")
    private String nacosConfigGroupId;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private NacosConfigManager nacosConfigManager;


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String jsonContent = nacosConfigManager.getConfigService().getConfig(nacosConfigDataId, nacosConfigGroupId, 5000l);
            List<RouteDefinition> routeDefinitions = parseContent(jsonContent);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return Flux.fromIterable(new ArrayList<>());
    }

    private List<RouteDefinition> parseContent(String content) {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, RouteDefinition.class);
        try {
            routeDefinitions =  objectMapper.readValue(content, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return routeDefinitions;
    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addListener();
    }

    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(nacosConfigDataId, nacosConfigGroupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

}
