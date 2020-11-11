package com.jiac.restaurantsystem.utils;

import java.util.Random;

/**
 * FileName: UserNameGenerator
 * Author: Jiac
 * Date: 2020/10/22 7:21
 */
public class UserNameGenerator {

    public static String getRandomName(){
        // 新建一个随机对象
        Random random = new Random(System.currentTimeMillis());
        // 用于生成最终的随机字符串
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 4; i++){
            stringBuilder.append((char)(random.nextInt(26) + 65));
        }
        for(int i = 0; i < 4; i++){
            stringBuilder.append((char)(random.nextInt(26) + 97));
        }
        for(int i = 0; i < 6; i++){
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
