package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcloud.admin.bean.ResourceBean;
import com.jcloud.admin.entity.Resource;
import com.jcloud.admin.mapper.ResourceMapper;
import com.jcloud.admin.service.MenuService;
import com.jcloud.admin.service.ResourceService;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.SqlHelper;
import com.jcloud.common.util.TreeNodeUtil;
import com.jcloud.orm.service.DefaultOrmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceServiceImpl extends DefaultOrmService<ResourceMapper, Resource, ResourceBean> implements ResourceService {

    @Autowired
    private MenuService menuService;

    public ResponseData save(ResourceBean bean) {
        ResponseData responseResult = new ResponseData();
        try {
            saveEntity(bean);
            responseResult.setCode(Const.CODE_SUCCESS);
            responseResult.setMsg(Const.CODE_SUCCESS_STR);
        } catch (DuplicateKeyException ex) {
            responseResult.setMsg("该资源已存在，请重新输入！");
        }
        return responseResult;
    }

    @Override
    public ResourceBean convert(Resource resource) {
        ResourceBean bean = new ResourceBean();
        BeanUtils.copyProperties(resource, bean);
        return bean;
    }

    @Override
    public QueryWrapper<Resource> queryCondition(ResourceBean bean) {
        QueryWrapper<Resource> condition = new QueryWrapper<>();
        condition.eq("deleted", 0);
        if (StringUtils.isNotBlank(bean.getName())) {
            condition.apply(SqlHelper.getSqlLike("name", 0), SqlHelper.getSqlLikeParams(bean.getName()));
        }
        if (StringUtils.isNotBlank(bean.getCode())) {
            condition.apply(SqlHelper.getSqlLike("code", 0), SqlHelper.getSqlLikeParams(bean.getCode()));
        }
        if (bean.getMenuId() != null) {
            condition.eq("menu_id", bean.getMenuId());
        }
        return condition;
    }

    /**
     * 全部部门树
     *
     * @return
     */
    public List<TreeNode> getResourceTree() {
        List<TreeNode> nodes = new ArrayList<>();
        QueryWrapper<Resource> condition = new QueryWrapper<>();
        condition.eq("deleted", 0);
        condition.orderByAsc("sort");
        List<Resource> resources = baseMapper.selectList(condition);
        //根节点
        List<Resource> parents = resources.stream().filter(m -> m.getParentId() == null).collect(Collectors.toList());
        //子节点
        List<Resource> child = resources.stream().filter(m -> m.getParentId() != null).collect(Collectors.toList());
        if (parents.isEmpty()) {
            return nodes;
        }
        parents.forEach(t -> {
            nodes.add(TreeNodeUtil.getTreeNode(t, child, "name"));
        });
        return nodes;
    }

    @Override
    public List<LabelNode> menuResources(Long menuId) {
        List<LabelNode> labelNodes = new ArrayList<>();
        QueryWrapper<Resource> condition = super.queryCondition(null);
        condition.eq("menu_id", menuId);
        List<Resource> resourceList = list(condition);
        resourceList.forEach(resource -> {
            labelNodes.add(new LabelNode(resource.getName(), resource.getId()));
        });
        return labelNodes;
    }

    @Override
    public List<TreeNode> getTree(Long parentId, Integer initChild) {
        QueryWrapper condition = new QueryWrapper();
        condition.eq("deleted", 0);
        if (parentId != null) {
            condition.eq("parent_id", parentId);
        }
        condition.orderByAsc("sort", "id");
        List<Resource> resources = list(condition);
        return TreeNodeUtil.buildTree(resources, "name", false, 0l);
    }

    @Override
    public ResourceBean info(Long id) {
        ResourceBean bean = new ResourceBean();
        Resource resource = getById(id);
        if (resource.getParentId().intValue() > 0) {
            Resource parent = getById(resource.getParentId());
            bean.setParentName(parent.getName());
        }
        BeanUtils.copyProperties(resource, bean);
        return bean;
    }

    /**
     * 菜單資源树,按照菜单分组成树
     *
     * @return
     */
    @Deprecated
    public List<TreeNode> menuResources() {
        List<TreeNode> labelNodes = new ArrayList<>();

        QueryWrapper<Resource> condition = super.queryCondition(null);
        List<TreeNode> menuNodes = menuService.getMenuTree(null, 1);
        List<Resource> resourceList = list(condition);
        Map<Long, List<Resource>> resourceGroup = resourceList.stream().collect(Collectors.groupingBy(Resource::getMenuId));
        menuNodes.forEach(menuNode -> {
            TreeNode menuResourceNode = getMenuResource(menuNode, resourceGroup);
            if (!CollectionUtils.isEmpty(menuResourceNode.getChildren())) {
                labelNodes.add(menuResourceNode);
            }
        });
        return labelNodes;
    }

    @Deprecated
    private TreeNode getMenuResource(TreeNode menuNode, Map<Long, List<Resource>> menuResourceMap) {
        List<Resource> resourceList = menuResourceMap.get(menuNode.getId());
        List<TreeNode> childNodes = menuNode.getChildren();
        TreeNode resourceNode = new TreeNode();
        resourceNode = new TreeNode();
        resourceNode.setId(menuNode.getId());
        resourceNode.setLabel(menuNode.getLabel());
        for (TreeNode childNode : childNodes) {
            TreeNode childResource = getMenuResource(childNode, menuResourceMap);
            if (!CollectionUtils.isEmpty(childResource.getChildren())) {
                resourceNode.getChildren().add(childResource);
            }
        }

        if (!CollectionUtils.isEmpty(resourceList)) {
            for (Resource resource : resourceList) {
                TreeNode childResourceNode = new TreeNode();
                childResourceNode.setChildren(null);
                childResourceNode.setLabel(resource.getName());
                // avoid id repeat
                childResourceNode.setId(-resource.getId());
                resourceNode.getChildren().add(childResourceNode);
            }
        }
        return resourceNode;
    }
}
