package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Window
 * Author: Jiac
 * Date: 2020/10/9 22:29
 */
public class Window {
    //窗口id
    private Integer windowId;

    // 窗口号
    private Integer windowNumber;

    //窗口楼层
    private Integer floor;

    //对应商家id
    private String merchantId;

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

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

    public Integer getWindowNumber() {
        return windowNumber;
    }

    public void setWindowNumber(Integer windowNumber) {
        this.windowNumber = windowNumber;
    }
}
