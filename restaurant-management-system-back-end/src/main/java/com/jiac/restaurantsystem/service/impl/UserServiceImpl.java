package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.mapper.UserMapper;
import com.jiac.restaurantsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FileName: UserServiceImpl
 * Author: Jiac
 * Date: 2020/10/13 20:08
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public String test(int id) {
        LOGGER.info("test api , parameter id" + id);
        String test = userMapper.test(id);
        return test;
    }
}
