package com.jcloud.orm.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseModel<T extends BaseModel<?>> extends Model<T> implements Serializable{

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '主键ID'")
    protected Long id;//自增id

    @JsonIgnore
    @Column(columnDefinition = "int(1) comment '逻辑删除状态'")
    protected Integer deleted=0;//删除状态，0表示未删除，1表示已删除

    @Column(columnDefinition = "datetime(6) comment '创建时间'")
    protected Date createTime;//创建时间

    @Column(columnDefinition = "datetime(6) comment '更新时间'")
    protected Date updateTime;//

    @Column(columnDefinition = "bigint(20) comment '更新人用户ID'")
    protected Long updateUserId;

    @Column(columnDefinition = "bigint(20) comment '创建人用户ID'")
    protected Long createUserId;//创建者

    @Column(columnDefinition = "varchar(255) comment '数据uuid'")
    private String uuid;

    @JsonIgnore
    @Column(columnDefinition = "int(11) comment '数据版本号'")
    private Integer version = 0; // 版本号

    /**
     * 部门id
     */
    @Column(columnDefinition = "bigint(20) comment '所属部门id'")
    private Long departmentId;

    /**
     * 数据来源，simple dictionary 字典表
     */
    @Column(columnDefinition = "bigint(20) comment '数据来源-字典'")
    private Long sourceFrom;


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
    public Long getCreateUserId()
    {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
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

    /**
     * id 是否有效
     * @return
     */
    public boolean idNotNull() {
        return idNotNull(getId());
    }

    public boolean idNotNull(Long id) {
        return getId() != null && getId() > 0;
    }

    public Long getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(Long sourceFrom) {
        this.sourceFrom = sourceFrom;
    }
}
