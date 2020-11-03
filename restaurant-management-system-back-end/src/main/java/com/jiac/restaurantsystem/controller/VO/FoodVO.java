package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;

/**
 * FileName: FoodVO
 * Author: Jiac
 * Date: 2020/10/16 10:29
 */
public class FoodVO implements Serializable {

    private Integer foodId;
    //菜名
    private String name;

    //菜价格
    private Double price;

    //口味
    private String taste;

    //窗口号
    private Integer wicketId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public Integer getWicketId() {
        return wicketId;
    }

    public void setWicketId(Integer wicketId) {
        this.wicketId = wicketId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }
}
