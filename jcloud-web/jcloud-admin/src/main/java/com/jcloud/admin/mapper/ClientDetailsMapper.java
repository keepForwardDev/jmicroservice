package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.ClientDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
@Mapper
public interface ClientDetailsMapper extends BaseMapper<ClientDetails> {

    @Select(value = "select * from sys_client_details where client_id=#{clientId}")
    ClientDetails findByClientId(String clientId);
}
