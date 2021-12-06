package com.jcloud.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

/**
 * @author jiaxm
 * @date 2021/11/24
 */
@Document(indexName = "datacenterlog", createIndex = false, useServerConfiguration = true)
@Setting(settingPath = "/elasticsearch_index_setting.json")
@Data
public class AppLog {


    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * 日志时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 日志级别
     */
    @Field(type = FieldType.Keyword)
    private String level;

    /**
     * 线程号
     */
    @Field(type = FieldType.Integer)
    private Integer pid;

    /**
     * 线程名称
     */
    @Field(type = FieldType.Keyword)
    private String threadName;


    /**
     * 日志输出类
     */
    @Field(type = FieldType.Text, analyzer = "point")
    private String clazz;


    /**
     * 日志内容
     */
    @Field(type = FieldType.Text)
    private String message;


    /**
     * 日志类，对应filebeat中的配置文件
     */
    @Field(type = FieldType.Keyword)
    private String logType;


    /**
     * 项目名称，对应配置文件的log.name
     */
    @Field(type = FieldType.Keyword)
    private String projectName;

    /**
     * 日志服务器的ip地址，对应filebeat配置文件
     */
    @Field(type = FieldType.Ip)
    private String sourceFrom;

}
