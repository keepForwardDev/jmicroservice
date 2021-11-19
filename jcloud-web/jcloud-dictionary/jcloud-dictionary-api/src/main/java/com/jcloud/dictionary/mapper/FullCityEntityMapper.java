package com.jcloud.dictionary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcloud.dictionary.entity.FullCityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/4/12
 */
@Mapper
public interface FullCityEntityMapper extends BaseMapper<FullCityEntity> {

    @Select("<script>select name from d_full_city where id in" +
            "        <foreach collection=\"ids\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">"+
            "                #{id}"+
            "           </foreach></script>")
    public List<String> selectByIds(@Param("ids") List<Long> ids);

}
