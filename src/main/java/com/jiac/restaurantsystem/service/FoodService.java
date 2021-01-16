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

    List<Food> list(Integer page, Integer size) throws CommonException;

    List<Food> selectFoodsByWindowId(Integer windowId, Integer page, Integer size) throws CommonException;

    List<Food> selectFoodsByTaste(String taste) throws CommonException;

    List<Food> selectFoodsByFloor(Integer floor) throws CommonException;

    boolean judgeFoodIsBelongMerchant(Integer merchantId, Integer wicketId) throws CommonException;

    Integer judgeFoodIsExist(Integer foodId) throws CommonException;

    void updateFood(Integer foodId, String name, Double price, String taste) throws CommonException;

    void deleteFood(Integer foodId) throws CommonException;

    Double selectFoodPriceByFoodName(String foodName) throws CommonException;

}
