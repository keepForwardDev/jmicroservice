package com.jcloud.admin.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页统计
 * @author jiaxm
 * @date 2021/9/23
 */
@Data
@ApiModel(value = "首页统计")
public class IndexStat {

    @ApiModelProperty(value = "用户数")
    private Integer userNumber;

    @ApiModelProperty(value = "应用数")
    private Integer clientNumber;

    @ApiModelProperty(value = "部门数")
    private Integer departmentNumber;

    @ApiModelProperty(value = "今日新增用户")
    private Integer currentCreateUser;

    @ApiModelProperty(value = "今日api调用次数")
    private Integer currentApiCall;

    @ApiModelProperty(value = "今日用户登录次数")
    private Integer currentUserLogin;

    @ApiModelProperty(value = "api调用总数")
    private Integer apiCall;

}
