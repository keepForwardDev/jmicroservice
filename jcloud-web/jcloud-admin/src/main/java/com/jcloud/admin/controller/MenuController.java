package com.jcloud.admin.controller;

import com.jcloud.admin.service.MenuService;
import com.jcloud.common.bean.Router;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.jcloud.common.util.ValidatorUtil.formValidateResult;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("menu")
public class MenuController {


    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "菜单列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseData<CommonPage<Router>> menuList(CommonPage page, String title) {
        return menuService.getRootMenuList(page, title);
    }

    @ApiOperation(value = "子菜单")
    @RequestMapping(value = "childMenu/{parentId}", method = RequestMethod.GET)
    public ResponseData<Router> childMenu(@PathVariable(value = "parentId") Long parentId) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(menuService.getMenuListByParentId(parentId));
        return responseResult;
    }

    @ApiOperation(value = "保存")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public ResponseData saveMenu(@RequestBody @Valid Router router, @ApiIgnore BindingResult result) {
        ResponseData responseResult = formValidateResult(result);
        if (responseResult == null) {
            menuService.saveEntity(router);
            return ResponseData.getSuccessInstance();
        }
        return responseResult;
    }

    @ApiOperation(value = "菜单树")
    @RequestMapping(value = "treeList", method = RequestMethod.GET)
    public ResponseData menuTree(Long parentId, @ApiParam(value = "初始化子节点,空和0 初始化，1 不初始化") Integer flag) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(menuService.getMenuTree(parentId, flag));
        return responseResult;
    }

    @ApiOperation(value = "菜单删除")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public ResponseData delete(@PathVariable(value = "id") Long id) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        menuService.physicsDelete(id);
        return responseResult;
    }

    @ApiOperation(value = "单个菜单详情")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET)
    public ResponseData<Router> menuInfo(@PathVariable Long id) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(menuService.getRouterById(id));
        return responseResult;
    }

}
