package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Merchant
 * Author: Jiac
 * Date: 2020/10/9 22:30
 */
public class Merchant {
    //商家id
    private String merchantId;

    //商家密码
    private String password;

    //商家邮箱
    private String email;

    //商家昵称
    private String name;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
