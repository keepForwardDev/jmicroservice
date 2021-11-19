package com.jcloud.admin.service;

import com.jcloud.admin.entity.SysLog;

/**
 *
 * @author jiaxm
 * @date 2021/9/18
 */
public interface LogPersistStrategy {

    /**
     * 持久化日志
     * @param sysLog
     */
    public Long persist(SysLog sysLog);
}
