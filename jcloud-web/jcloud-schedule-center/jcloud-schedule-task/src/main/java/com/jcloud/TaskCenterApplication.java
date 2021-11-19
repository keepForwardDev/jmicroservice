package com.jcloud;

import com.jcloud.security.annotation.EnableJResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 调度任务中心
 * @author jiaxm
 * @date 2021/4/14
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJResourceServer
@EnableFeignClients
public class TaskCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskCenterApplication.class, args);
    }
}
