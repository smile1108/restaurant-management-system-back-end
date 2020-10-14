package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * FileName: UserController
 * Author: Jiac
 * Date: 2020/10/9 9:10
 */
@Api(value = "用户controller", description = "用户操作")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private JedisPool jedisPool;


    @ApiOperation("用户登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true ),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType login(String id, String password){
        return CommonReturnType.success();
    }

    @ApiOperation("用户修改密码")
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true),
        @ApiImplicitParam(name = "newPass", value = "用户新密码", dataType = "string", paramType = "body", required = true),
        @ApiImplicitParam(name = "qualifyPass", value = "用户确认密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType modifyPass(String id, String newPass, String qualifyPass){
        return null;
    }

    @ApiOperation("用户找回密码")
    @RequestMapping(value = "/getbackPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType getbackPass(String email, String id){
        return null;
    }
}
