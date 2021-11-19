package com.jcloud.dictionary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
@Data
@TableName(value = "d_full_city")
@Table(name = "d_full_city")
@Entity
@org.hibernate.annotations.Table(appliesTo = "d_full_city", comment = "省市区-字典")
public class FullCityEntity extends DictionaryBase {
    private Date createTime;
    private Long createUserId;
    private Integer deleted;
    private Date modifyTime;
    private Date updateTime;
    private String adminCode;
    /**
     * 地区编码
     */
    private String areaCode;
    private Integer level;
    private Integer p;

    /**
     * 全拼
     */
    private String pinyin;

    /**
     * 第一个字母大写
     */
    private String pyFirstCharacter;

    /**
     * 邮编
     */
    private String postCode;
    private Integer tagSort;
    private String code;
    private String remark;
    private String lat;
    private String lng;
    private Integer zoneType;

    /**
     * 是否热门城市
     */
    private Integer hotFlag = 0;
}
