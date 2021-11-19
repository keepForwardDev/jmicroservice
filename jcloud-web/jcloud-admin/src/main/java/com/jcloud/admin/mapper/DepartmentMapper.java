package com.jcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.admin.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    String getMaxLevelCode(@Param("length") Integer length, @Param("parentLevelCode") String parentLevelCode);

    @Select(value = "select * from sys_department where  deleted = 0 and level_code like #{levelCode} order by sort asc")
    List<Department> getAllDepartmentByLevelCode(@Param(value = "levelCode") String levelCode);

    @Select(value = "select * from sys_department where  deleted = 0 and parent_id is null")
    List<Department> getRootDepartment();

    @Select(value = "select * from sys_department where uuid = #{deptUuid}")
    Department findByUuid(String deptUuid);

    int insertSelective(Department department);

    @Update(value = "update sys_department set level_code=#{levelCode}, full_name=#{fullName} where id=#{id}")
    int updateLevelCodeAndFullName(Long id, String levelCode, String fullName);
}
