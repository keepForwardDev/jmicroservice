package com.jcloud.admin.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 日志过滤参数
 * @author jiaxm
 * @date 2021/11/24
 */
@Data
@ApiModel(value = "日志过滤参数")
public class LogFilter {

    @ApiModelProperty(value = "日志类")
    private String clazz;

    @ApiModelProperty(value = "日志级别")
    private String level;

    @ApiModelProperty(value = "日志内容")
    private String message;

    @ApiModelProperty(value = "创建时间-min")
    private Date lCreateTime;

    @ApiModelProperty(value = "创建时间-max")
    private Date RCreateTime;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "来源ip")
    private String sourceFrom;

    @ApiModelProperty(value = "是否展示表格")
    private Boolean showTable;
}
