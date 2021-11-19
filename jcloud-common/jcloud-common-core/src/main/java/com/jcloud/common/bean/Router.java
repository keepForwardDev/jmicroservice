package com.jcloud.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 路由
 */
@Data
@ApiModel(value = "菜单vue路由", description = "返回给前端做动态路由")
public class Router implements Serializable{

    @ApiModelProperty(value = "菜单id")
    private Long id;

    /**
     * 页面的export_name
     */
    @ApiModelProperty(value = "页面的export_name，vue router在跳转可以用页面的名称，或者path做跳转")
    private String name;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String title;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private boolean hidden = false;


    /**
     * 重定向地址
     */
    @ApiModelProperty(value = "重定向路径")
    private String redirect;

    /**
     * 路由meta
     */
    @ApiModelProperty(value = "路由meta")
    private Meta meta = new Meta();

    @ApiModelProperty(value = "排序号")
    private Integer sort = 0;

    @ApiModelProperty(value = "父级id")
    private Long parentId = 0l;

    @ApiModelProperty(value = "父级名称")
    private String parentName;

    @ApiModelProperty(value = "是否有子菜单")
    private boolean hasChildren = false;

    /**
     * 路由访问地址
     */
    @ApiModelProperty(value = "路由访问地址")
    private String path;

    /**
     * 组件地址
     */
    @ApiModelProperty(value = "组件地址 views下面的路由 以/开头")
    private String component;

    /**
     * 菜单类型，d_simple_dictionary
     */
    @ApiModelProperty(value = "菜单类型-字典ID[name_key=menuType]")
    private Long type = 0l;

    @ApiModelProperty(value = "菜单备注")
    private String remark;

    @ApiModelProperty(value = "子菜单")
    private List<Router> children = new ArrayList<>();

    @Data
    public static class Meta implements Serializable{

        private String title;

        private String icon;

       private Boolean cache;
    }
}
