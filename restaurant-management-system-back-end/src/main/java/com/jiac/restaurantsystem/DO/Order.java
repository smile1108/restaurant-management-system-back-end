package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

import java.sql.Time;

/**
 * FileName: Order
 * Author: Jiac
 * Date: 2020/10/9 22:31
 */
@Repository
public class Order {
    //订单id
    private String orderId;

    //订单中菜的id
    private Integer foodId;

    //预取时间
    private Time takeTime;

    //是否打包
    private Integer isPackage;

    //订单是否完成
    private Integer isComplete;

    //下单时间
    private Time orderTime;

    //下单数量
    private Integer number;

    //总价
    private Double totalPrice;

    //用户评价
    private Integer grade;
}
