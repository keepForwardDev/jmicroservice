package com.jcloud.orm.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jiaxm
 * @date 2021/8/12
 */
@Getter
@Setter
@MappedSuperclass
public class AutoIdModel<T extends BaseModel<?>> extends BaseModel<T> implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '主键ID'")
    protected Long id;//自增id
}
