package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 路由菜单
 */
@Data
@Table(name="sys_menu")
@TableName("sys_menu")
@Entity
@org.hibernate.annotations.Table(appliesTo = "sys_menu", comment = "菜单")
public class Menu extends AutoIdModel<Menu> {
    /**
     * 页面的export_name
     */
    @Column(columnDefinition = "varchar(255) comment 'vue export name' ")
    private String name;

    /**
     * 菜单名称
     */
    @Column(columnDefinition = "varchar(255) comment '菜单名称' ")
    private String title;

    @Column(columnDefinition = "int(1) comment '是否有子菜单' ")
    private Integer hasChildren;

    @Column(columnDefinition = "int(1) comment '是否隐藏菜单'")
    private Integer hidden;

    /**
     * 重定向地址
     */
    @Column(columnDefinition = "varchar(255) comment '重定向地址' ")
    private String redirect;

    /**
     * 路由meta
     */
    @Column(columnDefinition = "varchar(255) comment '路由meta' ")
    private String meta;

    @Column(columnDefinition = "int(11) comment '排序号' ")
    private Integer sort = 0;

    @Column(columnDefinition = "bigint(11) comment '父级ID' ")
    private Long parentId = 0l;

    /**
     * 路由访问地址
     */
    @Column(columnDefinition = "varchar(255) comment '路由访问地址' ")
    private String path;

    /**
     * 组件地址
     */
    @Column(columnDefinition = "varchar(255) comment '组件地址' ")
    private String component;

    /**
     * 菜单类型-字典id
     */
    @Column(columnDefinition = "int(11) comment '菜单类型' ")
    private Long type = 0l;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) comment '备注' ")
    private String remark;

}
