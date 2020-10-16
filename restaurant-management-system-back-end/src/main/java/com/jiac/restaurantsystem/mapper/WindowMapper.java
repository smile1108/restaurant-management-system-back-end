package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Window;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * FileName: WindowMapper
 * Author: Jiac
 * Date: 2020/10/16 10:36
 */
@Repository
public interface WindowMapper {

    @Select("select * from wicket where wicket_number = #{windowNumber} and floor = #{floor}")
    Window selectWindowByNumberAndFloor(Integer windowNumber, Integer floor);

    @Insert("insert into wicket (wicket_number, floor, merchant_id) values (#{wicketNumber}, #{floor}, #{merchantId})")
    void insert(Integer wicketNumber, Integer floor, String merchantId);


}
