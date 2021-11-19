package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.ClientApiPrivileges;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/2
 */
@Mapper
public interface ClientPrivilegesMapper extends BaseMapper<ClientApiPrivileges> {

    @Delete(value = "delete from sys_client_api_privileges where client_id=#{clientId}")
    public int deleteByClientId(Long clientId);

    @Select(value = "<script> select * from sys_client_api_privileges where client_id=#{clientId} <if test='serviceId!=null'> and service_id=#{serviceId}</if> </script>")
    public List<ClientApiPrivileges> findByClientIdAndServiceId(Long clientId, String serviceId);

    @Select(value = "select * from sys_client_api_privileges where client_id=#{clientId} and api_path=#{apiPath} and service_id=#{serviceId}")
    public ClientApiPrivileges findApiPrivileges(Long clientId, String apiPath, String serviceId);
}
