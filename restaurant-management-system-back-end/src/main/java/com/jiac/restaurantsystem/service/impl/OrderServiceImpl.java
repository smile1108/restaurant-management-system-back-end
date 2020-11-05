package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.OrderMapper;
import com.jiac.restaurantsystem.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * FileName: OrderServiceImpl
 * Author: Jiac
 * Date: 2020/11/5 15:56
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void addOrder(String foodName, Timestamp takeTime, Integer isPackage, Integer isComplete,
                         Timestamp orderTime, Integer number, Double totalPrice) throws CommonException {
        orderMapper.insertOrder(foodName, takeTime, isPackage, isComplete, orderTime, number, totalPrice);
    }
}
