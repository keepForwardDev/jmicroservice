package com.jcloud.elasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author jiaxm
 * @date 2021/4/28
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jcloud")
public class ElasticsearchConfig {

}
