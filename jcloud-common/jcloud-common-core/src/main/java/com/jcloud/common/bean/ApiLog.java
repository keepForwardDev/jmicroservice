package com.jcloud.common.bean;

/**
 * @author jiaxm
 * @date 2021/9/8
 */
public class ApiLog {

    /**
     * 所属服务id
     */
    private String serviceId;

    /**
     * 请求路径
     */
    private String apiPath;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 应用key
     */
    private String appKey;

    /**
     * 用户token
     */
    private String accessToken;

    /**
     * 请求参数map
     */
    private String requestParam;
}
