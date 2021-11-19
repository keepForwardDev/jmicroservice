package com.jcloud.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.jcloud.common.bean.TreeBaseBean;
import com.jcloud.common.bean.TreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取树 子父级关系工具,遵循id parentId
 */
public class TreeNodeUtil {

    /**
     * 递归获取树节点
     * 获取所有父节点，循环父节点，传入全部子节点
     *
     * @param parent
     * @param allChild
     * @param labelName
     * @param <T>
     * @return
     * @deprecated 数据量少无所谓，数据量大存在性能问题 #use buildTree
     */
    public static <T> TreeNode getTreeNode(T parent, List<T> allChild, String labelName) {
        TreeNode treeNode = new TreeNode();
        String label = (String) ReflectUtil.getFieldValue(labelName, parent.getClass(), parent);
        Long id = (Long) ReflectUtil.getFieldValue("id", parent.getClass(), parent);
        treeNode.setLabel(label);
        treeNode.setId(id);
        if (allChild != null) {
            allChild.forEach(m -> {
                Long parentId = (Long) ReflectUtil.getFieldValue("parentId", m.getClass(), m);
                if (id.longValue() == parentId.longValue()) {
                    treeNode.getChildren().add(getTreeNode(m, allChild, labelName));
                }
            });
        }
        return treeNode;
    }


    /**
     * @param parent
     * @param allChild
     * @param labelName
     * @param initChild
     * @param <T>
     * @return
     * @deprecated 数据量少无所谓，数据量大存在性能问题 #use buildTree
     */
    public static <T> TreeNode getTreeNode(T parent, List<T> allChild, String labelName, boolean initChild) {
        TreeNode treeNode = new TreeNode(initChild);
        String label = (String) ReflectUtil.getFieldValue(labelName, parent.getClass(), parent);
        Long id = (Long) ReflectUtil.getFieldValue("id", parent.getClass(), parent);
        treeNode.setLabel(label);
        treeNode.setId(id);
        if (allChild != null) {
            allChild.forEach(m -> {
                Long parentId = (Long) ReflectUtil.getFieldValue("parentId", m.getClass(), m);
                if (id.longValue() == parentId.longValue()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(getTreeNode(m, allChild, labelName, initChild));
                }
            });
        }
        return treeNode;
    }


    /**
     * @param parentEntity
     * @param bean
     * @param allChild
     * @param <T>
     * @return
     * @deprecated 数据量少无所谓，数据量大存在性能问题 #use buildTree
     */
    public static <T> TreeBaseBean getTreeNode(T parentEntity, TreeBaseBean bean, List<T> allChild) {
        BeanUtils.copyProperties(parentEntity, bean);
        Long id = (Long) ReflectUtil.getFieldValue("id", parentEntity.getClass(), parentEntity);
        Field field = ReflectionUtils.findField(parentEntity.getClass(), "parentId");
        if (allChild != null) {
            allChild.forEach(m -> {
                Long parentId = (Long) ReflectUtil.getFieldValue(field, m);
                if (id.longValue() == parentId.longValue()) {
                    bean.getChildren().add(getTreeNode(m, ReflectUtil.instanceClazz(bean.getClass()), allChild));
                }
            });
        }
        return bean;
    }

    public static void initChildren(List<TreeNode> nodes) {
        nodes.forEach(r -> {
            if (r.getChildren() == null) {
                r.setChildren(new ArrayList<>());
            } else {
                initChildren(r.getChildren());
            }
        });
    }

    /**
     * 树构建
     *
     * @param dataList
     * @param labelName
     * @param initChildren
     * @param rootId 根节点id，为空时要传0
     * @param <T>
     * @return
     */
    public static <T> List<TreeNode> buildTree(List<T> dataList, String labelName, boolean initChildren, Long rootId) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        if (CollectionUtil.isEmpty(dataList)) {
            return treeNodeList;
        }
        Field idField = ReflectionUtils.findField(dataList.get(0).getClass(), "id");
        Field parentIdField = ReflectionUtils.findField(dataList.get(0).getClass(), "parentId");
        Field labelField = ReflectionUtils.findField(dataList.get(0).getClass(), labelName);
        // 子节点分组
        Map<Long, List<T>> parentMap = dataList.parallelStream().filter(r -> {
            Long value = (Long) ReflectUtil.getFieldValue(parentIdField, r);
            if (value == null) {
                ReflectUtil.setFieldValue(parentIdField, r, rootId);
            }
            return true;
        }).collect(Collectors.groupingBy(r -> {
            Long value = (Long) ReflectUtil.getFieldValue(parentIdField, r);
            return value;
        }));

        // 父级
        List<T> parent = parentMap.get(0l);

        parent.forEach(r -> {
            Long id = (Long) ReflectUtil.getFieldValue(idField, r);
            String name = (String) ReflectUtil.getFieldValue(labelField, r);
            TreeNode node = createTreeNode(r, idField, labelField, initChildren);
            node.setChildren(getChildNode(parentMap, id, idField, labelField, initChildren));
            treeNodeList.add(node);
        });
        return treeNodeList;
    }


    /**
     * 递归获取树
     *
     * @param map 根据parentId 分组
     * @param parentId 父级id
     * @param idField
     * @param labelField
     * @param initChildren
     * @param <T>
     * @return
     */
    private static <T> List<TreeNode> getChildNode(Map<Long, List<T>> map, Long parentId, Field idField, Field labelField, boolean initChildren) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<T> childList = map.get(parentId);
        if (childList == null) {
            return null;
        }
        for (T fc : childList) {
            TreeNode treeNode = createTreeNode(fc, idField, labelField, initChildren);
            List<T> child = map.get(treeNode.getId());
            if (child != null) {
                treeNode.setChildren(getChildNode(map, treeNode.getId(), idField, labelField, initChildren));
            }
            list.add(treeNode);
        }
        return list;
    }

    private static <T> TreeNode createTreeNode(T t, Field idField, Field labelField, boolean initChildren) {
        Long id = (Long) ReflectUtil.getFieldValue(idField, t);
        String name = (String) ReflectUtil.getFieldValue(labelField, t);
        TreeNode node = new TreeNode(initChildren);
        node.setId(id);
        node.setLabel(name);
        return node;
    }

    /**
     * tree转为map
     * @param treeNodes
     * @param treeNodeMap
     */
    public static Map<Object, TreeNode> treeToMap(Collection<TreeNode> treeNodes, Field keyField, Map<Object, TreeNode> treeNodeMap) {
        for (TreeNode treeNode : treeNodes) {
            treeToMap(treeNode, keyField, treeNodeMap);
        }
        return treeNodeMap;
    }


    /**
     * tree转为map
     * @param treeNode
     * @param treeNodeMap
     */
    public static Map<Object, TreeNode> treeToMap(TreeNode treeNode, Field keyField,Map<Object, TreeNode> treeNodeMap) {
        Object key = ReflectUtil.getFieldValue(keyField, treeNode);
        if (key != null) {
            treeNodeMap.put(key, treeNode);
        }
        if (!CollectionUtil.isEmpty(treeNode.getChildren())) {
            treeToMap(treeNode.getChildren(), keyField, treeNodeMap);
        }
        return treeNodeMap;
    }
}
