package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Food;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileName: FoodMapper
 * Author: Jiac
 * Date: 2020/10/16 11:26
 */
@Repository
public interface FoodMapper {

    @Insert("insert into food (name, price, taste, wicket_id) values (#{name}, #{price}, #{taste}, #{wicketId})")
    void insert(String name, Double price, String taste, Integer wicketId);

    @Select("select * from food where name = #{name}")
    Food selectFoodByName(String name);

    @Select("select * from food")
    List<Food> selectAllFood();

    @Select("select * from food where wicket_id = #{windowId}")
    List<Food> selectFoodsByWindowId(Integer windowId);

    @Select("select * from food where taste like '%${taste}%'")
    List<Food> selectFoodsByTaste(String taste);

    @Select("select * from food where wicket_id in (select wicket_id from wicket where floor = #{floor})")
    List<Food> selectFoodsByFloor(Integer floor);
}
