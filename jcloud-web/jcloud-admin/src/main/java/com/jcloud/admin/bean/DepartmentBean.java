package com.jcloud.admin.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DepartmentBean {

    private Long id;
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空！")
    private String name;

    /**
     * 父级id
     */
    private Long parentId;

    private String parentName;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 部门编码
     */
    private String levelCode;

    /**
     * 排序号
     */
    private Integer sort = 0;

    private String remark;

}
