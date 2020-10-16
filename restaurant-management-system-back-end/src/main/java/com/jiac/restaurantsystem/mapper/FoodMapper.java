package com.jiac.restaurantsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * FileName: FoodMapper
 * Author: Jiac
 * Date: 2020/10/16 11:26
 */
@Repository
public interface FoodMapper {

    @Insert("insert into food (name, price, taste, wicket_id) values (#{name}, #{price}, #{taste}, #{wicketId})")
    void insert(String name, Double price, String taste, Integer wicketId);
}
