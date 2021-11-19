package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "sys_department", uniqueConstraints = {@UniqueConstraint(columnNames = {"levelCode"})})
@TableName("sys_department")
@org.hibernate.annotations.Table(appliesTo = "sys_department", comment = "部门")
public class Department extends AutoIdModel<Department> {
    /**
     * 公司名称
     */
    @Column(columnDefinition = "varchar(255) comment '公司名称'")
    private String name;

    /**
     * 父级id
     */
    @Column(columnDefinition = "bigint(20) comment '父级ID'")
    private Long parentId;

    /**
     * 全名
     */
    @Column(columnDefinition = "varchar(255) comment '全名'")
    private String fullName;

    /**
     * 部门编码，主要是为了更好的查找下级，请勿在代码中直接应用
     */
    @Column(columnDefinition = "varchar(255) comment '部门编码'")
    private String levelCode;

    /**
     * 排序号
     */
    @Column(columnDefinition = "int(11) comment '排序号'")
    private Integer sort;

    @Column(columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
