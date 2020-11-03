package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Food;
import com.jiac.restaurantsystem.error.CommonException;

import java.util.List;

/**
 * FileName: FoodService
 * Author: Jiac
 * Date: 2020/10/16 11:27
 */
public interface FoodService {

    void insert(String name, Double price, String taste, Integer wicketId) throws CommonException;

    List<Food> list() throws CommonException;

    List<Food> selectFoodsByWindowId(Integer windowId) throws CommonException;

    List<Food> selectFoodsByTaste(String taste) throws CommonException;

    List<Food> selectFoodsByFloor(Integer floor) throws CommonException;
}
