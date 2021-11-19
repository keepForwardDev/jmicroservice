package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where uuid = #{uuid}")
    User findByUuid(String uuid);

    @Select(value = "select count(id) from sys_user where create_time > date_format(now(), '%Y-%m-%d')")
    Integer currentDayRegUser();
}
