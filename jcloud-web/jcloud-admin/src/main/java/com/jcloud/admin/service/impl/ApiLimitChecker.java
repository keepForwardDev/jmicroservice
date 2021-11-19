package com.jcloud.admin.service.impl;

import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.admin.service.ApiProcess;
import com.jcloud.admin.service.ApiProcessChain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * api次数校验
 * @author jiaxm
 * @date 2021/9/8
 */
@Service
public class ApiLimitChecker implements ApiProcess, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain) {
        String number = stringRedisTemplate.opsForValue().get(apiResult.getOrderNumber());
        if (StringUtils.isBlank(number)) {        // 假如key失效
            chain.backToLastProcess(apiRequest, apiResult);
        } else {
            Long limit = Long.valueOf(number);
            if (limit.longValue() <= 0) {
                apiResult.setCode(ApiResult.API_OUT_OF_BOUNDS);
                apiResult.setMsg(ApiResult.codeMap.get(ApiResult.API_OUT_OF_BOUNDS));
                apiResult.setOrderNumber(null);
            } else {
                apiResult.setCode(ApiResult.API_RESULT_SUCCESS);
                apiResult.setMsg(ApiResult.codeMap.get(ApiResult.API_RESULT_SUCCESS));
                chain.doProcess(apiRequest, apiResult);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
