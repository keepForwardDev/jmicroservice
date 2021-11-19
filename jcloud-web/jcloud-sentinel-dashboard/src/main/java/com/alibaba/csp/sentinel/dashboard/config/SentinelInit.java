package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiaxm
 * @date 2021/4/15
 */
@Configuration
public class SentinelInit implements InitializingBean {

    /**
     * 控制台地址
     */
    @Value("${spring.cloud.sentinel.transport.dashboard}")
    private String dashboardServer;

    @Value("${spring.cloud.sentinel.transport.port}")
    private Integer apiPort;


    @Override
    public void afterPropertiesSet() throws Exception {
        SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, dashboardServer);
        SentinelConfig.setConfig(TransportConfig.SERVER_PORT, apiPort.toString());
        triggerSentinelInit();
    }

    private static void triggerSentinelInit() {
        new Thread(() -> InitExecutor.doInit()).start();
    }
}
