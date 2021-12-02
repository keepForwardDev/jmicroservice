package com.jcloud.admin.service;

import com.jcloud.admin.bean.LogFilter;
import com.jcloud.admin.entity.AppLog;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.elasticsearch.domain.EsPage;

/**
 * @author jiaxm
 * @date 2021/11/24
 */
public interface AppLogService {

    /**
     * 搜索列表
     * @param pager
     * @param appLog
     * @return
     */
    public ResponseData<AppLog> pageList(EsPage pager, LogFilter appLog);
}
