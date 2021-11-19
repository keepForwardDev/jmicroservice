package com.jcloud.gateway.controller;

import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.jcloud.gateway.config.SwaggerResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/8/4
 */
@RestController
public class IndexHandler {

    @Autowired
    private SwaggerResourceConfig swaggerResource;

    /**
     * 获取全局唯一请求ID
     * @return
     */
    @RequestMapping("getRequestSn")
    public Mono<ResponseEntity<String>> getRequestSn() {
        return Mono.just(new ResponseEntity<>(UuidUtils.generateUuid().replace("-", StringUtils.EMPTY), HttpStatus.OK));
    }

    @RequestMapping("apiResources")
    public Mono<ResponseEntity<List<SwaggerResource>>> apiResources() {
        return Mono.just(new ResponseEntity<>(swaggerResource.getResource(true), HttpStatus.OK));
    }
}
