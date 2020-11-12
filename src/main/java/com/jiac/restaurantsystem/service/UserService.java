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

    User loginById(String id, String password) throws CommonException;

    User loginByEmail(String email, String password) throws CommonException;

    User modifyPass(String email, String oldPass, String newPass, String modifyPass) throws CommonException;

    void getbackPass(String email, String id) throws CommonException;

    String getCode(String email) throws CommonException;

    User register(String name, String password, String email) throws CommonException;

    boolean judgeUserIsExistByEmail(String email) throws CommonException;

    void modifyMsgByEmail(String name, String id, String email) throws CommonException;
}
