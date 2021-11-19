package com.jcloud.admin.service;

import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;

/**
 * api处理链
 * @author jiaxm
 * @date 2021/9/8
 */
public interface ApiProcessChain {

    public void doProcess(ApiRequest apiRequest, ApiResult apiResult);

    public void backToLastProcess(ApiRequest apiRequest, ApiResult apiResult);
}
