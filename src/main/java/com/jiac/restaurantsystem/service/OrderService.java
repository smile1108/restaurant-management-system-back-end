package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Order;
import com.jiac.restaurantsystem.error.CommonException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * FileName: OrderService
 * Author: Jiac
 * Date: 2020/11/5 15:56
 */
public interface OrderService {

    void addOrder(String email, Integer foodId, Timestamp takeTime, Integer isPackage, Integer isComplete
            , Timestamp orderTime, Integer number, Double totalPrice) throws CommonException;

    boolean judgeOrderBelongToUser(String userEmail, Integer orderId) throws CommonException;

    boolean judgeOrderIsExist(Integer orderId) throws CommonException;

    void deleteOrder(Integer orderId) throws CommonException;

    List<Order> getAllOrderByUserEmail(String userEmail) throws CommonException;

    List<Order> getAllOrderByMerchantId(Integer merchantId) throws CommonException;

    void judgeOrderCompleted(Integer orderId) throws CommonException;

    void completeOrder(Integer orderId) throws CommonException;

    boolean judgeOrderIsCompleted(Integer orderId) throws CommonException;

    void gradeOrder(Integer orderId, Integer grade) throws CommonException;

    List<Integer> selectAllGradeByFoodId(Integer foodId) throws CommonException;

    Order selectOrderById(Integer orderId) throws CommonException;
}
