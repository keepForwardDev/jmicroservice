package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.RolePrivileges;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Mapper
public interface RolePrivilegesMapper extends BaseMapper<RolePrivileges> {

    /**
     * 删除
     * @param roleId
     * @param resourceType
     * @return
     */
    @Delete("<script>delete from sys_role_privileges where role_id=#{roleId} and resource_type=${resourceType} <if test='resourceType=1 and serviceId!=null and serviceId.length() gt 0'>and service_id=#{serviceId}  </if> </script>")
    public int deleteRolePrivileges(@Param(value = "roleId") Long roleId, @Param(value = "resourceType") int resourceType, @Param(value = "serviceId") String serviceId);

    @Select(" <script>select * from sys_role_privileges where role_id=#{roleId} and resource_type=${resourceType} <if test='resourceType=1 and serviceId!=null and  serviceId.length() gt 0  '>and service_id=#{serviceId}  </if> </script>")
    List<RolePrivileges> findByRoleIdAndResType(Long roleId, int resourceType, String serviceId);
}
