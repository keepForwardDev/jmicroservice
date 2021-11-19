package com.jcloud.orm.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 关联基础表
 * @author jiaxm
 * @date 2021/6/29
 */
@MappedSuperclass
@Data
public class RefModel <T extends RefModel<?>> extends Model<T> implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '主键ID'")
    protected Long id;//自增id

    @Column(columnDefinition = "datetime(6) comment '创建时间'")
    protected Date createTime;//创建时间

    @Column(columnDefinition = "datetime(6) comment '更新时间'")
    protected Date updateTime;//

    @Column(columnDefinition = "bigint(20) comment '更新人用户ID'")
    protected Long updateUserId;

    @Column(columnDefinition = "bigint(20) comment '创建人用户ID'")
    protected Long createUserId;//创建者

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
