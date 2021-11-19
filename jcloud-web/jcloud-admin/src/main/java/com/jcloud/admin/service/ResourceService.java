package com.jcloud.admin.service;

import com.jcloud.admin.bean.ResourceBean;
import com.jcloud.admin.entity.Resource;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;

import java.util.List;

public interface ResourceService extends CrudListService<Resource, ResourceBean> {

    public List<TreeNode> getResourceTree();

    public ResponseData save(ResourceBean bean);

    public List<LabelNode> menuResources(Long menuId);

    /**
     * 资源树
     * @return
     */
    public List<TreeNode> getTree(Long parentId, Integer initChild);

    public ResourceBean info(Long id);

}
