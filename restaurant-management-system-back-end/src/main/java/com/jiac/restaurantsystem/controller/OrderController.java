package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;


    @ApiOperation("创建订单操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "foodName", value = "菜品名称", dataType = "string", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "number", value = "预定菜品的数量", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "isPackage", value = "是否打包", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "takeTime", value = "用户预取时间", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType create(String foodName, Integer number, Integer isPackage, String takeTime) throws CommonException {
        // 先校验参数是否为空
        if(foodName == null || foodName.trim().length() == 0 ||
            number == null || isPackage == null || takeTime == null
            || takeTime.trim().length() == 0){
            LOG.error("OrderController -> createOrder -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        if(isPackage != 0 && isPackage != 1){
            LOG.error("OrderController -> createOrder -> 是否打包参数异常");
            throw new CommonException(ResultCode.PACKAGE_PARAMETER_ERROR);
        }
        if(number.intValue() > 20 || number.intValue() < 0){
            LOG.error("OrderController -> createOrder -> 订餐数量异常(数量不能超过20,也不能为负数)");
            throw new CommonException(ResultCode.ORDER_NUMBER_ERROR);
        }
        // 获取对应菜品的单价
        Double foodPrice = foodService.selectFoodPriceByFoodName(foodName);
        // 将前端传递过来的时间字符串转换为Timestamp
        Timestamp takeTime2 = Timestamp.valueOf(takeTime);
        // 构建当前时间 用于传入数据库中
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp orderTime = Timestamp.valueOf(formatter.format(dateTime));
        // 比较预取时间和当前时间 如果预取时间早于当前时间 报错
        boolean before = takeTime2.before(orderTime);
        if(before){
            LOG.error("OrderController -> createOrder -> 预取时间不能早于当前时间");
            throw new CommonException(ResultCode.TIME_ERROR);
        }
        // 全部校验完成之后进行数据的写入
        orderService.addOrder(foodName, takeTime2, isPackage, 0, orderTime, number, number * foodPrice);

        return CommonReturnType.success();
    }


    @ApiOperation("取消订单操作")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orderId", value = "要取消的订单号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType delete(Integer orderId){
        return null;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp dateTime1 = Timestamp.valueOf(formatter.format(dateTime));
        System.out.println(dateTime1);

        String time = "2020-11-05 15:36:57";
        Timestamp dateTime2 = Timestamp.valueOf(time);
        System.out.println(dateTime2);
    }
}
