package com.jcloud.gateway.config;

import lombok.SneakyThrows;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

/**
 * spring loadbalancer do not support netty
 * ribbon dependence is from nacos and do not support this spring-cloud version
 *
 * @author jiaxm
 * @date 2021/9/15
 */
@Component
public class HighLevelRibbonLoadBalancerClient extends RibbonLoadBalancerClient {

    public HighLevelRibbonLoadBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
    }

    @SneakyThrows
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        return super.choose(serviceId);
    }
}
