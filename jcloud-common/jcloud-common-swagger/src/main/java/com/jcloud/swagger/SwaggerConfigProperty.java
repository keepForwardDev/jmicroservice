package com.jcloud.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger 配置参数
 * @author jiaxm
 * @date 2021/4/6
 */
@ConfigurationProperties(value = "swagger")
@Configuration
@Data
public class SwaggerConfigProperty {

    /**
     * swagger 扫描的包
     */
    private String basePackage;

    /**
     * 哪些路径不生成文档
     */
    private List<String> excludePath = new ArrayList<>();

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 服务地址
     */
    private String termsOfServiceUrl;

    /**
     * 获取token的地址
     */
    private String tokenUrl;

    /**
     * 分组名称
     */
    private String groupName;

    private String host;

    private String contactName;

    private String contactUrl;

    private String contactEmail;
}
