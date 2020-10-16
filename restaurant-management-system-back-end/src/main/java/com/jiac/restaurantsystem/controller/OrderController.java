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

import java.sql.Time;

/**
 * FileName: OrderController
 * Author: Jiac
 * Date: 2020/10/9 9:56
 */
@Api(value = "订单controller", description = "订单操作")
@RestController
@RequestMapping("/api/dbcourse/order")
public class OrderController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);


    @ApiOperation("创建订单操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "number", value = "预定菜品的数量", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "isPackage", value = "是否打包", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "predictTime", value = "用户预取时间", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType create(Integer foodId, Integer number, Integer isPackage, String predictTime){
        return null;
    }


    @ApiOperation("取消订单操作")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orderId", value = "要取消的订单号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType delete(Integer orderId){
        return null;
    }
}
