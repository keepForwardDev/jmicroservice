package com.jcloud.remote.admin;

import com.jcloud.common.consts.ServiceConst;
import com.jcloud.common.domain.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@FeignClient(value = ServiceConst.JCLOUD_ADMIN, contextId = "sysLog")
public interface LogRemoteService {

    /**
     * 保存api日志
     * @param log
     * @return
     */
    @PostMapping(value = "/log/saveApiOrder")
    public ResponseData saveApiOrder(Map<String, Object> log);

    /**
     * 保存日志
     * @param log
     * @return
     */
    @PostMapping(value = "/log/save")
    public ResponseData save(Map<String, Object> log) ;
}
