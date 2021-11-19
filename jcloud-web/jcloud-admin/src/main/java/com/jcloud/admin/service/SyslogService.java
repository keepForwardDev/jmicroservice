package com.jcloud.admin.service;

import com.jcloud.admin.entity.SysLog;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.service.CrudListService;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
public interface SyslogService extends CrudListService<SysLog, SysLog> {

    /**
     * 保存api调用日志
     * @param log
     * @return
     */
    ResponseData saveApiLog(SysLog log);

    /**
     * 首页统计
     * @return
     */
    ResponseData index();
}
