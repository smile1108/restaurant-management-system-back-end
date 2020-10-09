package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: AdministratorController
 * Author: Jiac
 * Date: 2020/10/9 11:14
 */
@Api(value = "管理员controller", description = "管理员操作")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @ApiOperation("管理员登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "管理员账号", dataType = "string", paramType = "body", required = true ),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType login(String name, String password){
        return null;
    }
}
