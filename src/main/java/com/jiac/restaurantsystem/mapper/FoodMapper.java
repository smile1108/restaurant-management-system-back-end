package com.jiac.restaurantsystem.mapper;

import com.jiac.restaurantsystem.DO.Food;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Select("select * from food limit #{page}, #{size}")
    List<Food> selectAllFood(Integer page, Integer size);

    @Select("select * from food where wicket_id = #{windowId}")
    List<Food> selectFoodsByWindowId(Integer windowId);

    @Select("select * from food where taste like '%${taste}%'")
    List<Food> selectFoodsByTaste(String taste);

    @Select("select * from food where wicket_id in (select wicket_id from wicket where floor = #{floor})")
    List<Food> selectFoodsByFloor(Integer floor);

    @Select("select wicket_id from food where food_id = #{foodId}")
    Integer selectWicketIdByFoodId(Integer foodId);

    @Update("update food set name = #{name}, price = #{price}, taste = #{taste} where food_id = #{foodId}")
    void updateFood(Integer foodId, String name, Double price, String taste);

    @Delete("delete from food where food_id = #{foodId}")
    void deleteFoodByFoodId(Integer foodId);

}
