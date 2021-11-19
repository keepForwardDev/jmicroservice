package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.admin.bean.PrivilegeBean;
import com.jcloud.admin.bean.PrivilegesSaveBean;
import com.jcloud.admin.consts.PrivilegesType;
import com.jcloud.admin.consts.ResType;
import com.jcloud.admin.entity.ClientDetails;
import com.jcloud.admin.service.ApiProcess;
import com.jcloud.admin.service.ApiProcessChain;
import com.jcloud.admin.service.ClientDetailsService;
import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.time.Duration;
import java.util.List;

/**
 * api权限校验
 * @author jiaxm
 * @date 2021/9/8
 */
@Service
public class ApiPrivilegeChecker implements ApiProcess, Ordered {

    @Autowired
    private PrivilegesManager privilegesManager;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain) {
        boolean processSuccess = process(apiRequest, apiResult);
        if (processSuccess) {
            chain.doProcess(apiRequest, apiResult);
        }
    }

    public boolean process(ApiRequest apiRequest, ApiResult apiResult) {
        String key = null;
        PrivilegeBean privilegeBean = new PrivilegeBean();
        if (StringUtils.isNotBlank(apiRequest.getAppKey())) {
            ClientDetails clientDetails = clientDetailsService.getClientDetails(apiRequest.getAppKey());
            privilegeBean.setPrivilegeType(PrivilegesType.CLIENT);
            privilegeBean.setId(clientDetails.getId());
            key = apiRequest.getAppKey();
        } else {
            privilegeBean.setPrivilegeType(PrivilegesType.USER);
            key = StringUtils.isNotBlank(apiRequest.getAppKey()) ? apiRequest.getAppKey() : SecurityUtil.getCurrentUser().getPhone();
        }
        privilegeBean.setServiceId(apiRequest.getServiceId());
        privilegeBean.setResourceType(ResType.API);
        // 缓存redis key
        String apiKey = privilegeBean.getPrivilegeType() + StringPool.COLON + key + StringPool.COLON +  apiRequest.getServiceId();
        // 获取该app service 下权限
        PrivilegesSaveBean saveBean = privilegesManager.getAllPrivileges(apiKey, privilegeBean);
        ApiLimit apiLimit = getApiLimit( saveBean.getApiConfig(), apiRequest.getApiPath());
        if (apiLimit == null) { // 无权限
            apiResult.setCode(ApiResult.API_NO_PERMISSION);
            apiResult.setMsg(ApiResult.codeMap.get(apiResult.getCode()));
            return false;
        }
        String apiLimitKey = "apiLimit:" + apiKey + StringPool.COLON + apiLimit.getApiPath();
        stringRedisTemplate.opsForValue().setIfAbsent(apiLimitKey, apiLimit.getApiLimit() + StringUtils.EMPTY, Duration.ofHours(6));
        apiResult.setOrderNumber(apiLimitKey);
        return true;
    }


    private ApiLimit getApiLimit(List<ApiLimit> apiLimitList, String requestPath) {
        for (ApiLimit apiLimit : apiLimitList) {
            if (antPathMatcher.match(apiLimit.getApiPath(), requestPath)) {
                return apiLimit;
            }
        }
        return null;
    }


    @Override
    public int getOrder() {
        return 150;
    }
}
