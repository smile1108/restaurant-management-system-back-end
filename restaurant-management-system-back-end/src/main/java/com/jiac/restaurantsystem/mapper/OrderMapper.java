package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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

    @Select("select * from order_info where order_id = #{orderId}")
    Order selectOrderById(Integer orderId);

    @Delete("delete from order_info where order_id = #{orderId}")
    void deleteOrder(Integer orderId);

    @Select("select * from order_info where user_email = #{userEmail}")
    List<Order> selectAllOrderByUserEmail(String userEmail);
}
