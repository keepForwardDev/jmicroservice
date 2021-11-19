package com.jcloud.dictionary.service;

import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字典公共获取
 * @author jiaxm
 * @date 2021/4/13
 */
public interface DictionaryCommon<T> {

    /**
     * 从缓存拿到全部
     * @return
     */
    public Collection<T> redisToList();

    /**
     * 根据id获取
     * @param id
     * @return
     */
    public T getById(Long id);

    /**
     * 批量获取,降低rtt
     * @param ids
     * @return
     */
    public List<T> getByIds(List<Long> ids);

    /**
     * 根据id获取名称
     * @param id
     * @return
     */
    public String getNameById(Long id);


    /**
     * 批量获取名称
     * @param ids
     * @return
     */
    public List<String> getNameByIds(List<Long> ids);

    /**
     * 批量获取名称map
     * @param ids
     * @return
     */
    public Map<Long, String> getNameMap(Set<Long> ids);

    /**
     * 根据名称获取字典, 目前只有simpleDictionary，支持，需要保证名称唯一
     * @param
     * @param name
     * @return
     */
    public T getByName(String nameKey, String name);

    /**
     * 获取下拉选项数据，nameKey分类
     * 目前只有simpleDictionary，支持
     * @param nameKey
     * @return
     */
    public List<LabelNode> getSelectListByNameKey(String nameKey);

    /**
     * 获取ui树
     * @return
     */
    public List<TreeNode> getTreeNode();

    /**
     * 根据nameKey获取ui树，目前只有simpleDictionary 支持
     * @param nameKey
     * @return
     */
    public List<TreeNode> getTreeNodeByNameKey(String nameKey);

}
