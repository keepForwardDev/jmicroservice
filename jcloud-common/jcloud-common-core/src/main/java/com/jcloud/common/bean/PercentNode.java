package com.jcloud.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jiaxm
 * @date 2021/5/20
 */
@ApiModel(value = "百分比模型输出")
@Data
public class PercentNode extends ItemNode {

    @ApiModelProperty(value = "占比")
    private String percent;

}
