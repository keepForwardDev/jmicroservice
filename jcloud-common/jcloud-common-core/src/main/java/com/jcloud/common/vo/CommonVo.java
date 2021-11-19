package com.jcloud.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcloud.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 基础输出bean
 * @author jiaxm
 * @date 2021/4/27
 */
@Data
public class CommonVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = DateUtil.TIME_PATTERN)
    @DateTimeFormat(pattern = DateUtil.TIME_PATTERN)
    private Date updateTime;

}
