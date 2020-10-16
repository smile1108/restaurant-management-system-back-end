package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * FileName: MerchantMapper
 * Author: Jiac
 * Date: 2020/10/16 8:43
 */
@Repository
public interface MerchantMapper {

    @Select("select * from merchant where merchant_id = #{id}")
    Merchant selectById(String id);

    @Update("update merchant set password = #{newPass} where merchant_id = #{id}")
    void updatePassword(String id, String newPass);

    @Insert("insert into merchant values (#{merchantId}, #{name}, #{password}, #{email})")
    void insert(String merchantId, String name, String password, String email);
}
