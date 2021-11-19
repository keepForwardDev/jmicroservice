package com.jcloud.remote.config;

import com.jcloud.common.consts.Const;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 开发环境生效
 * @author jiaxm
 * @date 2021/5/13
 */
@Profile(Const.PROFILE_DEV)
@Configuration
public class LocalLoadBalanceConfig {

    @LoadBalancerClients(defaultConfiguration = {LocalLoadBalanceBean.class})
    private class Config {

    }

}
