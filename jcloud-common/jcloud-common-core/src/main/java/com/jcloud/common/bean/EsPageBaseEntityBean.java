package com.jcloud.common.bean;

import lombok.Data;

@Data
public class EsPageBaseEntityBean extends BaseEntityBean {

    /**
     * 当前页数 从第一页开始
     */
    private int currentPage=1;

    /**
     *每页条数
     */
    private int pageSize=20;



}
