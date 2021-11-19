package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Data
@Entity
@Table(name = "sys_role_privileges", indexes = {@Index(columnList = "roleId,resourceType, serviceId")})
@TableName("sys_role_privileges")
@org.hibernate.annotations.Table(appliesTo = "sys_role_privileges", comment = "角色权限")
public class RolePrivileges extends AutoIdModel<RolePrivileges> {

    @Column(columnDefinition = "bigint(20) comment '关联的角色ID'")
    private Long roleId;

    @Column(columnDefinition = "int(1) comment '0 菜单权限 1 api权限 2 自定义权限'")
    private Integer resourceType = 0;


    @Column(columnDefinition = "bigint(20) comment '关联ID'")
    private Long relateId;

    @Column(columnDefinition = "varchar(255) comment 'api所属服务ID'")
    private String serviceId;

    @Column(columnDefinition = "varchar(255) comment '系统api路径'")
    private String apiPath;

    @Column(columnDefinition = "varchar(255) comment 'api调用次数 -1 为无限次数'")
    private Long apiLimit = -1l;

    @Column(columnDefinition = "int(1) comment 'api调用限制策略，0 按次数限制 1 按每天限制'")
    private Integer apiLimitStrategy;

    @Column(columnDefinition = "int(20) comment '资源排序号'")
    private Integer sort = 0;

}
