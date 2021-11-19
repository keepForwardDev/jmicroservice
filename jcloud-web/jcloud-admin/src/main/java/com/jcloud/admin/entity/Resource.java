package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name="sys_resource", uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@TableName("sys_resource")
@org.hibernate.annotations.Table(appliesTo = "sys_resource", comment = "自定义权限")
public class Resource extends AutoIdModel<Resource> {

    /**
     * 资源编码， 唯一
     */
    @Column(columnDefinition = "varchar(255) comment '系统唯一编码'")
    private String code;

    /**
     * 资源链接
     */
    @Column(columnDefinition = "varchar(255) comment '权限链接'")
    private String url;

    /**
     * 资源名称
     */
    @Column(columnDefinition = "varchar(255) comment '权限名称'")
    private String name;

    /**
     * 资源描述
     */
    @Column(columnDefinition = "varchar(255) comment '权限描述'")
    private String description;

    /**
     * 资源展示排序
     */
    @Column(columnDefinition = "bigint(20) comment '排序号'")
    private Integer sort=0;

    /**
     * 所属父级
     */
    @Column(columnDefinition = "bigint(20) comment '所属父级'")
    private Long parentId = 0l;

    /**
     * 所属菜单
     */
    @Column(columnDefinition = "varchar(255) comment '所属菜单'")
    private Long menuId;

}
