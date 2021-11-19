package com.jcloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础列表入参
 * @author jiaxm
 * @date 2021/4/27
 */
@Data
public class CommonListDto {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage = 1;


    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String orderField;

    /**
     * 排序方式
     */
    @ApiModelProperty(value = "排序方式", allowableValues = "descending,ascending")
    private String orderWay = "descending";

}
