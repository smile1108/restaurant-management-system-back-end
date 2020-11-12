package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * FileName: UserMapper
 * Author: Jiac
 * Date: 2020/10/13 20:04
 */
@Repository
public interface UserMapper {

    @Select("select * from student where id = #{id}")
    User selectUserById(String id);

    @Select("select * from student where email = #{email}")
    User selectUserByEmail(String email);

    @Update("update student set password = #{newPass} where email = #{email}")
    void updatePassword(String email, String newPass);

    @Insert("insert into student (name, email, password) values (#{name}, #{email}, #{password})")
    void insert(String name, String password, String email);

    @Update("update student set name = #{name}, id = #{id} where email = #{email}")
    void updateMsg(String name, String id, String email);
}
