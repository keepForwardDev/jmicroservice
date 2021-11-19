package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author jiaxm
 * @date 2021/9/16
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    @Select(value = "select count(id) from sys_log where type=1 and create_time >= date_format(now(), '%Y-%m-%d')")
    Integer currentDayApiCall();

    @Select(value = "select count(id) from sys_log where type=0 and  create_time >= date_format(now(), '%Y-%m-%d') ")
    Integer currentLoginNumber();

    @Select(value = "select count(id) from sys_log where type=1")
    Integer totalApiCall();

}
