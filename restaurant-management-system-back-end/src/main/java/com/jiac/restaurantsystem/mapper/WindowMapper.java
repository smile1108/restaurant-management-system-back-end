package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Window;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    void insert(Integer wicketNumber, Integer floor, Integer merchantId);

    @Select("select * from wicket where merchant_id = #{merchantId}")
    List<Window> selectAllWindowByMerchantId(String merchantId);

    @Select("select * from wicket where floor = #{floor}")
    List<Window> selectAllWindowByFloor(Integer floor);

    @Select("select merchant_id from wicket where wicket_id = #{wicketId}")
    Integer selectMerchantByWicket(Integer wicketId);
}
