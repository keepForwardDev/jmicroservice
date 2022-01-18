package com.jcloud.dictionary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 字典
 *
 * @author jiaxm
 * @date 2021/4/12
 */
@Data
@TableName("d_simple_dictionary")
@Table(name = "d_simple_dictionary")
@Entity
@org.hibernate.annotations.Table(appliesTo = "d_simple_dictionary", comment = "常规字典")
public class SimpleDictionaryEntity extends DictionaryBase {
    private static final Long serialVersionUID = -21993193804594923l;
    private Date createTime;
    private Integer deleted;
    private Date updateTime;
    private String nameKey;
    private Date modifyTime;
    private Long createUserId;
    private Integer sortNum;
    private Integer dataVal;
    private String remark;
    private String uuid;
    private String parentUuid;
}
