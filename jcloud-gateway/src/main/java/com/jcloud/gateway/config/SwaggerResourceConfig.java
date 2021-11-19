package com.jcloud.gateway.config;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 其他服务无需引入ui
 * 只提供api
 * gatewayhost/doc.html
 */
@Slf4j
@Primary
@Configuration
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Value("${springfox.documentation.swagger.v2.path:/v2/api-docs}")
    private String apiPath;

    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    @Autowired
    private NacosServiceDiscovery nacosServiceDiscovery;

    @Autowired
    private Environment environment;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = getResource(false);
        return resources;
    }

    public List<SwaggerResource> getResource(boolean useName) {
        List<SwaggerResource> resources = new ArrayList<>();
        routeDefinitionRepository.getRouteDefinitions().subscribe(route -> {
            route.getPredicates().stream()
                    .filter(predicateDefinition -> {
                        return ("Path").equalsIgnoreCase(predicateDefinition.getName());
                    }).collect(Collectors.toList())
                    .forEach(predicateDefinition -> {
                        Object showSwagger = route.getMetadata().get("showSwagger");
                        Object serviceName = route.getMetadata().get("name");
                        String serviceId = route.getUri().toString().replace("lb://", StringUtils.EMPTY);
                        if (showSwagger != null && Boolean.TRUE.equals(showSwagger)) {
                            String name = useName ? (serviceName == null ? serviceId : serviceName.toString()) : serviceId;
                            String path = predicateDefinition.getArgs().get("pattern").replace("/**", apiPath);
                            String location = useName ? route.getId() : path;
                            resources.add(swaggerResource(name, location));
                        }
                    });
        });
        swapOnlineResource(resources);
        return resources;
    }


    @SneakyThrows
    private void swapOnlineResource(List<SwaggerResource> resources) {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        if (profiles.contains("dev")) {
            InetUtilsProperties target = new InetUtilsProperties();
            InetUtils utils = new InetUtils(target);
            InetUtils.HostInfo hostInfo = utils.findFirstNonLoopbackHostInfo();
            String ipAddress = hostInfo.getIpAddress();
            // 选择目前在线的服务作为第一个，否则swagger首页将会加载不出来
            for (int i = 0; i < resources.size(); i++) {
                ServiceInstance serviceInstances = nacosServiceDiscovery.getInstances(resources.get(i).getName()).stream().filter(r -> r.getHost().equals(ipAddress)).findFirst().orElse(null);
                if (serviceInstances != null) {
                    Collections.swap(resources, 0, i);
                    break;
                }
            }
        } else {
            // 选择目前在线的服务作为第一个，否则swagger首页将会加载不出来
            for (int i = 0; i < resources.size(); i++) {
                ServiceInstance serviceInstances = nacosServiceDiscovery.getInstances(resources.get(i).getName()).stream().findFirst().orElse(null);
                if (serviceInstances != null) {
                    Collections.swap(resources, 0, i);
                    break;
                }
            }
        }
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
