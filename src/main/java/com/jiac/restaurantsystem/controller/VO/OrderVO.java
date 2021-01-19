package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private String foodId;

    //预取时间
    private Timestamp takeTime;

    //是否打包
    private Integer isPackage;

    //订单是否完成
    private Integer isComplete;

    //下单时间
    private Timestamp orderTime;

    //下单数量
    private Integer number;

    //总价
    private Double totalPrice;

    //用户评价
    private Integer grade;

    private String foodName;

    private String foodImg;

    private Integer wicketNumber;

    private Integer floor;


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public Integer getWicketNumber() {
        return wicketNumber;
    }

    public void setWicketNumber(Integer wicketNumber) {
        this.wicketNumber = wicketNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
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


    public Timestamp getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Timestamp takeTime) {
        this.takeTime = takeTime;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderVO{");
        sb.append("orderId=").append(orderId);
        sb.append(", userEmail='").append(userEmail).append('\'');
        sb.append(", foodId='").append(foodId).append('\'');
        sb.append(", takeTime=").append(takeTime);
        sb.append(", isPackage=").append(isPackage);
        sb.append(", isComplete=").append(isComplete);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", number=").append(number);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", grade=").append(grade);
        sb.append(", foodName='").append(foodName).append('\'');
        sb.append(", foodImg='").append(foodImg).append('\'');
        sb.append(", wicketNumber=").append(wicketNumber);
        sb.append(", floor=").append(floor);
        sb.append('}');
        return sb.toString();
    }
}
