package com.jcloud.orm.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class ParentSonModel extends BaseModel{

    /**
     * uuid 根节点uuid
     */

    public String uuid = "";
    /**
     * 父uuid
     */
    public String parentUuid="";

    /**
     * 描述
     */
    public String description;

    /**
     * 专利数量
     */
    public Integer number=0;
}
