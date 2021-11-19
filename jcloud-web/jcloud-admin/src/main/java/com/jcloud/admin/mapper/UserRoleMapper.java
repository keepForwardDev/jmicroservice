package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.Role;
import com.jcloud.admin.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select(value = "select * from sys_role where id in (select role_id from sys_user_role where user_id=#{userId})")
    List<Role> findByUserId(Long userId);

    @Select(value = "select user_id from sys_user_role where role_id=#{roleId}")
    List<Long> getUserIdsByRoleId(Long roleId);
}
