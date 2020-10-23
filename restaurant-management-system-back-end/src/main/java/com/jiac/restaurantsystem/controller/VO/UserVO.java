package com.jiac.restaurantsystem.controller.VO;

/**
 * FileName: UserVO
 * Author: Jiac
 * Date: 2020/10/14 19:59
 */
public class UserVO {
    //学号
    private String id;

    //邮箱
    private String email;

    //姓名
    private String name;

    // 角色
    private Integer role = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "UserVO{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
