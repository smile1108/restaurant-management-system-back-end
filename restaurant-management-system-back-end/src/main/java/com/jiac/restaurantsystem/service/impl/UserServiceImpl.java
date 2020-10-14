package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.UserMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FileName: UserServiceImpl
 * Author: Jiac
 * Date: 2020/10/13 20:08
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String id, String password) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        User user = userMapper.selectUserById(id);
        // 如果user等于空 表示用户不存在
        if(user == null){
            LOG.error("用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }

        // 用户存在的话 判断密码是否正确
        // 表示用户名或密码错误
        if(!user.getId().equals(id) || !user.getPassword().equals(password)){
            LOG.error("用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("用户登录成功");
        return user;
    }

    @Override
    @Transactional
    public void modifyPass(String id, String oldPass, String newPass, String modifyPass) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        User user = userMapper.selectUserById(id);
        // 如果user等于空 表示用户不存在
        if(user == null){
            LOG.error("用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在
        // 先验证旧密码正确与否
        if(!user.getId().equals(id) || !user.getPassword().equals(oldPass)){
            LOG.error("用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        // 密码加密暂时不写 之后添加
        userMapper.updatePassword(id, newPass);
    }
}
