package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Window
 * Author: Jiac
 * Date: 2020/10/9 22:29
 */
@Repository
public class Window {
    //窗口id
    private Integer windowId;

    //窗口楼层
    private Integer floor;

    //对应商家id
    private Integer merchantId;

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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
