package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: MerchantController
 * Author: Jiac
 * Date: 2020/10/9 10:32
 */
@Api(value = "商家controller", description = "商家操作")
@RestController
@RequestMapping("/api/dbcourse/merchant")
public class MerchantController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(MerchantController.class);


    @ApiOperation("商家登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "商家名称", dataType = "string", paramType = "body", required = true ),
            @ApiImplicitParam(name = "password", value = "商家密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType login(String name, String password){
        return null;
    }

    @ApiOperation("商家修改密码")
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "商家名称", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "newPass", value = "商家新密码", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "商家确认密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType modifyPass(String id, String newPass, String qualifyPass){
        return null;
    }

    @ApiOperation("商家找回密码")
    @RequestMapping(value = "/getbackPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "name", value = "商家名称  ", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType getbackPass(String email, String id){
        return null;
    }

    @ApiOperation("注册商家")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "商家名称", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "password", value = "商家密码", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType register(String name, String password, String email){
        return null;
    }
}
