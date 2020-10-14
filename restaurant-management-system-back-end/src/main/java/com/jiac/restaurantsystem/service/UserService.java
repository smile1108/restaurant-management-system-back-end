package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.error.CommonException;
import org.springframework.stereotype.Service;

/**
 * FileName: UserService
 * Author: Jiac
 * Date: 2020/10/13 20:05
 */
public interface UserService {

    User login(String id, String password) throws CommonException;

    void modifyPass(String id, String oldPass, String newPass, String modifyPass) throws CommonException;

    void getbackPass(String email, String id) throws CommonException;
}
