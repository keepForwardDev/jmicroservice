package com.jcloud.dictionary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.util.BooleanUtil;
import com.jcloud.dictionary.consts.DictionaryConst;
import com.jcloud.dictionary.entity.SimpleDictionaryEntity;
import com.jcloud.dictionary.mapper.SimpleDictionaryEntityMapper;
import com.jcloud.dictionary.service.DictionaryCrudOperation;
import com.jcloud.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典
 *
 * @author jiaxm
 * @date 2021/4/12
 */
@Service(value = DictionaryConst.SIMPLE_DICTIONARY)
public class SimpleDictionary extends AbstractDictionaryService<SimpleDictionaryEntity> implements DictionaryCrudOperation {

    @Autowired
    private SimpleDictionaryEntityMapper simpleDictionaryEntityMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public List<SimpleDictionaryEntity> getData() {
        QueryWrapper<SimpleDictionary> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort_num", "id");
        return simpleDictionaryEntityMapper.selectList(null);
    }

    @Override
    public String getDictionaryKey() {
        return DictionaryConst.SIMPLE_DICTIONARY;
    }

    @Override
    protected void dataToRedis(List<SimpleDictionaryEntity> list) {
        super.dataToRedis(list);
        Map<Long, List<SimpleDictionaryEntity>> parentGroupMap = list.stream().collect(Collectors.groupingBy(r -> r.getParentId()));
        // 获取父级
        List<SimpleDictionaryEntity> parentList = parentGroupMap.get(0l);
        // 存储name id
        parentGroupMap.forEach((k, v) -> {
            if (k.longValue() != 0) {
                SimpleDictionaryEntity child = parentList.stream().filter(r -> r.getId().longValue() == k.longValue()).findFirst().orElse(null);
                if (child == null) { // 只会添加1 - 2 级，多级的使用树形结构
                    return;
                }
                BoundHashOperations operations = dictionaryRedisTemplate.boundHashOps(DictionaryConst.SIMPLE_DICTIONARY + child.getNameKey());
                for (SimpleDictionaryEntity simpleDictionaryEntity : v) {
                    operations.put(simpleDictionaryEntity.getName(), simpleDictionaryEntity);
                }
            }
        });
    }

    @Override
    public SimpleDictionaryEntity getByName(String nameKey, String name) {

        return (SimpleDictionaryEntity) dictionaryRedisTemplate.opsForHash().get(DictionaryConst.SIMPLE_DICTIONARY + nameKey, name);
    }

    @Override
    public List<LabelNode> getSelectListByNameKey(String nameKey) {
        List<LabelNode> list = new ArrayList<>();
        Map<Object, Object> map = dictionaryRedisTemplate.boundHashOps(getDictionaryKey() + nameKey).entries();
        Optional.ofNullable(map).ifPresent(r -> {
            r.forEach((k, v) -> {
                LabelNode labelNode = new LabelNode();
                SimpleDictionaryEntity t = (SimpleDictionaryEntity) v;
                labelNode.setName(t.getName());
                labelNode.setValue(t.getId());
                list.add(labelNode);
            });
        });
        return list;
    }

    @Override
    public List<TreeNode> getTreeNodeByNameKey(String nameKey) {
        List<TreeNode> resultList = new ArrayList<>();
        List<TreeNode> treeNodeList = getTreeNode();
        SimpleDictionaryEntity simpleDictionary = simpleDictionaryEntityMapper.findByNameKey(nameKey);
        Optional.ofNullable(simpleDictionary).ifPresent(r -> {
            TreeNode treeNode = treeNodeList.stream().filter(o -> r.getId().longValue() == o.getId().longValue()).findFirst().orElse(null);
            resultList.addAll(treeNode.getChildren());
        });
        return resultList;
    }

    @Override
    public void cudOperation(Object dictionaryBase) {
        SimpleDictionaryEntity simpleDictionaryEntity = (SimpleDictionaryEntity) dictionaryBase;
        if (BooleanUtil.numberToBoolean(simpleDictionaryEntity.getDeleted())) { // 删除
            deleteSimpleDictionary(simpleDictionaryEntity.getId());
        } else {
            if (simpleDictionaryEntity.getId() != null) {
                SimpleDictionaryEntity entity = simpleDictionaryEntityMapper.selectById(simpleDictionaryEntity.getId());
                entity.setNameKey(simpleDictionaryEntity.getNameKey());
                entity.setName(simpleDictionaryEntity.getName());
                entity.setRemark(simpleDictionaryEntity.getRemark());
                entity.setSortNum(simpleDictionaryEntity.getSortNum());
                entity.setUpdateTime(new Date());
                simpleDictionaryEntityMapper.updateById(entity);
            } else {
                simpleDictionaryEntity.setCreateTime(new Date());
                simpleDictionaryEntity.setCreateUserId(SecurityUtil.getCurrentUser().getId());
                simpleDictionaryEntityMapper.insert(simpleDictionaryEntity);
            }
        }
        threadPoolTaskExecutor.execute(() -> {
            //更新缓存
            dataToRedis();
        });
    }

    @Override
    public List<TreeNode> getByParentId(Long parentId) {
        QueryWrapper<SimpleDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.orderByAsc("sort_num", "id");
        List<SimpleDictionaryEntity> list = simpleDictionaryEntityMapper.selectList(queryWrapper);
        List<TreeNode> treeNodes = new ArrayList<>();
        for (SimpleDictionaryEntity simpleDictionaryEntity : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(simpleDictionaryEntity.getId());
            treeNode.setLabel(simpleDictionaryEntity.getName());
            treeNode.setData(simpleDictionaryEntity);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    private void deleteSimpleDictionary(Long id) {
        simpleDictionaryEntityMapper.deleteById(id);
        QueryWrapper<SimpleDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<SimpleDictionaryEntity> list = simpleDictionaryEntityMapper.selectList(queryWrapper);
        if (!list.isEmpty()) {
            for (SimpleDictionaryEntity simpleDictionaryEntity : list) {
                deleteSimpleDictionary(simpleDictionaryEntity.getId());
            }
        }

    }
}
