package com.jcloud.remote.admin;

import com.jcloud.common.consts.ServiceConst;
import com.jcloud.common.domain.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 客户端
 * @author jiaxm
 * @date 2021/4/21
 */
@FeignClient(value = ServiceConst.JCLOUD_ADMIN, contextId = "clientDetailsService")
public interface ClientDetailsRemoteService {

    /**
     * 根据clientId获取
     * @param clientId
     * @return
     */
    @RequestMapping("/clientDetails/getClientDetails/{clientId}")
    ResponseData getClientDetails(@PathVariable String clientId);

}

