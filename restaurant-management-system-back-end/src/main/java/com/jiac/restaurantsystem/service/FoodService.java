package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.error.CommonException;

/**
 * FileName: FoodService
 * Author: Jiac
 * Date: 2020/10/16 11:27
 */
public interface FoodService {

    void insert(String name, Double price, String taste, Integer wicketId) throws CommonException;
}
