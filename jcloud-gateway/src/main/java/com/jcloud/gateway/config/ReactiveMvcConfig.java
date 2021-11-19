package com.jcloud.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.stream.Collectors;

/**
 * @author jiaxm
 * @date 2021/8/11
 */
@Configuration
public class ReactiveMvcConfig implements WebFluxConfigurer {

    @Value("${system.extPath}")
    private String extPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传路径资源配置
        registry.addResourceHandler("/ext/**")
                .addResourceLocations("file:"+ extPath + "/");
    }


    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

}
