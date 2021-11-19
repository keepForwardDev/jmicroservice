package com.jcloud.dictionary.service;

import com.jcloud.common.bean.TreeNode;

import java.util.List;

/**
 * 字典的增加修改 查询 删除操作  基于数据库层面
 * @author jiaxm
 * @date 2021/11/19
 */
public interface DictionaryCrudOperation {

    /**
     * 保存更新 删除字典,删除传递deleted = 0 和id
     * @param dictionaryBase
     */
    public void cudOperation(Object dictionaryBase);

    /**
     * 通过父级id获取字典，达到懒加载效果
     * @param parentId
     * @return
     */
    public List<TreeNode> getByParentId(Long parentId);

}
