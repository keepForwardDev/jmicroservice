package com.jcloud.admin.controller;

import com.jcloud.admin.entity.SysLog;
import com.jcloud.admin.service.SyslogService;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.domain.DataBasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@Api(tags = "日志")
@RequestMapping("/log")
@RestController
public class SysLogController {

    @Autowired
    private SyslogService syslogService;

    @ApiOperation(value = "日志列表")
    @RequestMapping(value = "pageList", method = RequestMethod.GET)
    public ResponseData<SysLog> pageList(DataBasePage dataBasePage, SysLog sysLog) {
        return syslogService.pageList(dataBasePage, sysLog);
    }

    @ApiOperation(value = "保存日志")
    @PostMapping(value = "/save")
    public ResponseData save(@RequestBody SysLog log) {
        syslogService.saveEntity(log);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation(value = "保存api日志")
    @PostMapping(value = "/saveApiOrder")
    public ResponseData saveApiOrder(@RequestBody SysLog log) {
        return syslogService.saveApiLog(log);
    }

    @ApiOperation(value = "首页统计")
    @GetMapping("/index")
    public ResponseData index() {
        return syslogService.index();
    }

}
