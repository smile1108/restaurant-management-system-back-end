package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Food
 * Author: Jiac
 * Date: 2020/10/9 22:27
 */
public class Food {
    //菜id
    private Integer foodId;

    //菜名
    private String name;

    //菜价格
    private Double price;

    //口味
    private String taste;

    //窗口号
    private Integer wicketId;

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

    public Integer getWicketId() {
        return wicketId;
    }

    public void setWicketId(Integer wicketId) {
        this.wicketId = wicketId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Food{");
        sb.append("foodId=").append(foodId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", taste='").append(taste).append('\'');
        sb.append(", wicketId=").append(wicketId);
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
