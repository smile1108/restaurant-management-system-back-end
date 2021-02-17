package com.jiac.restaurantsystem.utils;

import com.jiac.restaurantsystem.config.RabbitMQConfig;
import com.jiac.restaurantsystem.service.SendMail;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * FileName: RabbitMQConsumer
 * Author: Jiac
 * Date: 2021/2/17 9:22
 */
@Component
@RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_EMAIL})
public class RabbitMQConsumer {

    @Autowired
    private SendMail mailService;

    @Autowired
    private Jedis jedis;

    @RabbitHandler
    public void send_email(String email) {
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
        jedis.setex(email, 180, String.valueOf(code));
    }
}
