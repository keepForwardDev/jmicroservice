package com.jcloud.admin.controller;

import com.jcloud.admin.bean.LogFilter;
import com.jcloud.admin.entity.AppLog;
import com.jcloud.admin.service.AppLogService;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.elasticsearch.domain.EsPage;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务日志
 * @author jiaxm
 * @date 2021/11/24
 */
@Api(tags = "服务日志")
@RequestMapping("appLog")
@RestController
public class AppLogController {

    @Autowired
    private AppLogService appLogService;

    @RequestMapping("pageList")
    public ResponseData<AppLog> pageList(EsPage esPage, LogFilter logFilter) {
        return appLogService.pageList(esPage, logFilter);
    }

}
