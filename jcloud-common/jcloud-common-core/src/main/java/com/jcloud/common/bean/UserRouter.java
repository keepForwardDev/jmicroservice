package com.jcloud.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户路由
 * @author jiaxm
 * @date 2021/11/12
 */
@Data
public class UserRouter implements Serializable {

    /**
     * 菜单对应的字典类型id
     */
    private Long menuType;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由
     */
    private List<Router> routers;

}
