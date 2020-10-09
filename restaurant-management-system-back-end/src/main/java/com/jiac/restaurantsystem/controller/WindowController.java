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
 * FileName: WindowController
 * Author: Jiac
 * Date: 2020/10/9 11:27
 */
@Api(value = "窗口controller", description = "窗口操作")
@RestController
@RequestMapping("/window")
public class WindowController {

    @ApiOperation("窗口开通操作")
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true)
    })
    public CommonReturnType open(Integer windowId, Integer merchantId){
        return null;
    }
}
