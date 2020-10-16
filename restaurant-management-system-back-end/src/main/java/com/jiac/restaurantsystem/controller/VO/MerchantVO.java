package com.jiac.restaurantsystem.controller.VO;

/**
 * FileName: MerchantVO
 * Author: Jiac
 * Date: 2020/10/16 8:40
 */
public class MerchantVO {

    // 商家id
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    // 商家名称
    private String name;

    // 商家邮箱
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
