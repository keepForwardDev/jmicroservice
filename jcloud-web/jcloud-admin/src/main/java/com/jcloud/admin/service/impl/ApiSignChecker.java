package com.jcloud.admin.service.impl;

import com.jcloud.admin.entity.ClientDetails;
import com.jcloud.admin.service.ApiProcess;
import com.jcloud.admin.service.ApiProcessChain;
import com.jcloud.admin.service.ClientDetailsService;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Date;

/**
 * api签名验证
 * @author jiaxm
 * @date 2021/9/8
 */
@Service
@Slf4j
public class ApiSignChecker implements ApiProcess, Ordered {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${system.enableApiRecall:false}")
    private Boolean enableApiRecall;

    @Override
    public void process(ApiRequest apiRequest, ApiResult apiResult, ApiProcessChain chain) {
        long now = new Date().getTime();
        if (StringUtils.isNotBlank(apiRequest.getAppKey())) {
            ClientDetails clientDetails = clientDetailsService.getClientDetails(apiRequest.getAppKey());
            if (clientDetails == null) {
                apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
                apiResult.setMsg("appKey not found");
                return;
            }
            String secret = clientDetails.getClientSecret();
            // 时间戳校验
            long requestTime = Long.valueOf(apiRequest.getTimeStamp(), 16);
            long diff = now - requestTime;
            // 允许前后偏差5s
            if (requestTime > now && Math.abs(diff) > 5000) {
                apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
                apiResult.setMsg("check your system timeStamp");
                return;
            }
            if (diff > 5000) {
                apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
                apiResult.setMsg("sign is expired");
                return;
            }
            String text = apiRequest.getAppKey() + secret + apiRequest.getTimeStamp() + apiRequest.getNonce();
            String result = md5(text.toUpperCase());
            if (!StringUtils.equals(apiRequest.getToken(), result)) {
                apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
                apiResult.setMsg("token is wrong");
                return;
            }
            if (!enableApiRecall) {
                boolean setSuccess = stringRedisTemplate.opsForValue().setIfAbsent(result, apiRequest.getNonce(), Duration.ofSeconds(30));
                if (!setSuccess) {
                    apiResult.setCode(ApiResult.API_PARAMS_NOT_CORRECT);
                    apiResult.setMsg("api not allow recall");
                    return;
                }
            }
            chain.doProcess(apiRequest, apiResult);
        }
    }

    private String md5(String text) {
        try {
            String md5 = DigestUtils.md5DigestAsHex(text.getBytes("UTF-8"));
            return md5;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
