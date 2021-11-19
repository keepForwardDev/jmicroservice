package com.jcloud.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 * @author jiaxm
 * @date 2021/9/22
 */
@Controller
public class AdminController {

    /**
     * 后台管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/root/admin/index";
    }
}
