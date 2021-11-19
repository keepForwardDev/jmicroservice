package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcloud.admin.entity.Menu;
import com.jcloud.admin.mapper.MenuMapper;
import com.jcloud.admin.service.MenuService;
import com.jcloud.common.bean.Router;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.bean.UserRouter;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.BooleanUtil;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.NumberUtil;
import com.jcloud.common.util.SqlHelper;
import com.jcloud.dictionary.api.DictionaryProvider;
import com.jcloud.dictionary.consts.DictionaryConst;
import com.jcloud.orm.domain.DataBasePage;
import com.jcloud.orm.service.DefaultOrmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends DefaultOrmService<MenuMapper, Menu, Router> implements MenuService {

    @Override
    public Menu saveEntity(Router bean) {
        Menu menu;
        if (bean.getId() != null && bean.getId() > 0) {
            // 更新
            menu = getById(bean.getId());
        } else {
            menu = new Menu();
        }
        insertCommonInfo(menu);
        if (bean.getParentId() != 0) {
            Menu parentMenu = getById(bean.getParentId());
            parentMenu.setHasChildren(1);
            updateById(parentMenu);
        }
        convertRouterToMenu(bean, menu);
        Optional.ofNullable(menu).ifPresent(m -> {
            saveOrUpdate(menu);
        });
        return menu;
    }


    private void convertRouterToMenu(Router router, Menu menu) {
        if (menu == null) {
            return;
        }
        BeanUtils.copyProperties(router, menu);
        router.getMeta().setTitle(router.getTitle());
        menu.setHidden(NumberUtil.booleanToInteger(router.isHidden()));
        menu.setMeta(JsonUtils.toJsonString(router.getMeta()));
        menu.setHasChildren(NumberUtil.booleanToInteger(hasChildren(menu)));
    }

    @Override
    public ResponseData getRootMenuList(CommonPage page, String title) {
        QueryWrapper<Menu> condition = new QueryWrapper();
        condition.eq("parent_id", 0);
        if (StringUtils.isNotBlank(title)) {
            condition.apply(SqlHelper.getSqlLike("title", 0), SqlHelper.getSqlLikeParams(title));
        }
        condition.orderByAsc("sort", "create_time");
        Page<Menu> iPage = new Page<>();
        iPage.setSize(page.getPageSize());
        iPage.setCurrent(page.getCurrentPage());
        Page<Menu> pageList = page(iPage, condition);
        List<Menu> menuList = pageList.getRecords();
        List<Router> routers = convert(menuList);
        DataBasePage commonPage = new DataBasePage(pageList, routers);
        ResponseData result = ResponseData.getSuccessInstance();
        result.setData(commonPage);
        return result;
    }

    @Override
    public List<Router> getMenuListByParentId(Long parentId) {

        if (parentId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Menu> condition = new QueryWrapper();
        condition.eq("parent_id", parentId);
        condition.orderByAsc("sort");
        List<Menu> menuList = list(condition);
        List<Router> routers = convert(menuList);
        return routers;
    }

    @Override
    public List<TreeNode> getMenuTree(Long parentId, Integer flag) {

        List<TreeNode> treeNodes = new ArrayList<>();
        QueryWrapper condition = new QueryWrapper();
        condition.eq("deleted", 0);
        if (parentId != null) {
            condition.eq("parent_id", parentId);
        }
        condition.orderByAsc("sort", "id");
        List<Menu> menuList = list(condition);
        // 分为 root 节点 和 子节点
        Map<Boolean, List<Menu>> groupMap = menuList.stream().collect(Collectors.groupingBy(m -> {
            if (m.getParentId() == 0) {
                return true;
            }
            return false;
        }));
        List<Menu> childList = groupMap.get(false);
        if (!CollectionUtils.isEmpty(menuList)) {
            groupMap.get(true).forEach(m -> {
                treeNodes.add(getTreeNode(m, childList, flag));
            });
        }
        return treeNodes;
    }

    @Override
    public void physicsDelete(Long id) {

        Menu menu = getById(id);
        removeById(id);
        if (menu.getParentId() != null && menu.getParentId() > 0) {
            Menu parent = getById(menu.getParentId());
            parent.setHasChildren(NumberUtil.booleanToInteger(hasChildren(parent)));
        }
        // 查询子节点，然后删除
        List<Menu> childList = queryChildren(id);
        List<Long> idList = childList.stream().map(Menu::getId).collect(Collectors.toList());
        removeByIds(idList);
    }

    public boolean hasChildren(Menu menu) {
        if (menu.idNotNull()) {
            QueryWrapper condition = new QueryWrapper();
            condition.eq("parent_id", menu.getId());
            return getOne(condition, false) != null;
        }
        return false;
    }

    /**
     * 递归查询所有子节点
     *
     * @param id
     * @return
     */
    private List<Menu> queryChildren(Long id) {
        List<Menu> menus = new ArrayList<>();
        return queryChildren(id, menus);
    }

    private List<Menu> queryChildren(Long id, List<Menu> menus) {
        QueryWrapper<Menu> condition = new QueryWrapper<>();
        condition.eq("parent_id", id);
        List<Menu> list = baseMapper.selectList(condition);
        if (!list.isEmpty()) {
            list.forEach(r -> {
                queryChildren(r.getId(), menus);
            });
            menus.addAll(list);
        }
        return menus;
    }

    private TreeNode getTreeNode(Menu parent, List<Menu> allChild, Integer flag) {
        TreeNode treeNode = new TreeNode(BooleanUtil.numberToBoolean(flag));
        treeNode.setLabel(parent.getTitle());
        treeNode.setId(parent.getId());
        if (StringUtils.isNotBlank(parent.getMeta())) {
            Router.Meta meta = JsonUtils.readObject(parent.getMeta(), Router.Meta.class);
            treeNode.setExtra(meta.getIcon());
            treeNode.setData(parent.getPath());
        }
        if (allChild != null) {

            allChild.forEach(m -> {
                if (parent.getId().longValue() == m.getParentId().longValue()) {
                    TreeNode treeNode1 = getTreeNode(m, allChild, flag);
                    if (m.getPath().startsWith("/")) {
                        treeNode1.setData(treeNode.getData() + m.getPath());
                    } else {
                        treeNode1.setData(treeNode.getData() + "/" + m.getPath());
                    }
                    treeNode.children().add(treeNode1);
                }
            });
        }
        return treeNode;
    }

    @Override
    public Router getRouterById(Long id) {
        return convert(getById(id));
    }

    @Override
    public Router convert(Menu menu) {
        Router router = new Router();
        BeanUtils.copyProperties(menu, router);
        router.setHasChildren(BooleanUtil.numberToBoolean(menu.getHasChildren()));
        router.setHidden(BooleanUtil.numberToBoolean(menu.getHidden()));
        router.setMeta(JsonUtils.readObject(menu.getMeta(), Router.Meta.class));
        router.getMeta().setTitle(menu.getTitle());
        if (menu.getParentId().intValue() != 0) {
            Menu menu1 = getById(menu.getParentId());
            router.setParentName(menu1.getTitle());
        }
        return router;
    }


    @Override
    public List<UserRouter> getRoutersByIds(List<Long> idList) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", idList);
        queryWrapper.orderByAsc("sort", "id");
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        Map<Long, List<Menu>> menuGroup = menus.stream().collect(Collectors.groupingBy(r -> r.getType()));
        List<UserRouter> userRouters = new ArrayList<>();
        menuGroup.forEach((type, ms) -> {
            UserRouter userRouter = new UserRouter();
            userRouter.setMenuType(type);
            userRouter.setRouters(getRouters(ms));
            userRouter.setName(DictionaryProvider.service(DictionaryConst.SIMPLE_DICTIONARY).getNameById(type));
            userRouters.add(userRouter);
        });
        return userRouters;
    }


    private List<Router> getRouters(List<Menu> menu) {
        List<Router> routers = new ArrayList<>();
        //根节点
        List<Menu> parents = menu.stream().filter(m -> m.getParentId() == 0).collect(Collectors.toList());
        //子节点
        List<Menu> child = menu.stream().filter(m -> m.getParentId() != 0).collect(Collectors.toList());
        if (parents.isEmpty()) {
            return routers;
        }
        parents.forEach(p -> {
            routers.add(getRouter(p, child));
        });
        return routers;
    }

    /**
     * 递归得到所有路由
     *
     * @param parent   父级节点
     * @param allChild 全部子节点
     * @return
     */
    public Router getRouter(Menu parent, List<Menu> allChild) {
        Router router = convert(parent);
        allChild.forEach(m -> {
            if (parent.getId().longValue() == m.getParentId().longValue()) {
                router.getChildren().add(getRouter(m, allChild));
            }
        });
        return router;
    }
}
