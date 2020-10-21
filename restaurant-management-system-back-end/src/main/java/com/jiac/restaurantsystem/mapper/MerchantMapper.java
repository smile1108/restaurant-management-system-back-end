package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.Window;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileName: MerchantMapper
 * Author: Jiac
 * Date: 2020/10/16 8:43
 */
@Repository
public interface MerchantMapper {

    @Select("select * from merchant where email = #{email}")
    Merchant selectByEmail(String email);

    @Select("select * from merchant where merchant_id = #{id}")
    Merchant selectById(String id);

    @Update("update merchant set password = #{newPass} where merchant_id = #{id}")
    void updatePassword(String id, String newPass);

    @Insert("insert into merchant (password, email) values (#{password}, #{email})")
    void insert(String password, String email);
}
