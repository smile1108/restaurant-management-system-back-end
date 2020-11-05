package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.error.CommonException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * FileName: OrderService
 * Author: Jiac
 * Date: 2020/11/5 15:56
 */
public interface OrderService {

    void addOrder(String foodName, Timestamp takeTime, Integer isPackage, Integer isComplete
            , Timestamp orderTime, Integer number, Double totalPrice) throws CommonException;
}
