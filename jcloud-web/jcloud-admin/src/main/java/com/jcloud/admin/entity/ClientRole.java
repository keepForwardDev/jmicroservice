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
 * @date 2021/9/8
 */
@Data
@Entity
@Table(name = "sys_client_role")
@TableName("sys_client_role")
@org.hibernate.annotations.Table(appliesTo = "sys_client_role", comment = "应用角色关系")
public class ClientRole extends Model<ClientRole> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用id
     */
    @Column(columnDefinition = "bigint(20) comment '应用ID'")
    private Long clientId;

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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
