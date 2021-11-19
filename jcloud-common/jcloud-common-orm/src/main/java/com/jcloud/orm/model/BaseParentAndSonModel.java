package com.jcloud.orm.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author laiguowei 1276293854@qq.com
 * @version 1.0
 * @date 2021/7/4 下午2:43
 */
@Data
@MappedSuperclass
public class BaseParentAndSonModel<T> extends AutoIdModel {

    private String name;

//    public String uuid;

    private String parentUuid;
}
