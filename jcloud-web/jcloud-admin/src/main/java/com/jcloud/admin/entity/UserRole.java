package com.jcloud.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Data
@Entity
@Table(name = "sys_user_role")
@TableName("sys_user_role")
@org.hibernate.annotations.Table(appliesTo = "sys_user_role", comment = "用户角色关系")
public class UserRole extends Model<UserRole> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @Column(columnDefinition = "bigint(20) comment '用户ID'")
    private Long userId;

    /**
     * 角色id
     */
    @Column(columnDefinition = "bigint(20) comment '角色ID'")
    private Long roleId;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
