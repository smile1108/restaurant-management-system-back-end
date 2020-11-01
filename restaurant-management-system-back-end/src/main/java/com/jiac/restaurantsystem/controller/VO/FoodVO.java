package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;

/**
 * FileName: FoodVO
 * Author: Jiac
 * Date: 2020/10/16 10:29
 */
public class FoodVO implements Serializable {
    //菜名
    private String name;

    //菜价格
    private Double price;

    //口味
    private String taste;

    //窗口号
    private Integer windowId;

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

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }
}
