package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.UserMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.SendMail;
import com.jiac.restaurantsystem.service.UserService;
import com.jiac.restaurantsystem.utils.SHA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    @Autowired
    private SendMail mailService;

    @Override
    public User login(String id, String password) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        User user = userMapper.selectUserById(id);
        // 如果user等于空 表示用户不存在
        if(user == null){
            LOG.error("UserService -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }

        // 用户存在的话 判断密码是否正确
        // 表示用户名或密码错误
        if(!user.getId().equals(id) || !user.getPassword().equals(SHA.getResult(password))){
            LOG.error("UserService -> 用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("UserService -> 用户登录成功");
        return user;
    }

    @Override
    @Transactional
    public void modifyPass(String id, String oldPass, String newPass, String modifyPass) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        User user = userMapper.selectUserById(id);
        // 如果user等于空 表示用户不存在
        if(user == null){
            LOG.error("UserService -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在
        // 先验证旧密码正确与否
        if(!user.getId().equals(id) || !user.getPassword().equals(SHA.getResult(oldPass))){
            LOG.error("UserService -> 用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        // 密码加密暂时不写 之后添加
        userMapper.updatePassword(id, newPass);
    }

    @Override
    public void getbackPass(String email, String id) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        User user = userMapper.selectUserById(id);
        // 如果user等于空 表示用户不存在
        if(user == null){
            LOG.error("UserService -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 学号与邮箱不对应
        if(!user.getEmail().equals(email)){
            LOG.error("UserService -> 学号与输入的邮箱不对应");
            throw new CommonException(ResultCode.EMAIL_NOT_TRUE);
        }
        String text = "您好, 您学号为 " + user.getId() + " 的账号的密码为: " + user.getPassword() + " ,\n" +
                "请您妥善保管密码, 如有盗号嫌疑请立即修改密码";
        mailService.sendTextMail(email, "用户找回密码", text);
    }

    @Override
    public String getCode(String email) throws CommonException {
        // 获取一个随机类
        Random random = new Random(System.currentTimeMillis());
        int code = random.nextInt(10000);
        int length = (code + "").length();
        StringBuilder stringBuilder = new StringBuilder();
        if(length < 4){
            for(int i = 0; i < 4 - length; i ++){
                stringBuilder.append("0");
            }
            stringBuilder.append(code);
        }else{
            stringBuilder.append(code);
        }
        String text = "您好, 欢迎您注册餐厅点餐系统, 您本次的验证码为 " + stringBuilder.toString() + ", 验证码有效时间为3分分钟";
        mailService.sendTextMail(email, "用户注册", text);
        return stringBuilder.toString();
    }

    @Override
    public User register(String name, String password, String email) throws CommonException {
        // 首先给商家生成一个商家号
        String userId = generateMerchantId();
        User user = userMapper.selectUserById(userId);
        if(user != null){
            LOG.error("UserService -> 用户已经存在");
            throw new CommonException(ResultCode.MERCHANT_HAVE_EXISTED);
        }
        User user1 = new User();
        user1.setPassword(password);
        user1.setEmail(email);
        user1.setId(userId);
        userMapper.insert(name, password, email);
        LOG.info("UserService -> 用户注册成功");
        return user1;
    }

    private String generateMerchantId(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}
