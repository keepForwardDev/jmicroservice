package com.jcloud.admin.controller;


import com.jcloud.admin.bean.ResourceBean;
import com.jcloud.admin.service.ResourceService;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.ValidatorUtil;
import com.jcloud.orm.domain.DataBasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "资源管理")
@RequestMapping("resource")
@RestController
public class ResourcesController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "保存")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseData save(@Valid ResourceBean bean, BindingResult bindingResult) {
        ResponseData responseResult = ValidatorUtil.formValidateResult(bindingResult);
        return responseResult == null ? resourceService.save(bean) : responseResult;
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('super_admin')")
    public ResponseData delete(@PathVariable Long id) {
        resourceService.physicsDelete(id);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "资源列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseData listPage(DataBasePage page, ResourceBean bean) {
        return resourceService.pageList(page, bean);
    }

    /**
     * lazy load
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "懒加载获取资源")
    @RequestMapping(value = "menuResource/{menuId}", method = RequestMethod.GET)
    public ResponseData<List<LabelNode>> menuResource(@PathVariable Long menuId) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(resourceService.menuResources(menuId));
        return responseResult;
    }

    @ApiOperation(value = "资源树")
    @RequestMapping(value = "treeList", method = RequestMethod.GET)
    public ResponseData menuTree(Long parentId, @ApiParam(value = "初始化子节点,空和0 初始化，1 不初始化") Integer flag) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(resourceService.getTree(parentId, flag));
        return responseResult;
    }


    @ApiOperation(value = "资源详情")
    @RequestMapping(value = "info/{id}", method = RequestMethod.GET)
    public ResponseData info(@PathVariable Long id) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(resourceService.info(id));
        return responseResult;
    }
}
