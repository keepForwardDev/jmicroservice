package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.UserPrivileges;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Mapper
public interface UserPrivilegesMapper extends BaseMapper<UserPrivileges> {

    @Delete("<script> delete from sys_user_privileges where user_id=#{userId} and resource_type=#{resourceType} <if test='resourceType==1 and serviceId!=null'>and service_id=#{serviceId}  </if> </script>")
    public int deleteByUserIdAndResourceType(Long userId, Integer resourceType, String serviceId);

    @Select("<script>select * from sys_user_privileges where user_id=#{userId} and resource_type=#{resourceType} <if test='resourceType==1 and serviceId!=null '> and service_id=#{serviceId} </if> </script>")
    public List<UserPrivileges> findByUserIdAndResourceTypeAndServiceId(Long userId, Integer resourceType, String serviceId);

    @Select(value = "select * from sys_user_privileges where user_id=#{userId} and resource_type=1 and api_path=#{apiPath} and service_id=#{serviceId}")
    UserPrivileges findApiPrivileges(Long userId, String apiPath, String serviceId);
}
