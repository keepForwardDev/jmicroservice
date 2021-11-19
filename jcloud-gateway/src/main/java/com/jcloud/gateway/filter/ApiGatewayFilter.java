package com.jcloud.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.remote.admin.ApiPrivilegesRemoteService;
import com.jcloud.remote.admin.LogRemoteService;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CLIENT_RESPONSE_CONN_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 用于api授权，校验api权限
 *
 * @author jiaxm
 * @date 2021/9/7
 * @deprecated 此方式耦合较高
 */
@Slf4j
public class ApiGatewayFilter implements GatewayFilter, Ordered {

    private ApiPrivilegesRemoteService apiPrivilegesRemoteService;

    private RouteLocator routeLocator;

    private ObjectMapper objectMapper;

    private LogRemoteService logRemoteService;

    public ApiGatewayFilter(ApiPrivilegesRemoteService apiPrivilegesRemoteService, RouteLocator routeLocator, ObjectMapper objectMapper, LogRemoteService logRemoteService) {
        this.apiPrivilegesRemoteService = apiPrivilegesRemoteService;
        this.routeLocator = routeLocator;
        this.objectMapper = objectMapper;
        this.logRemoteService = logRemoteService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getRawPath().replace("/api", StringUtils.EMPTY);
        if (StringUtils.isNotBlank(path)) {
            exchange = exchange.mutate().request(exchange.getRequest().mutate().path(path).build()).build();
        }
        final ServerWebExchange webExchange = exchange;
        return lookupRoute(exchange).map(r -> {
            webExchange.getAttributes().put(GATEWAY_ROUTE_ATTR, r);
            return apiPrivilegesRemoteService.getOpenApiPrivileges(exactParams(webExchange, r));
        }).flatMap(r -> {
            return processApiCheckResult(r, webExchange, chain);
        });
    }

    private Mono<Void> processApiCheckResult(ApiResult apiResult, ServerWebExchange webExchange, GatewayFilterChain chain) {
        if (apiResult.getCode() == 200) { // 鉴权成功
            return chain.filter(webExchange).then(Mono.defer(() -> {
                Connection connection = webExchange.getAttribute(CLIENT_RESPONSE_CONN_ATTR);
                webExchange.getAttributes().remove(CLIENT_RESPONSE_CONN_ATTR);
                if (connection == null) {
                    return Mono.empty();
                }
                ServerHttpResponse response = webExchange.getResponse();
                final Flux<DataBuffer> body = connection
                        .inbound()
                        .receive()
                        .retain()
                        .map(byteBuf -> wrap(webExchange, byteBuf, response, apiResult));
                return response.writeWith(body);
            })).doOnCancel(() -> cleanup(webExchange));
        } else { // 返回失败信息
            ServerHttpResponse response = webExchange.getResponse();
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            DataBufferFactory bufferFactory = response.bufferFactory();
            return response.writeWith(Mono.fromSupplier(() -> {
                return bufferFactory.wrap(toJsonByte(apiResult));
            }));
        }
    }

    protected DataBuffer wrap(ServerWebExchange webExchange, ByteBuf byteBuf, ServerHttpResponse response, ApiResult resultMap) {
        DataBufferFactory bufferFactory = response.bufferFactory();
        String content = convertByteBufToString(byteBuf);
        ResponseData apiResult = readObject(content, ResponseData.class);
        if (apiResult.getCode() == 1) {
            // 生成订单 记录日志
            ResponseData logResultMap = saveApiLog(webExchange, resultMap.getOrderNumber());
            if (logResultMap.getCode() == 1) {
                resultMap.setOrderNumber(logResultMap.getData().toString());
                resultMap.setData(apiResult.getData());
                resultMap.setReserveData(apiResult.getReserveData());
            } else {
                resultMap.setCode(ApiResult.API_ERROR);
                resultMap.setMsg(apiResult.getMsg());
            }
        } else {
            resultMap.setCode(ApiResult.API_ERROR);
            resultMap.setMsg(ApiResult.codeMap.get(resultMap.getCode()));
            resultMap.setReserveData(apiResult);
            resultMap.setOrderNumber(null);
        }
        return bufferFactory.wrap(toJsonByte(resultMap));
    }

    private void cleanup(ServerWebExchange exchange) {
        Connection connection = exchange.getAttribute(CLIENT_RESPONSE_CONN_ATTR);
        if (connection != null && connection.channel().isActive() && !connection.isPersistent()) {
            connection.dispose();
        }
    }

    private ApiRequest exactParams(ServerWebExchange exchange, Route route) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setToken(httpHeaders.getFirst("Token"));
        apiRequest.setAccessToken( httpHeaders.getFirst("AccessToken"));
        apiRequest.setAppKey(httpHeaders.getFirst("AppKey"));
        apiRequest.setNonce(httpHeaders.getFirst("Nonce"));
        apiRequest.setTimeStamp(httpHeaders.getFirst("TimeStamp"));
        apiRequest.setApiPath(getApiPath(exchange.getRequest().getURI().getRawPath(), route));
        apiRequest.setServiceId(route.getId());
        return apiRequest;
    }

    private String getApiPath(String originUri, Route route) {
        // because arrays.asList is not editable
        List<String> pathArray = new ArrayList<>(Arrays.asList(originUri.split("/")));
        List<GatewayFilter> gatewayFilterList = route.getFilters();
        for (GatewayFilter gatewayFilter : gatewayFilterList) {
            // [[StripPrefix parts = 1], order = 1]
            String filterString = gatewayFilter.toString();
            if (filterString.contains("StripPrefix")) {
                // 获取截断的个数
                String parts = "parts = ";
                String regex = parts + "[0-9]{1,}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(filterString);
                String matchString = null;
                if (matcher.find()) {
                    matchString = matcher.group(0);
                }
                Integer part = Integer.valueOf(matchString.replace(parts, StringUtils.EMPTY));
                pathArray.remove(part.intValue());
                return StringUtils.join(pathArray, "/");
            }
        }
        return originUri;
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

    public byte[] toJsonByte(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if (buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }

    public <T> T readObject(String value, Class<T> clazz) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(value)) {
                return null;
            }
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ResponseData saveApiLog(ServerWebExchange webExchange, String apiInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);
        params.put("title", "api 日志");
        params.put("content", apiInfo);
        InetSocketAddress remoteAddress = webExchange.getRequest().getRemoteAddress();
        String clientIp = Objects.requireNonNull(remoteAddress).getAddress().getHostAddress();
        params.put("remoteAddr", clientIp);
        params.put("userAgent", webExchange.getRequest().getHeaders().getFirst("user-agent"));
        params.put("requestUri", webExchange.getRequest().getURI().toString());
        ResponseData logMap = logRemoteService.saveApiOrder(params);
        return logMap;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
