package com.jcloud.admin.bean;

import com.jcloud.common.bean.BaseEntityBean;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResourceBean extends BaseEntityBean {

    /**
     * 资源编码， 唯一
     */
    @NotBlank(message = "资源编码不能为空！")
    private String code;

    /**
     * 资源链接
     */
    private String url;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空！")
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源展示排序
     */
    private Integer sort=0;

    /**
     * 所属菜单
     */
    private Long menuId;


    private Long parentId = 0l;

    private String parentName;
}
