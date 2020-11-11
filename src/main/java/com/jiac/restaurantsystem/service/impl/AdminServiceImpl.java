package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Admin;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.AdminMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FileName: AdminServiceImpl
 * Author: Jiac
 * Date: 2020/10/30 8:36
 */
@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);


    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String name, String password) throws CommonException {
        Admin admin = adminMapper.selectAdminByUsername(name);

        // 如果管理员为空 表示该账号不存在
        if(admin == null){
            LOG.error("adminService -> 管理员不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }

        if(!password.equals(admin.getPassword())){
            LOG.error("adminService -> 用户名或密码不正确");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("adminService -> 管理员登录成功");
        return admin;
    }
}
