package com.jcloud.admin.bean;

import com.jcloud.common.bean.BaseEntityBean;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RoleBean extends BaseEntityBean implements Serializable {

    @NotBlank(message = "必须填写角色名称！")
    private String name;

    @NotBlank(message = "必须填写角色编码！")
    private String code;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
