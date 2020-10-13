package com.jiac.restaurantsystem.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * FileName: UserMapper
 * Author: Jiac
 * Date: 2020/10/13 20:04
 */
@Repository
public interface UserMapper {

    @Select("select test from test where id = #{id}")
    String test(int id);
}
