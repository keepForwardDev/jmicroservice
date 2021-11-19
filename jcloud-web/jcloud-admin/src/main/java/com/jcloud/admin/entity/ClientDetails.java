package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
@Data
@Entity
@Table(name = "sys_client_details", uniqueConstraints = {@UniqueConstraint(columnNames = {"clientId"})})
@TableName("sys_client_details")
@org.hibernate.annotations.Table(appliesTo = "sys_client_details", comment = "oauth2客户端详情")
public class ClientDetails extends BaseModel<ClientDetails> {

    /**
     * app_key
     */
    @Column(columnDefinition = "varchar(255) comment '客户端ID'")
    private String clientId;

    @Column(columnDefinition = "varchar(255) comment '应用名称'")
    private String clientName;

    /**
     * 0 待审核 1 已通过 2 未通过
     */
    @Column(columnDefinition = "int(1) comment '审核状态'")
    private Integer status;

    /**
     * 授权得资源id，在调用资源服务器会校验是否授权了该id
     */
    @Column(columnDefinition = "varchar(255) comment '客户端授权资源服务器ID'")
    private String resourceIds;

    /**
     * 类似 appSecret
     */
    @Column(columnDefinition = "varchar(255) comment '客户端密匙'")
    private String clientSecret;

    /**
     * 授权的范围，资源的进一步划分
     */
    @Column(columnDefinition = "varchar(255) comment '授权范围'")
    private String scope;

    /**
     * authorization_code 授权码模式
     * ,password 密码模式
     * ,refresh_token, 刷新令牌模式
     * client_credentials 客户端模式
     */
    @Column(columnDefinition = "varchar(255) comment '支持的授权方式'")
    private String authorizedGrantTypes;

    /**
     * oauth2 登录成功后调用客户端回调url，传递token给客户端
     */
    @Column(columnDefinition = "varchar(255) comment '授权码模式的回调地址'")
    private String webServerRedirectUri;

    /**
     * 客户端角色
     */
    @Column(columnDefinition = "varchar(255) comment '客户端角色'")
    private String authorities;

    /**
     * 生成的token的有效时间，单位为s
     */
    @Column(columnDefinition = "int(11) comment '生成的令牌有效时间'")
    private Integer accessTokenValidity = 24 * 60 * 60;

    /**
     * 生成的刷新令牌有效时间
     */
    @Column(columnDefinition = "int(11) comment '生成的刷新令牌有效时间'")
    private Integer refreshTokenValidity = 3600 * 60 * 60;

    /**
     * 一些额外的信息,存json
     */
    @Column(columnDefinition = "text comment '额外信息'")
    private String additionalInformation;

    /**
     * 是否自动确认，将此改为true 将可以在授权时需要用户的确认
     */
    @Column(columnDefinition = "varchar(255) comment '是否自动确认，将此改为true 将可以在授权时需要用户的确认'")
    private String autoapprove;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    private String remarks;

}
