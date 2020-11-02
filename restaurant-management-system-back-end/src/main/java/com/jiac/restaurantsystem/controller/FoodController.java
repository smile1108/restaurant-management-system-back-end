package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Food;
import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.controller.VO.FoodVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.FoodMapper;
import com.jiac.restaurantsystem.mapper.WindowMapper;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.MerchantService;
import com.jiac.restaurantsystem.service.WindowService;
import com.jiac.restaurantsystem.utils.SerializeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import javax.xml.transform.Result;
import java.io.IOException;
import java.text.FieldPosition;
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
    public CommonReturnType list() throws CommonException, IOException, ClassNotFoundException {
        LOG.info("FoodController -> 进入/food/list接口");
        String listKey = "food:list";
        Boolean listExist = jedis.exists(listKey);
        List<FoodVO> foodVOS = new ArrayList<>();
        if(listExist){
            LOG.info("FoodController -> 缓存中存在菜品列表,不需要查询数据库");
            // 表示菜品的列表存在 这时就不需要查询数据库
            Set<String> smembers = jedis.smembers(listKey);
            for(String member : smembers){
                // 从列表中拿出所有菜品的id 然后构造出对应的菜品
                String foodInfoKey = "food:info:" + member;
                FoodVO foodVO = (FoodVO)SerializeUtil.serializeToObject(jedis.get(foodInfoKey));
                foodVOS.add(foodVO);
            }
        }else{
            LOG.info("FoodController -> 缓存中没有数据,要查询数据库");
            List<Food> foods = foodService.list();
            String foodInfoKey = null;
            for(Food food : foods){
                foodInfoKey = "food:info:" + food.getFoodId();
                FoodVO foodVO = convertFromFood(food);
                jedis.sadd(listKey, food.getFoodId().toString());
                jedis.set(foodInfoKey, SerializeUtil.serialize(foodVO));
            }
            foodVOS = convertFromFoodList(foods);
        }

        return CommonReturnType.success(foodVOS);
    }

    @ApiOperation("修改菜品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "菜品名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "price", value = "菜品价格", dataType = "double", paramType = "query", required = true),
            @ApiImplicitParam(name = "taste", value = "菜品口味", dataType = "string", paramType = "query", required = true),
    })
    public CommonReturnType update(Integer merchantId, String name, Double price, String taste) throws CommonException {
        // 先校验参数不能为空
        if(merchantId == null || name == null || name.trim().length() == 0
            || price == null || taste == null || taste.trim().length() == 0){
            LOG.error("FoodController -> 修改菜品 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 然后根据这个名字查看这个菜品是否属于这个商家 如果不属于 没有权限修改

        return CommonReturnType.success();
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
            @ApiImplicitParam(name = "merchantId", value = "商家号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "name", value = "菜品名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "price", value = "菜品价格", dataType = "double", paramType = "query", required = true),
            @ApiImplicitParam(name = "taste", value = "菜品口味", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "wicketNumber", value = "窗口号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
    })
    public CommonReturnType add(Integer merchantId, String name, Double price, String taste, Integer floor, Integer wicketNumber) throws CommonException {
        // 先校验参数是否为空
        if(merchantId == null
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
    public CommonReturnType getByWindowId(Integer wicketNumber, Integer floor) throws CommonException, IOException, ClassNotFoundException {
        // 先校验参数是否为空
        if(wicketNumber == null || floor == null){
            LOG.error("FoodController -> 按照窗口查找菜品 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 根据窗口号和楼层查找对应的窗口 看看是否存在
        Window window = windowService.findWindowByNumberAndFloor(wicketNumber, floor);
        // findWindowByNumberAndFloor方法内部如果窗口不存在 会抛出异常 所以如果执行到下边 表示窗口存在
        Integer windowId = window.getWicketId();
        String windowKey = "food:window:" + windowId;
        List<FoodVO> foodVOS = new ArrayList<>();
        String foodInfoKey = null;
        if(jedis.exists(windowKey)){
            LOG.info("FoodController -> 根据窗口id查找菜品 -> 缓存中有对应窗口的菜品缓存,不需要查找数据库");
            // 如果对应窗口id 存在缓存的数据 直接从缓存中拿取
            Set<String> smembers = jedis.smembers(windowKey);
            for(String member : smembers){
                // 从列表中拿出所有菜品的id 然后构造出对应的菜品
                foodInfoKey = "food:info:" + member;
                FoodVO foodVO = (FoodVO)SerializeUtil.serializeToObject(jedis.get(foodInfoKey));
                foodVOS.add(foodVO);
            }
        }else{
            LOG.info("FoodController -> 根据窗口id查找菜品 -> 缓存中没有对应窗口的菜品,需要查询数据库");
            // 如果缓存中不存在对应的数据 应该从数据库进行查找 并写入缓存中
            List<Food> foods = foodService.selectFoodsByWindowId(windowId);
            // 从数据库中获取数据之后 写入缓存中 以便之后从缓存中获取数据 不需要通过数据库
            for(Food food : foods){
                // 遍历所有的food 进行操作
                // 先将foodId放入对应该窗口id的缓存键中
                LOG.info("FoodController -> 将foodId放入该窗口id对应的缓存键中");
                jedis.sadd(windowKey, food.getFoodId().toString());
                foodInfoKey = "food:info:" + food.getFoodId();
                // 先判断这个菜品在redis缓存中是否已经有了这条数据 如果有的话就不需要再添加了
                // 不存在的话才添加对应的数据
                if(!jedis.exists(foodInfoKey)){
                    FoodVO foodVO = convertFromFood(food);
                    jedis.set(foodInfoKey, SerializeUtil.serialize(foodVO));
                }
            }
            foodVOS = convertFromFoodList(foods);
        }
        return CommonReturnType.success(foodVOS);
    }

    @ApiOperation("按照口味查找菜品")
    @RequestMapping(value = "/getByTaste", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taste", value = "口味", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType getByTaste(String taste) throws CommonException, IOException, ClassNotFoundException {
        // 先判断对应参数是否为空
        if(taste == null || taste.trim().length() == 0){
            LOG.error("FoodController -> getByTaste -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 构建taste对用的键
        String tasteKey = "food:taste:" + taste;
        // 构建FoodVO的集合
        List<FoodVO> foodVOS = new ArrayList<>();
        // 然后先看redis缓存中是否有这个键 如果有的话直接使用缓存
        String foodInfoKey = null;
        if(jedis.exists(tasteKey)){
            LOG.info("FoodController -> 根据口味查找菜品 -> 缓存中有对应口味的菜品");
            // 查找到缓存中tasteKey中所有的foodId
            Set<String> smembers = jedis.smembers(tasteKey);
            for(String member : smembers){
                // 拿出id 构建对应food的key food:info:#{id}
                foodInfoKey = "food:info:" + member;
                FoodVO foodVO = (FoodVO)SerializeUtil.serializeToObject(jedis.get(foodInfoKey));
                foodVOS.add(foodVO);
            }
        }else{
            LOG.info("FoodController -> 根据口味查找菜品 -> 缓存中没有对应口味的菜品");
            // 从数据库中获取数据
            List<Food> foods = foodService.selectFoodsByTaste(taste);
            for(Food food : foods){
                // 遍历查询到的food数组 将其加入缓存中
                LOG.info("FoodController -> 将查找到的taste的菜品加入缓存");
                jedis.sadd(tasteKey, food.getFoodId().toString());
                foodInfoKey = "food:info:" + food.getFoodId();
                // 先判断这个菜品在redis缓存中是否已经有了这条数据 如果有的话就不需要再添加了
                // 不存在的话才添加对应的数据
                if(!jedis.exists(foodInfoKey)){
                    FoodVO foodVO = convertFromFood(food);
                    jedis.set(foodInfoKey, SerializeUtil.serialize(foodVO));
                }
            }
            foodVOS = convertFromFoodList(foods);
        }
        return CommonReturnType.success(foodVOS);
    }

    @ApiOperation("按照楼层查找菜品")
    @RequestMapping(value = "/getByLevel", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType getByLevel(Integer floor){
        return null;
    }

    private FoodVO convertFromFood(Food food){
        if(food == null){
            return null;
        }
        FoodVO foodVO = new FoodVO();
        BeanUtils.copyProperties(food, foodVO);
        return foodVO;
    }

    private List<FoodVO> convertFromFoodList(List<Food> foods){
        if(foods == null){
            return null;
        }
        List<FoodVO> foodVOS = new ArrayList<>();
        for(Food food : foods){
            FoodVO foodVO = new FoodVO();
            BeanUtils.copyProperties(food, foodVO);
            foodVOS.add(foodVO);
        }
        return foodVOS;
    }

}
