package com.jcloud.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应用需要传 token appKey  nonce timeStamp
 * 用户只需传 accessToken
 * @author jiaxm
 * @date 2021/9/8
 */
@Data
@ApiModel(value = "api请求头参数")
public class ApiRequest {

    /**
     * oauth2 access_token
     */
    @ApiModelProperty(value = "用户登录token")
    private String accessToken;


    /**
     * client token
     */
    @ApiModelProperty(value = "应用token")
    private String token;

    /**
     * client id
     */
    @ApiModelProperty(value = "应用app_key")
    private String appKey;

    /**
     * 随机值
     */
    @ApiModelProperty(value = "随机值，应保证30s不重复")
    private String nonce;

    /**
     * 时间戳 16进制
     */
    @ApiModelProperty(value = "时间戳，16进制")
    private String timeStamp;

    /**
     * api请求地址
     */
    @ApiModelProperty(hidden = true)
    private String apiPath;

    /**
     * 服务id
     */
    @ApiModelProperty(hidden = true)
    private String serviceId;
}
