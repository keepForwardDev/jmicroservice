package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jcloud.orm.model.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@Data
@Entity
@Table(name = "sys_log", indexes = {@Index(columnList = "type"), @Index(columnList = "clientId"), @Index(columnList = "apiPath")})
@TableName("sys_log")
public class SysLog extends BaseModel<SysLog> {
    /**
     * 日志类型, 0 登录日志，其余的在后面一次新增, 1 open api 调用
     */
    @Column(columnDefinition = "int(2) comment '日志类型'")
    private Integer type;
    /**
     * 日志标题
     */
    @Column(columnDefinition = "varchar(255) comment '日志标题'")
    private String title;

    /**
     * 日志内容
     */
    @Column(columnDefinition = "text comment '日志类容'")
    private String content;

    @Column(columnDefinition = "varchar(32) comment '客户端ID'")
    private String clientId;

    /**
     * 操作IP地址
     */
    @Column(columnDefinition = "varchar(255) comment '操作IP'")
    private String remoteAddr;

    @Column(columnDefinition = "varchar(255) comment '浏览器标识'")
    private String userAgent;

    @Column(columnDefinition = "varchar(255) comment '系统api地址'")
    private String apiPath;

    /**
     * 请求URI
     */
    @Column(columnDefinition = "varchar(255) comment '请求路径'")
    private String requestUri;

    @Column(columnDefinition = "varchar(255) comment '请求参数'")
    private String queryString;

    /**
     * 异常信息
     */
    @Column(columnDefinition = "text comment '异常信息'")
    private String exception;
}
