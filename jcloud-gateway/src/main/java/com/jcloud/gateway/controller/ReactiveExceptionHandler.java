package com.jcloud.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/8/9
 */
@Slf4j
@Component
@Order(-1)
public class ReactiveExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        //log.error(ExceptionUtils.getStackTrace(ex));
        ServerHttpResponse httpResponse = exchange.getResponse();
        Map<String, Object> response = new HashMap<>();
        response.put("code", -1);
        response.put("msg", ex.getMessage());
        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof NotFoundException) {
            NotFoundException notFoundException = ((NotFoundException) ex);
            httpResponse.setStatusCode(notFoundException.getStatus());
            response.put("description", "服务未开启，请稍后再试！");
        } else {
            response.put("description", "服务异常，请稍后再试！");
        }
        return httpResponse.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = httpResponse.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(response));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }));
    }
}
