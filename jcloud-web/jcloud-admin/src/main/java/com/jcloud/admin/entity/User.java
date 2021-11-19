package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.AutoIdModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_user", uniqueConstraints={@UniqueConstraint(columnNames={"phone"}), @UniqueConstraint(columnNames={"account"}), @UniqueConstraint(columnNames={"unionId"})})
@TableName("sys_user")
@org.hibernate.annotations.Table(appliesTo = "sys_user", comment = "用户")
public class User extends AutoIdModel<User> {
    /**
     * 头像
     */
    @Column(columnDefinition = "varchar(255) comment '头像'")
    private String avatar;

    /**
     * 账号
     */
    @Column(columnDefinition = "varchar(255) comment '账号'")
    private String account;

    /**
     * 密码
     */
    @Column(columnDefinition = "varchar(255) comment '密码'")
    private String password;

    /**
     * 真实名字
     */
    @Column(columnDefinition = "varchar(255) comment '姓名'")
    private String name;

    /**
     * 生日
     */
    @Column(columnDefinition = "datetime(6) comment '生日'")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Column(columnDefinition = "int(1) comment '性别'")
    private Integer sex = 1;

    /**
     * 电话
     */
    @Column(columnDefinition = "varchar(255) comment '电话'")
    private String phone;

    /**
     * 电子邮件
     */
    @Column(columnDefinition = "varchar(255) comment '电子邮箱'")
    private String email;

    /**
     * 地址
     */
    @Column(columnDefinition = "varchar(255) comment '地址'")
    private String address;

    /**
     * 是否启用 0 否 1 是
     */
    @Column(columnDefinition = "int(1) comment '是否启用'")
    private Integer enabled = 0;


    /**
     * 职务
     */
    @Column(columnDefinition = "varchar(255) comment '职务'")
    private String duty;

    @Column(columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 用户类型 0 系统用户，1外部用户
     */
    @Column(columnDefinition = "int(1) comment '用户类型'")
    private Integer type = 0;

    /**
     * 同一微信公众号下面，用户唯一id相同
     */
    @Column(columnDefinition = "varchar(255) comment '微信union id'")
    private String unionId;

    /**
     * 不同小程序openid不同
     */
    @Column(columnDefinition = "varchar(255) comment '微信open id'")
    private String openid;
}
