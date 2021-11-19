package com.jcloud.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcloud.common.bean.UserRouter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Data
@ApiModel(value = "区创用户", description = "区创用户信息")
public class ShiroUser implements Serializable, UserDetails {

    @ApiModelProperty(value = "唯一id")
    private Long id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "所属部门")
    private Long departmentId;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像地址")
    private String avatar;

    /**
     * 账号
     */
    @ApiModelProperty(value = "个人账号")
    private String account;

    /**
     * 名字
     */
    @ApiModelProperty(value = "用户名称")
    private String name;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 用户唯一uuid
     */
    @ApiModelProperty(value = "用户唯一uuid")
    private String uuid;

    /**
     * 角色code
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private List<String> rolesCode = new ArrayList<>();

    @ApiModelProperty(value = "用户类型")
    private Integer type = 0;

    @ApiModelProperty(value = "用户来源")
    private Long sourceFrom;

    @ApiModelProperty(value = "用户菜单")
    private List<UserRouter> menus = new ArrayList<>();

    /**
     * 角色资源code
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Set<String> resourcesCode = new HashSet<>();

    @ApiModelProperty(value = "是否启用")
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRolesCode() {
        return rolesCode;
    }

    public void setRolesCode(List<String> rolesCode) {
        this.rolesCode = rolesCode;
    }

    public Set<String> getResourcesCode() {
        return resourcesCode;
    }

    public void setResourcesCode(Set<String> resourcesCode) {
        this.resourcesCode = resourcesCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> resourcesSet = new HashSet<>();
        // 权限资源
        getResourcesCode().forEach(code -> {
            resourcesSet.add(new SimpleGrantedAuthority(code));
        });
        getRolesCode().forEach(code -> {
            // 角色是一种特殊的资源，必须加上ROLE_
            resourcesSet.add(new SimpleGrantedAuthority("ROLE_" + code));
        });
        return resourcesSet;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getUuid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
