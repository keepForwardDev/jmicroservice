package com.jcloud;

import com.jcloud.security.annotation.EnableJResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiaxm
 * @date 2021/4/13
 */
@EnableDiscoveryClient
@EnableJResourceServer
@SpringBootApplication
public class DictionaryWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictionaryWebApplication.class, args);
    }
}
