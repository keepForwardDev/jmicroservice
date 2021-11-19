package com.jcloud.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcloud.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value = "基础输入bean")
public class BaseEntityBean {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "创建时间，传入不会被覆盖")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    private Date createTime;

    /**
     * 是否使用默认排序
     */
    @ApiModelProperty(value = "是否使用默认排序")
    private Boolean useDefaultOrder = true;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String orderField;

    /**
     * 排序方式
     */
    @ApiModelProperty(value = "排序方式")
    private String orderWay = "descending";

    @ApiModelProperty(value = "数据uuid，传入不会覆盖", hidden = true)
    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonIgnore
    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    @JsonIgnore
    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getUseDefaultOrder() {
        return useDefaultOrder;
    }

    public void setUseDefaultOrder(Boolean useDefaultOrder) {
        this.useDefaultOrder = useDefaultOrder;
    }
}
