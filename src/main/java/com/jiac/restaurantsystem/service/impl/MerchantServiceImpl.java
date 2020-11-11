package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.MerchantMapper;
import com.jiac.restaurantsystem.mapper.WindowMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.MerchantService;
import com.jiac.restaurantsystem.service.SendMail;
import com.jiac.restaurantsystem.utils.SHA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private WindowMapper windowMapper;

    @Autowired
    private SendMail mailService;

    @Override
    public Merchant login(String email, String password) throws CommonException {
        // 通过id在数据库中查找数据
        Merchant merchant = merchantMapper.selectByEmail(email);
        // 如果查找到的merchant为null 表示不存在这个merchant
        if(merchant == null){
            LOG.error("MerchantService -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在 然后验证密码是否正确
        if(!SHA.getResult(password).equals(merchant.getPassword())){
            LOG.error("MerchantService -> 用户名或密码不对");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("MerchantService -> 商家登录成功");
        return merchant;
    }

    @Override
    @Transactional
    public void modifyPass(String email, String oldPass, String newPass, String qualifyPass) throws CommonException {
        //使用UserMapper获取数据库中对应id的学生数据
        Merchant merchant = merchantMapper.selectByEmail(email);
        // 如果user等于空 表示用户不存在
        if(merchant == null){
            LOG.error("MerchantService -> 商家不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果用户存在
        // 先验证旧密码正确与否
        if(!merchant.getEmail().equals(email) || !merchant.getPassword().equals(SHA.getResult(oldPass))){
            LOG.error("MerchantService -> 用户名或密码错误");
            throw new CommonException(ResultCode.AUTH_FAILED);
        }
        LOG.info("MerchantService -> 商家修改密码成功");
        merchantMapper.updatePassword(email, SHA.getResult(newPass));
    }

    @Override
    public void getbackPass(String email, Integer id) throws CommonException {
        // 首先查找对应的merchant
        Merchant merchant = merchantMapper.selectById(id);
        if(merchant == null){
            LOG.error("MerchantService -> 商家不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }

        // 然后验证邮箱与id是否对应
        if(!email.equals(merchant.getEmail())){
            LOG.error("MerchantService -> 邮箱与商户不对应");
            throw new CommonException(ResultCode.EMAIL_NOT_TRUE);
        }
        LOG.info("MerchantService -> 邮件已发送");
        String text = "您好, 您商家号为 " + merchant.getMerchantId() + " 的账号的密码为: " + merchant.getPassword() + " ,\n" +
                "请您妥善保管密码, 如有盗号嫌疑请立即修改密码";
        mailService.sendTextMail(email, "商家找回密码", text);
    }

    @Override
    public Merchant register(String password, String email) throws CommonException {
        Merchant merchant1 = merchantMapper.selectByEmail(email);
        if(merchant1 != null){
            LOG.error("MerchantService -> 商家已经存在");
            throw new CommonException(ResultCode.MERCHANT_HAVE_EXISTED);
        }
        Merchant merchant = new Merchant();
        merchant.setEmail(email);
        merchant.setPassword(password);
        merchantMapper.insert(password, email);
        LOG.info("MerchantService -> 商家注册成功");
        return merchant;
    }

    @Override
    public void findByMerchantId(Integer merchantId) throws CommonException {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if(merchant == null){
            LOG.error("MerchantService -> 商家不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST, "商家不存在");
        }
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
        mailService.sendTextMail(email, "商家注册", text);
        return stringBuilder.toString();
    }

}
