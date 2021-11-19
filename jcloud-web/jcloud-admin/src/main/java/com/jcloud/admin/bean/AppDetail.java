package com.jcloud.admin.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "客户端详情")
public class AppDetail {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户端app_key")
    private String clientId;

    @ApiModelProperty(value = "客户端名称")
    private String clientName;

    @ApiModelProperty(value = "所属用户")
    private String createUserName;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "所属角色模板ID")
    private List<Long> roleIds = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "回调地址")
    private String webServerRedirectUri;

    @ApiModelProperty(value = "uuid")
    private String uuid;


    private Integer deleted = 0;
}
