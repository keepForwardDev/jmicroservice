package com.jcloud.security.config.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/3/25
 */
@Data
public class IgnoreUrlProperties {

    /**
     * 当前项目忽略 urls,这种会进入过滤链，在filter层排除
     */
    private List<String> urls = new ArrayList<>();

    /**
     * 全局忽略 url，这种不会进入认证过滤链，在filter层外就排除了
     * 一般排除一些静态文件之类的
     */
    private List<String> publicUrls = new ArrayList<>();

    public String[] getIgnoreUrls() {
        List<String> tmp = new ArrayList<>();
        tmp.addAll(urls);
        tmp.addAll(publicUrls);
        return tmp.toArray(new String[tmp.size()]);
    }
}
