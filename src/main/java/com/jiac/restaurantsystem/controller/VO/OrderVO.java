package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;
import java.sql.Time;

/**
 * FileName: Order
 * Author: Jiac
 * Date: 2020/10/9 22:31
 */
public class OrderVO implements Serializable {
    //订单id
    private Integer orderId;

    // 用户邮箱
    private String userEmail;

    //订单中菜的id
    private String foodName;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Time getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Time takeTime) {
        this.takeTime = takeTime;
    }

    public Integer getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(Integer isPackage) {
        this.isPackage = isPackage;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
