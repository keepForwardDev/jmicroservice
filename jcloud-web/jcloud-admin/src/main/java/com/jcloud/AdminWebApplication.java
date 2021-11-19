package com.jcloud;

import com.jcloud.security.annotation.EnableJResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxm
 * @date 2021/3/12
 */
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableDiscoveryClient
@EnableJResourceServer
@EnableFeignClients
@RestController
public class AdminWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AdminWebApplication.class, args);
    }
}
