package com.jcloud.admin.controller;

import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.service.ApiProcess;
import com.jcloud.admin.service.impl.DefaultApiProcessChain;
import com.jcloud.admin.service.impl.PrivilegesManager;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.domain.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Api(tags = "权限")
@RestController
@RequestMapping("/privilege")
public class PrivilegesController {

    @Autowired
    private PrivilegesManager privilegesManager;

    @Autowired
    private List<ApiProcess> apiProcesses;

    @ApiOperation(value = "保存权限")
    @RequestMapping(value = "savePrivileges", method = RequestMethod.POST)
    public ResponseData saveRolePrivileges(@RequestBody PrivilegesSaveBean bean) {
        privilegesManager.save(bean);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "获取权限")
    @RequestMapping(value = "getPrivileges", method = RequestMethod.GET)
    public ResponseData<PrivilegesSaveBean> getPrivileges(PrivilegeBean bean) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        responseData.setData(privilegesManager.getPrivileges(bean));
        return responseData;
    }

    /**
     * 获取用户全部权限，用户权限等于 个人授权 + 角色授权
     * 在应用上
     * @return
     */
    @ApiOperation(value = "获取用户所有权限")
    @RequestMapping(value = "getUserAllPrivileges", method = RequestMethod.GET)
    public ResponseData<PrivilegesSaveBean> getUserAllPrivileges(PrivilegeBean bean) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        responseData.setData(privilegesManager.getAllPrivileges(bean));
        return responseData;
    }

    /**
     * 获取open api 接口权限
     * @return
     */
    @ApiOperation(value = "获取api权限")
    @RequestMapping(value = "getOpenApiPrivileges", method = RequestMethod.POST)
    public ApiResult getOpenApiPrivileges(@RequestBody ApiRequest apiRequest) {
        ApiResult apiResult = new ApiResult();
        DefaultApiProcessChain chain = new DefaultApiProcessChain(apiProcesses);
        chain.doProcess(apiRequest, apiResult);
        return apiResult;
    }

}
