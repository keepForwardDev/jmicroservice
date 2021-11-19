package com.jcloud.admin.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 权限参数
 * @author jiaxm
 * @date 2021/9/2
 */
@ApiModel(value = "权限获取")
@Data
public class PrivilegeBean {

    @ApiModelProperty(value = "权限承载类型 0 用户 1角色 2应用")
    private Integer privilegeType;

    @ApiModelProperty(value = "资源类型 0 菜单权限 1 api权限 2 自定义权限")
    private Integer resourceType;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "服务ID名")
    private String serviceId;

    @ApiModelProperty(value = "是否返回apiTree")
    private Integer showApiTree = 1;

}
