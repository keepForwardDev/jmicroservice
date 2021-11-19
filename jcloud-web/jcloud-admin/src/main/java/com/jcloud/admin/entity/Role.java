package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 角色表
 */
@Data
@Entity
@Table(name = "sys_role", uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@TableName("sys_role")
@org.hibernate.annotations.Table(appliesTo = "sys_role", comment = "角色")
public class Role extends AutoIdModel<Role> {
    /**
     * 角色名称
     */
    @Column(columnDefinition = "varchar(255) comment '角色名称'")
    private String name;

    /**
     * 描述
     */
    @Column(columnDefinition = "varchar(255) comment '描述'")
    private String description;

    /**
     * 角色编码，唯一
     */
    @Column(columnDefinition = "varchar(255) comment '角色编码'")
    private String code;

}
