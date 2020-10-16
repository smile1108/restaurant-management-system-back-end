package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.MerchantMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FileName: MerchantServiceImpl
 * Author: Jiac
 * Date: 2020/10/16 8:42
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Merchant login(String id, String password) throws CommonException {
        // 通过id在数据库中查找数据
        Merchant merchant = merchantMapper.selectById(id);
        // 如果查找到的merchant为null 表示不存在这个merchant
        if(merchant == null){
            LOG.error("MerchantService -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在 然后验证密码是否正确
        if(!password.equals(merchant.getPassword())){
            LOG.error("MerchantService -> 用户名或密码不对");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("MerchantService -> 商家登录成功");
        return merchant;
    }

    @Override
    @Transactional
    public void modifyPass(String id, String oldPass, String newPass, String qualifyPass) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        Merchant merchant = merchantMapper.selectById(id);
        // 如果user等于空 表示用户不存在
        if(merchant == null){
            LOG.error("MerchantService -> 商家不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在
        // 先验证旧密码正确与否
        if(!merchant.getMerchantId().equals(id) || !merchant.getPassword().equals(oldPass)){
            LOG.error("MerchantService -> 用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("MerchantService -> 商家修改密码成功");
        merchantMapper.updatePassword(id, newPass);
    }
}
