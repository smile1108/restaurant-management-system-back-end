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

//    private Integer wicketId;

    //窗口号
    private Integer wicketNumber;

    // 楼层
    private Integer floor;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FoodVO{");
        sb.append("foodId=").append(foodId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", taste='").append(taste).append('\'');
        sb.append(", wicketNumber=").append(wicketNumber);
        sb.append(", floor=").append(floor);
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
