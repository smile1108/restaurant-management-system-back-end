package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * FileName: FoodController
 * Author: Jiac
 * Date: 2020/10/9 9:45
 */
@Api(value = "菜品controller", description = "菜品操作")
@RestController
@RequestMapping("/food")
public class FoodController {

    @ApiOperation("搜索全部菜品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonReturnType list(){
        return null;
    }

    @ApiOperation("修改菜品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodName", value = "菜品名称", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodPrice", value = "菜品价格", dataType = "double", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodTaste", value = "菜品口味", dataType = "string", paramType = "body", required = true),
    })
    public CommonReturnType update(Integer merchantId, Integer foodId, String foodName, Double foodPrice, String foodTaste){
        return null;
    }

    @ApiOperation("删除菜品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "body", required = true),
    })
    public CommonReturnType delete(Integer merchantId, Integer foodId){
        return null;
    }

    @ApiOperation("增加菜品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodName", value = "菜品名称", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodPrice", value = "菜品价格", dataType = "double", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodTaste", value = "菜品口味", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "windowId", value = "窗口id", dataType = "int", paramType = "body", required = true),
    })
    public CommonReturnType add(Integer merchantId, Integer foodId, String foodName, Double foodPrice, String foodTaste, Integer windowId){
        return null;
    }

    @ApiOperation("按照窗口号查找菜品")
    @RequestMapping(value = "/getByWindowId", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口号", dataType = "int", paramType = "query", required = true)
    })
    public CommonReturnType getByWindowId(Integer windowId){
        return null;
    }

    @ApiOperation("按照口味查找菜品")
    @RequestMapping(value = "/getByTaste", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taste", value = "口味", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType getByTaste(String taste){
        return null;
    }

    @ApiOperation("按照楼层查找菜品")
    @RequestMapping(value = "/getByLevel", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "楼层", dataType = "int", paramType = "query", required = true)
    })
    public CommonReturnType getByTaste(Integer level){
        return null;
    }

}
