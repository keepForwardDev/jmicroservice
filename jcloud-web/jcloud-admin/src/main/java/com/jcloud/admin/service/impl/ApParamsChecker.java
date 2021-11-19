package com.jcloud.admin.service.impl;

import com.jcloud.admin.service.ApiProcess;
import com.jcloud.admin.service.ApiProcessChain;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * api参数校验
 * @author jiaxm
 * @date 2021/9/8
 */
@Service
public class ApParamsChecker implements ApiProcess, Ordered {

    @Override
    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain) {
        List<String> error = new ArrayList<>();
        if (StringUtils.isBlank(apiRequest.getAccessToken()) && StringUtils.isBlank(apiRequest.getAppKey())) {
            error.add("accessToken or appKey");
        }
        if (StringUtils.isBlank(apiRequest.getToken()) && StringUtils.isBlank(apiRequest.getAccessToken())) {
            error.add("token");
        }
        if (StringUtils.isBlank(apiRequest.getNonce()) && StringUtils.isBlank(apiRequest.getAccessToken())) {
            error.add("nonce");
        }
        if (StringUtils.isBlank(apiRequest.getTimeStamp()) && StringUtils.isBlank(apiRequest.getAccessToken())) {
            error.add("timeStamp");
        }
        if (StringUtils.isBlank(apiRequest.getApiPath())) {
            error.add("apiPath");
        }
        if (StringUtils.isBlank(apiRequest.getServiceId())) {
            error.add("serviceId");
        }
        if (error.size() > 0) {
            apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
            apiResult.setMsg(ApiResult.codeMap.get(apiResult.getCode()));
        } else {
            chain.doProcess(apiRequest, apiResult);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
