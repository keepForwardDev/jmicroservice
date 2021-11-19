package com.jcloud.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxm
 * @date 2021/3/24
 */
@RestController
public class Test {

    @RequestMapping("hi")
    public String hi() {
        return "hello world";
    }
}
