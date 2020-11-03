package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Food;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.FoodMapper;
import com.jiac.restaurantsystem.mapper.WindowMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FileName: FoodServiceImpl
 * Author: Jiac
 * Date: 2020/10/16 11:27
 */
@Service
public class FoodServiceImpl implements FoodService {

    private static final Logger LOG = LoggerFactory.getLogger(FoodServiceImpl.class);

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private WindowMapper windowMapper;

    @Override
    public void insert(String name, Double price, String taste, Integer wicketId) throws CommonException {
        Food food = foodMapper.selectFoodByName(name);
        if(food != null){
            LOG.error("FoodServiceImpl -> 添加菜品 -> 菜品已经存在");
            // 如果可以使用菜品名字查出来菜品 说明菜品已经存在 不可以再添加
            throw new CommonException(ResultCode.FOOD_IS_EXISTED);
        }
        foodMapper.insert(name, price, taste, wicketId);
    }

    @Override
    public List<Food> list() throws CommonException {
        LOG.info("FoodServiceImpl -> 查找所有菜品");
        List<Food> foods = foodMapper.selectAllFood();
        return foods;
    }

    @Override
    public List<Food> selectFoodsByWindowId(Integer windowId) throws CommonException {
        LOG.info("FoodServiceImpl -> 根据窗口查找菜品");
        List<Food> foods = foodMapper.selectFoodsByWindowId(windowId);
        return foods;
    }

    @Override
    public List<Food> selectFoodsByTaste(String taste) throws CommonException {
        LOG.info("FoodServiceImpl -> 根据口味查找菜品 -> " + taste);
        List<Food> foods = foodMapper.selectFoodsByTaste(taste);
        return foods;
    }

    @Override
    public List<Food> selectFoodsByFloor(Integer floor) throws CommonException {
        LOG.info("FoodServiceImpl -> 根据楼层查找菜品 -> " + floor);
        List<Food> foods = foodMapper.selectFoodsByFloor(floor);
        return foods;
    }

    @Override
    public boolean judgeFoodIsBelongMerchant(Integer merchantId, Integer wicketId) throws CommonException {
        Integer id = windowMapper.selectMerchantByWicket(wicketId);
        if(id.equals(merchantId)){
            return true;
        }
        return false;
    }

    @Override
    public Integer judgeFoodIsExist(Integer foodId) {
        // 根据name查找对应的菜品 找对对应的wicket_id
        Integer wicketId = foodMapper.selectWicketIdByFoodId(foodId);
        if(wicketId == null){
            return -1;
        }
        return wicketId;
    }

    @Override
    public void updateFood(Integer foodId, String name, Double price, String taste) throws CommonException {
        foodMapper.updateFood(foodId, name, price, taste);
    }
}
