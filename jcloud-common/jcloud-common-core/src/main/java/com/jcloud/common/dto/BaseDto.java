package com.jcloud.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class BaseDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 逻辑删除状态
     */
    @ApiModelProperty(value = "逻辑删除状态")
    private Integer deleted=0;//删除状态，0表示未删除，1表示已删除

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;//创建时间

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//

    /**
     * 更新人用户ID
     */
    @ApiModelProperty(value = "更新人用户ID")
    private Long updateUserId;

    /**
     * 创建人用户ID
     */
    @ApiModelProperty(value = "创建人用户ID")
    private Long createUserId;//创建者

    /**
     * 数据uuid
     */
    @ApiModelProperty(value = "uuid")
    private String uuid;

    /**
     * 数据版本号
     */
    @ApiModelProperty(value = "版本号")
    private Integer version = 0; // 版本号

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     * 数据来源，simple dictionary 字典表
     */
    @ApiModelProperty(value = "数据来源")
    private Long sourceFrom;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId = 0L;//省

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId = 0L;//市

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId = 0L;//区

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId = 0L;//街道

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(Long sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }
}
