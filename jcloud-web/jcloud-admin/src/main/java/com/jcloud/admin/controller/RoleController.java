package com.jcloud.admin.controller;

import com.jcloud.admin.bean.RoleBean;
import com.jcloud.admin.service.RoleService;
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

@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@Valid RoleBean roleBean, @ApiIgnore BindingResult bindingResult) {
        ResponseData responseResult = ValidatorUtil.formValidateResult(bindingResult);
        return responseResult == null ? roleService.saveRole(roleBean) : responseResult;
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public ResponseData delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(DataBasePage page, RoleBean bean) {
        return roleService.pageList(page, bean);
    }

    @ApiOperation(value = "角色下拉框选择")
    @RequestMapping(value = "/labelNodes", method = RequestMethod.GET)
    public ResponseData labelNodes() {
        ResponseData responseResult = ResponseData.getSuccessInstance();
        responseResult.setData(roleService.getLabelNodes());
        return responseResult;
    }

}
