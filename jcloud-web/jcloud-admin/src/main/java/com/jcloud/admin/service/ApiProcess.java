package com.jcloud.admin.service;

import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;

/**
 * api处理
 * @author jiaxm
 * @date 2021/9/8
 */
public interface ApiProcess {

    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain);
}
