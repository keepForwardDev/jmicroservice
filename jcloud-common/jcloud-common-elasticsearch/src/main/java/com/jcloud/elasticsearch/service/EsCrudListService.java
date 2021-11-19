package com.jcloud.elasticsearch.service;

import com.jcloud.common.domain.ResponseData;
import com.jcloud.elasticsearch.domain.EsPage;

import java.util.List;

/**
 *
 * @param <T> es entity
 * @param <B> mysql entity or transformBean
 */
public interface EsCrudListService<T, B> {

    /**
     * 新增
     * @param bean
     * @return
     */
    public T saveEntity(B bean);

    /**
     * 列表
     * @param page
     * @param params
     * @return
     */
    public ResponseData pageList(EsPage page, Object params);

    /**
     * 物理删除
     * @param id
     */
    public void physicsDelete(Long id);

    /**
     * 物理删除
     * @param idList
     */
    public void physicsDelete(List<Long> idList);

    /**
     * 建索引，并且建mapping
     * @return
     */
    boolean createIndex();

    /**
     * 删除索引
     * @return
     */
    boolean removeIndex();

    /**
     * 数据一般存储于mysql，做索引更新
     * @return
     */
    boolean fillData();

}
