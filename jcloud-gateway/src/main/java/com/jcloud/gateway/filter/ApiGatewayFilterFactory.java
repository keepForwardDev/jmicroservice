package com.jcloud.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcloud.remote.admin.ApiPrivilegesRemoteService;
import com.jcloud.remote.admin.LogRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/15
 */
@Component
public class ApiGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiGatewayFilterFactory.Config> {

    private static final String API = "api";

    @Autowired
    private ApiPrivilegesRemoteService apiPrivilegesRemoteService;

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogRemoteService logRemoteService;

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(API);
    }

    public ApiGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // return new ApiGatewayFilter(apiPrivilegesRemoteService, routeLocator, objectMapper, logRemoteService);
        return new ApiFilter(routeLocator);
    }

    public static class Config {

        private int api;

        public int getApi() {
            return api;
        }

        public void setApi(int api) {
            this.api = api;
        }
    }
}
