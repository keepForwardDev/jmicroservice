package com.jcloud.dictionary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
@Data
@MappedSuperclass
public class DictionaryBase implements Serializable {

    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级
     */
    private Long parentId;
}
