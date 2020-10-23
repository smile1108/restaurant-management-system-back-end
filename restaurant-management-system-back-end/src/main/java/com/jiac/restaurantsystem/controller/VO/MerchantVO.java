package com.jiac.restaurantsystem.controller.VO;

/**
 * FileName: MerchantVO
 * Author: Jiac
 * Date: 2020/10/16 8:40
 */
public class MerchantVO {

    // 商家id
    private String merchantId;

    // 商家邮箱
    private String email;

    // 角色
    private Integer role = 1;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
