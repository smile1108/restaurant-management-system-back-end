package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Food;
import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.FoodMapper;
import com.jiac.restaurantsystem.mapper.WindowMapper;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.MerchantService;
import com.jiac.restaurantsystem.service.WindowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: FoodController
 * Author: Jiac
 * Date: 2020/10/9 9:45
 */
@Api(value = "菜品controller", description = "菜品操作")
@RestController
@RequestMapping("/api/dbcourse/food")
public class FoodController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(FoodController.class);


    @Autowired
    private WindowService windowService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private FoodService foodService;

    @ApiOperation("搜索全部菜品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonReturnType list(){
        return null;
    }

    @ApiOperation("修改菜品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
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
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
    })
    public CommonReturnType delete(Integer merchantId, Integer foodId){
        return null;
    }

    @ApiOperation("增加菜品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家号", dataType = "string", paramType = "body", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "foodName", value = "菜品名称", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodPrice", value = "菜品价格", dataType = "double", paramType = "body", required = true),
            @ApiImplicitParam(name = "foodTaste", value = "菜品口味", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "windowNumber", value = "窗口号", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
    })
    public CommonReturnType add(String merchantId, String foodName, Double foodPrice, String foodTaste, Integer floor, Integer windowNumber) throws CommonException {
        // 先校验参数是否为空
        if(merchantId == null || merchantId.trim().length() == 0
            || foodName == null || foodName.trim().length() == 0
            || foodPrice == null || foodTaste == null || foodTaste.trim().length() == 0
            || floor == null || windowNumber == null){
            LOG.error("FoodController -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        merchantService.findByMerchantId(merchantId);
        // 首先验证窗口是否已经开通
        Window window = windowService.findWindowByNumberAndFloor(windowNumber, floor);
        if(window == null){
            LOG.error("FoodController -> 该窗口还未开通");
            throw new CommonException(ResultCode.WINDOW_IS_NOT_OPEN);
        }
        // 然后根据这个window对象判断是否属于这个商家
        if(!window.getMerchantId().equals(merchantId)){
            LOG.error("FoodController -> 该窗口不属于该商家, 该商家没有权限为该窗口添加菜品");
            throw new CommonException(ResultCode.HAVE_NOT_ACCESS, "该窗口不属于该商家");
        }
        // 全部验证通过后 才能添加菜品
        foodService.insert(foodName, foodPrice, foodTaste, window.getWicketId());
        LOG.info("FoodController -> 菜品添加成功");
        return CommonReturnType.success();
    }

    @ApiOperation("按照窗口号查找菜品")
    @RequestMapping(value = "/getByWindowId", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
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
            @ApiImplicitParam(name = "level", value = "楼层", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType getByTaste(Integer level){
        return null;
    }

}
