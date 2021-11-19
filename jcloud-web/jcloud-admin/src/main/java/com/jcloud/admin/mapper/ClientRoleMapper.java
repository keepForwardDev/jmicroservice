package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.ClientRole;
import com.jcloud.admin.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/8
 */
@Mapper
public interface ClientRoleMapper extends BaseMapper<ClientRole> {

    @Delete(value = "delete from sys_client_role where client_id=#{clientId}")
    public int deleteByClientId(Long clientId);

    @Select(value = "select * from sys_role where id in (select role_id from sys_client_role where client_id=#{clientId})")
    List<Role> findByClientId(Long clientId);

    @Select(value = "select client_id from sys_client_role where role_id=#{roleId}")
    List<Long> getClientIdByRoleId(Long roleId);
}
