package com.jcloud.common.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 全局设置
 * @author jiaxm
 * @date 2021/3/26
 */
@Component
public class GlobalImport {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

}
