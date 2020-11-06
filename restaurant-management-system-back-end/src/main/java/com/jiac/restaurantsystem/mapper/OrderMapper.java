package com.jiac.restaurantsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * FileName: OrderMapper
 * Author: Jiac
 * Date: 2020/11/5 16:00
 */
@Repository
public interface OrderMapper {

    @Insert("insert into order_info (user_email, food_name, take_time, is_package, is_complete, order_time, number, total_price)" +
            "values (#{email}, #{foodName}, #{takeTime}, #{isPackage}, #{isComplete}, #{orderTime}, #{number}," +
            "#{totalPrice})")
    void insertOrder(String email, String foodName, Timestamp takeTime, Integer isPackage, Integer isComplete,
                     Timestamp orderTime, Integer number, Double totalPrice);
}
