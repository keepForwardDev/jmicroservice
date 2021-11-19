package com.jcloud.admin.controller;

import com.jcloud.admin.bean.DepartmentBean;
import com.jcloud.admin.service.DepartmentService;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.ValidatorUtil;
import com.jcloud.orm.domain.DataBasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "部门管理")
@RestController
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存")
    public ResponseData save(@Valid DepartmentBean bean, @ApiIgnore BindingResult bindingResult) {
        ResponseData responseResult = ValidatorUtil.formValidateResult(bindingResult);
        if (responseResult == null) {
            departmentService.saveEntity(bean);
            responseResult = ResponseData.getSuccessInstance();
        }
        return responseResult;
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public ResponseData delete(@PathVariable Long id) {
        departmentService.physicsDelete(id);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "部门列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(DataBasePage page, DepartmentBean bean) {
        return departmentService.pageList(page, bean);
    }

    @ApiOperation(value = "部门树")
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> treeList() {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(departmentService.getDepartmentTree());
        return responseResult;
    }

    @ApiOperation(value = "获取部门详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ResponseData<DepartmentBean> getInfo(@PathVariable Long id) {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(departmentService.getInfo(id));
        return responseResult;
    }

    @ApiIgnore
    @RequestMapping(value = "listDepartment", method = RequestMethod.GET)
    public ResponseData listDepartment() {
        return departmentService.listDepartment();
    }

}
