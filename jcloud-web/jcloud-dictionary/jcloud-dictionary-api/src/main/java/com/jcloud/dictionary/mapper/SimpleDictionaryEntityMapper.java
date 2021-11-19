package com.jcloud.dictionary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.dictionary.entity.SimpleDictionaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
@Mapper
public interface SimpleDictionaryEntityMapper extends BaseMapper<SimpleDictionaryEntity> {

    @Select(value = "select * from d_simple_dictionary where name_key=#{nameKey}")
    SimpleDictionaryEntity findByNameKey(String nameKey);

    @Select(value = "select * from d_simple_dictionary where parent_id = #{parentId}")
    List<SimpleDictionaryEntity> selectListByParentId(@Param("parentId") Long parentId);
}
