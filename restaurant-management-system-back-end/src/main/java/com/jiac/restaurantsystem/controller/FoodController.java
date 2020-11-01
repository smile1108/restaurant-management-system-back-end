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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private Jedis jedis;

    @ApiOperation("搜索全部菜品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonReturnType list() throws CommonException {
        String listKey = "food:list";
        Boolean listExist = jedis.exists(listKey);
        List<Food> foods = new ArrayList<>();
        if(listExist){
            LOG.info("FoodController -> 缓存中存在菜品列表,不需要查询数据库");
            // 表示菜品的列表存在 这时就不需要查询数据库
            Set<String> smembers = jedis.smembers(listKey);
            for(String member : smembers){
                Food food = new Food();
                // 从列表中拿出所有菜品的id 然后构造出对应的菜品
                food.setFoodId(Integer.valueOf(member));
                food.setName(jedis.hget("food:info:" + member, "name"));
                food.setPrice(Double.valueOf(jedis.hget("food:info:" + member, "price")));
                food.setTaste(jedis.hget("food:info:" + member, "taste"));
                food.setWicketId(Integer.valueOf(jedis.hget("food:info:" + member, "wicketId")));
                foods.add(food);
            }
        }else{
            LOG.info("FoodController -> 进入/food/list接口");
            foods = foodService.list();
            String foodInfoKey = null;
            for(Food food : foods){
                foodInfoKey = "food:info:" + food.getFoodId();
                jedis.sadd(listKey, food.getFoodId().toString());
                jedis.hset(foodInfoKey, "name", food.getName());
                jedis.hset(foodInfoKey, "price", food.getPrice().toString());
                jedis.hset(foodInfoKey, "taste", food.getTaste());
                jedis.hset(foodInfoKey, "wicketId", food.getWicketId().toString());
            }
        }

        return CommonReturnType.success(foods);
    }

    @ApiOperation("修改菜品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "菜品名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "price", value = "菜品价格", dataType = "double", paramType = "query", required = true),
            @ApiImplicitParam(name = "taste", value = "菜品口味", dataType = "string", paramType = "query", required = true),
    })
    public CommonReturnType update(Integer merchantId, Integer foodId, String name, Double price, String taste){
        return null;
    }

    @ApiOperation("删除菜品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "foodId", value = "菜品id", dataType = "int", paramType = "query", required = true),
    })
    public CommonReturnType delete(Integer merchantId, Integer foodId){
        return null;
    }

    @ApiOperation("增加菜品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家号", dataType = "string", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "name", value = "菜品名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "price", value = "菜品价格", dataType = "double", paramType = "query", required = true),
            @ApiImplicitParam(name = "taste", value = "菜品口味", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "wicketNumber", value = "窗口号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
    })
    public CommonReturnType add(String merchantId, String name, Double price, String taste, Integer floor, Integer wicketNumber) throws CommonException {
        // 先校验参数是否为空
        if(merchantId == null || merchantId.trim().length() == 0
            || name == null || name.trim().length() == 0
            || price == null || taste == null || taste.trim().length() == 0
            || floor == null || wicketNumber == null){
            LOG.error("FoodController -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        merchantService.findByMerchantId(merchantId);
        // 首先验证窗口是否已经开通
        Window window = windowService.findWindowByNumberAndFloor(wicketNumber, floor);
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
        foodService.insert(name, price, taste, window.getWicketId());
        LOG.info("FoodController -> 菜品添加成功");
        return CommonReturnType.success();
    }

    @ApiOperation("按照窗口号查找菜品")
    @RequestMapping(value = "/getByWindowId", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wicketNumber", value = "窗口号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType getByWindowId(Integer wicketNumber, Integer floor){
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
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType getByTaste(Integer floor){
        return null;
    }

}
