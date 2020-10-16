package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.mapper.FoodMapper;
import com.jiac.restaurantsystem.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FileName: FoodServiceImpl
 * Author: Jiac
 * Date: 2020/10/16 11:27
 */
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodMapper foodMapper;

    @Override
    public void insert(String name, Double price, String taste, Integer wicketId) {
        foodMapper.insert(name, price, taste, wicketId);
    }
}
