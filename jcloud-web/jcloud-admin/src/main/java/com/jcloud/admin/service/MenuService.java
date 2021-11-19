package com.jcloud.admin.service;

import com.jcloud.admin.entity.Menu;
import com.jcloud.common.bean.Router;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.bean.UserRouter;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;

import java.util.List;

public interface MenuService extends CrudListService<Menu, Router> {


    /**
     * 获取跟菜单
     * @param page
     * @param title
     * @return
     */
    ResponseData getRootMenuList(CommonPage page, String title);

    /**
     * 异步获取子菜单
     * @param parentId
     * @return
     */
    public List<Router> getMenuListByParentId(Long parentId);

    /**
     * 父节点获取菜单树
     * @param parentId
     * @param flag 初始化子节点,空和0 初始化，1 不初始化
     * @return
     */
    public List<TreeNode> getMenuTree(Long parentId, Integer flag);

    /**
     * 根据id获取菜单详情
     * @param id
     * @return
     */
    public Router getRouterById(Long id);



    /**
     * 根据id获取路由
     * @param idList
     * @return
     */
    List<UserRouter> getRoutersByIds(List<Long> idList);
}
