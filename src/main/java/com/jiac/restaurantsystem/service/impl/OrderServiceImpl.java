package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Order;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.OrderMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
    public void addOrder(String email, Integer foodId, Timestamp takeTime, Integer isPackage, Integer isComplete,
                         Timestamp orderTime, Integer number, Double totalPrice) throws CommonException {
        orderMapper.insertOrder(email, foodId, takeTime, isPackage, isComplete, orderTime, number, totalPrice);
    }

    @Override
    public boolean judgeOrderBelongToUser(String userEmail, Integer orderId) throws CommonException {
        Order order = orderMapper.selectOrderById(orderId);
        if(!order.getUserEmail().equals(userEmail)){
            // 表示对应订单不属于该用户 没有权限
            LOG.error("OrderServiceImpl -> judgeOrderBelongToUser -> 对应订单不属于该用户");
            return false;
        }
        return true;
    }

    @Override
    public boolean judgeOrderIsExist(Integer orderId) throws CommonException {
        Order order = orderMapper.selectOrderById(orderId);
        if(order == null){
            LOG.error("OrderServiceImpl -> judgeOrderIsExist -> 对应订单不存在");
            return false;
        }
        return true;
    }

    @Override
    public void deleteOrder(Integer orderId) throws CommonException {
        orderMapper.deleteOrder(orderId);
    }

    @Override
    public List<Order> getAllOrderByUserEmail(String userEmail) throws CommonException {
        return orderMapper.selectAllOrderByUserEmail(userEmail);
    }

    @Override
    public List<Order> getAllOrderByMerchantId(Integer merchantId) throws CommonException {
        return orderMapper.selectAllOrderByMerchantId(merchantId);
    }

    @Override
    public void judgeOrderCompleted(Integer orderId) throws CommonException {
        Order order = orderMapper.selectOrderById(orderId);
        if(order == null){
            LOG.error("OrderServiceImpl -> judgeOrderCompleted -> 不存在该订单");
            throw new CommonException(ResultCode.ORDER_IS_NOT_EXIST);
        }
        if(order.getIsComplete().intValue() == 1){
            // 表示对应订单已完成 抛出异常 不能重复完成订单
            LOG.error("OrderServiceImpl -> judgeOrderCompleted -> 对应订单已经完成");
            throw new CommonException(ResultCode.ORDER_HAVE_COMPLETED);
        }
    }

    @Override
    public void completeOrder(Integer orderId) throws CommonException {
        // 直接修改数据库
        orderMapper.updateOrderCompleteByOrderId(orderId);
    }

    @Override
    public boolean judgeOrderIsCompleted(Integer orderId) throws CommonException {
        Order order = orderMapper.selectOrderById(orderId);
        if(order.getIsComplete().intValue() == 0){
            return false;
        }
        return true;
    }

    @Override
    public void gradeOrder(Integer orderId, Integer grade) throws CommonException {
        orderMapper.updateOrderGradeByOrderId(orderId, grade);
    }

    @Override
    public List<Integer> selectAllGradeByFoodId(Integer foodId) throws CommonException {
        return orderMapper.selectAllGradeByFoodId(foodId);
    }

}
