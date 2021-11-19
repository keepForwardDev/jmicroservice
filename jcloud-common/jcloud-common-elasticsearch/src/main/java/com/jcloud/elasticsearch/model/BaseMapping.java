package com.jcloud.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class BaseMapping {

    @Id
    @Field(type = FieldType.Keyword)
    protected Long id;//自增id

    @Field(type = FieldType.Integer)
    protected Integer deleted=0;//删除状态，0表示未删除，1表示已删除

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    protected Date createTime;//创建时间

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    protected Date updateTime;//

    @Field(type = FieldType.Long)
    protected Long updateUserId;

    @Field(type = FieldType.Long)
    protected Long createUserId;//创建者

    @Field(type = FieldType.Keyword)
    private String uuid;

    @Field(type = FieldType.Integer)
    private Integer version = 0; // 版本号

    /**
     * 数据来源
     */
    @Field(type = FieldType.Long)
    private Long sourceFrom;
}
