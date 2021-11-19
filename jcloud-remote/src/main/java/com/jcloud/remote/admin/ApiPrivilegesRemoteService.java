package com.jcloud.remote.admin;

import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.consts.ServiceConst;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiaxm
 * @date 2021/9/15
 */
@FeignClient(value = ServiceConst.JCLOUD_ADMIN, contextId = "apiPrivileges")
public interface ApiPrivilegesRemoteService {

    /**
     * 获取api权限
     * @param apiResult
     * @return
     */
    @PostMapping("/privilege/getOpenApiPrivileges")
    public ApiResult getOpenApiPrivileges(@RequestBody ApiRequest apiResult);

}
