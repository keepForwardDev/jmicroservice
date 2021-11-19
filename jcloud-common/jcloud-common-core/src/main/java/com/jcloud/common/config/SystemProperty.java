package com.jcloud.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 系统参数
 *
 * @author jiaxm
 * @date 2020/10/21
 */
@Component
@ConfigurationProperties(value = "system")
@RefreshScope
@Data
public class SystemProperty {

    /**
     * 外部文件地址，用于存放一些静态文件，或者上传文件，可以通过/ext 获取
     */
    private String extPath;

    /**
     * 中台host
     */
    private String domain;

    /**
     * 超级密码
     */
    private String superPassword;

    /**
     * 默认注册用户密码
     */
    private String defaultPassword;

    /**
     * 是否开启元数据表查询统计
     */
    private Boolean enableMetaTableCount;

    /**
     * 是否开启登录验证码
     */
    private Boolean openLoginCode;

}
