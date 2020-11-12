package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;

/**
 * FileName: MerchantVO
 * Author: Jiac
 * Date: 2020/10/16 8:40
 */
public class MerchantVO implements Serializable {

    // 商家id
    private Integer merchantId;

    // 商家邮箱
    private String email;

    // 角色
    private Integer role = 1;

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "MerchantVO{" +
                "merchantId='" + merchantId + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
