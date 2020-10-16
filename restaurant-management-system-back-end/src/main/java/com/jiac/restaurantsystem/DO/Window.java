package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Window
 * Author: Jiac
 * Date: 2020/10/9 22:29
 */
public class Window {
    //窗口id
    private Integer wicketId;

    // 窗口号
    private Integer wicketNumber;

    //窗口楼层
    private Integer floor;

    //对应商家id
    private String merchantId;

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getWicketId() {
        return wicketId;
    }

    public void setWicketId(Integer wicketId) {
        this.wicketId = wicketId;
    }

    public Integer getWicketNumber() {
        return wicketNumber;
    }

    public void setWicketNumber(Integer wicketNumber) {
        this.wicketNumber = wicketNumber;
    }
}
