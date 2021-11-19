package com.jcloud.gateway.filter;

import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.consts.Const;
import com.jcloud.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 将api处理过程交给 服务，网关只做转发任务
 * @author jiaxm
 * @date 2021/9/17
 */
@Slf4j
public class ApiFilter implements GatewayFilter, Ordered {

    private RouteLocator routeLocator;

    public ApiFilter(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getRawPath().replace("/api", StringUtils.EMPTY);
        if (StringUtils.isNotBlank(path)) {
            exchange = exchange.mutate().request(exchange.getRequest().mutate().path(path).build()).build();
        }
        final ServerWebExchange webExchange = exchange;
        return lookupRoute(exchange).flatMap(r -> {
            webExchange.getAttributes().put(GATEWAY_ROUTE_ATTR, r);
            webExchange.getAttributes().put(Const.OPEN_API_HEADER, Const.OPEN_API_HEADER);
            return chain.filter(webExchange.mutate().request(webExchange.getRequest().mutate().header(Const.OPEN_API_HEADER, r.getId()).build()).build());
        }).switchIfEmpty(Mono.defer(() -> {
            Object send = webExchange.getAttributes().get(Const.OPEN_API_HEADER);
            if (send == null) {
                ApiResult apiResult = new ApiResult();
                apiResult.setCode(201);
                apiResult.setMsg(ApiResult.codeMap.get(apiResult.getCode()));
                ServerHttpResponse response = webExchange.getResponse();
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                DataBufferFactory bufferFactory = response.bufferFactory();
                return response.writeWith(Mono.fromSupplier(() -> {
                    return bufferFactory.wrap(JsonUtils.toJsonByte(apiResult));
                }));
            } else {
                webExchange.getAttributes().remove(Const.OPEN_API_HEADER);
            }
            return Mono.empty();
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    protected Mono<Route> lookupRoute(ServerWebExchange exchange) {
        return this.routeLocator.getRoutes()
                .concatMap(route -> Mono.just(route).filterWhen(r -> {
                    return r.getPredicate().apply(exchange);
                })
                        .doOnError(e -> log.error("Error applying predicate for route: " + route.getId(), e))
                        .onErrorResume(e -> Mono.empty()))
                .next().map(r -> {
                    return r;
                });
    }
}
