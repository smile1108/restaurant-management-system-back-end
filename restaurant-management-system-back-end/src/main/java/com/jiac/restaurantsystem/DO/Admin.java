package com.jiac.restaurantsystem.DO;

import org.springframework.stereotype.Repository;

/**
 * FileName: Admin
 * Author: Jiac
 * Date: 2020/10/9 22:25
 */
public class Admin {

    private Integer administratorId;

    private String name;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(Integer administratorId) {
        this.administratorId = administratorId;
    }
}
