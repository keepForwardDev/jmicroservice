package com.jcloud.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.admin.bean.IndexStat;
import com.jcloud.admin.entity.SysLog;
import com.jcloud.admin.mapper.ClientDetailsMapper;
import com.jcloud.admin.mapper.DepartmentMapper;
import com.jcloud.admin.mapper.SysLogMapper;
import com.jcloud.admin.mapper.UserMapper;
import com.jcloud.admin.service.LogPersistStrategy;
import com.jcloud.admin.service.SyslogService;
import com.jcloud.common.bean.ApiRequest;
import com.jcloud.common.bean.ApiResult;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.DefaultOrmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@Service
public class SyslogServiceServiceImpl extends DefaultOrmService<SysLogMapper, SysLog, SysLog> implements SyslogService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LogPersistStrategy logPersistStrategy;

    @Autowired
    private ApiPrivilegeChecker apiPrivilegeChecker;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClientDetailsMapper clientDetailsMapper;



    @Override
    public SysLog convert(SysLog sysLog) {
        return sysLog;
    }

    @Override
    public ResponseData saveApiLog(SysLog log) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        String apiKey = log.getContent();
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        String apiLimit = stringRedisTemplate.opsForValue().get(apiKey);
        if (StringUtils.isNotBlank(apiLimit)) {
            Integer apiLimitValue = Integer.valueOf(apiLimit);
            if (apiLimitValue.longValue() <= 0) { // 超出限制
                responseData.setCode(Const.CODE_ERROR);
                responseData.setMsg("接口调用次数超出限制");
            }
            if (apiLimitValue.intValue() == Integer.MAX_VALUE) { // 无限访问
                pass(responseData, log);
            } else { // 删减次数
                Long dec = valueOperations.decrement(apiKey);
                if (dec >= 0) {
                    pass(responseData, log);
                } else { // 防止竞争条件引起的多次调用问题
                    stringRedisTemplate.opsForValue().set(apiKey, "0", Duration.ofHours(6));
                }
            }
        } else { // 假如失效, 重新查询
            // apiLimit:2:data-center:jcloud-admin:/user/checkPhone/{phone}
            ApiRequest apiRequest = new ApiRequest();
            String[] contentArray = log.getContent().split(StringPool.COLON);
            apiRequest.setAppKey(contentArray[2]);
            apiRequest.setApiPath(contentArray[4]);
            apiRequest.setServiceId(contentArray[3]);
            boolean success = apiPrivilegeChecker.process(apiRequest, new ApiResult());
            if (success) {
                saveApiLog(log);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData index() {
        IndexStat indexStat = new IndexStat();
        indexStat.setClientNumber(clientDetailsMapper.selectCount(null));
        indexStat.setUserNumber(userMapper.selectCount(null));
        indexStat.setDepartmentNumber(departmentMapper.selectCount(null));
        indexStat.setCurrentCreateUser(userMapper.currentDayRegUser());
        indexStat.setApiCall(baseMapper.totalApiCall());
        indexStat.setCurrentApiCall(baseMapper.currentDayApiCall());
        indexStat.setCurrentUserLogin(baseMapper.currentLoginNumber());
        return ResponseData.getSuccessInstance(indexStat);
    }


    private void pass(ResponseData responseData, SysLog log) {
        insertCommonInfo(log);
        Long id = logPersistStrategy.persist(log);
        responseData.setCode(Const.CODE_SUCCESS);
        responseData.setMsg(Const.CODE_SUCCESS_STR);
        responseData.setData(id);
    }
}
