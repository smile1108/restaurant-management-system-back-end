package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Admin;
import com.jiac.restaurantsystem.error.CommonException;

/**
 * FileName: AdminService
 * Author: Jiac
 * Date: 2020/10/30 8:35
 */
public interface AdminService {
    // 管理员登录的方法
    Admin login(String name, String password) throws CommonException;
}
