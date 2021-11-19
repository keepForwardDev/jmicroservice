package com.jcloud;

import com.alibaba.csp.sentinel.log.LogBase;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author jiaxm
 * @date 2021/4/2
 */
@SpringBootApplication(exclude = Knife4jAutoConfiguration.class)
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableFeignClients
public class GatewayApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty(LogBase.LOG_OUTPUT_TYPE, "console");
        SpringApplication.run(GatewayApplication.class, args);
    }

}
