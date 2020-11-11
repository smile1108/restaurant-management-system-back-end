package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Admin;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * FileName: AdminMapper
 * Author: Jiac
 * Date: 2020/10/30 8:40
 */
@Repository
public interface AdminMapper {

    @Select("select * from administrator where username = #{username}")
    Admin selectAdminByUsername(String username);
}
