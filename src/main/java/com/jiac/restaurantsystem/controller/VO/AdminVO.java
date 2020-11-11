package com.jiac.restaurantsystem.controller.VO;

import java.io.Serializable;

/**
 * FileName: AdminVO
 * Author: Jiac
 * Date: 2020/10/30 8:59
 */
public class AdminVO implements Serializable {

    private Integer administratorId;

    private String name;

    // 角色
    private Integer role = 2;

    public Integer getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(Integer administratorId) {
        this.administratorId = administratorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AdminVO{" +
                "administratorId=" + administratorId +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
