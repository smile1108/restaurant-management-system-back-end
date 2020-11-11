package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.OrderService;
import com.jiac.restaurantsystem.service.UserService;
import com.jiac.restaurantsystem.utils.SerializeUtil;
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
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.io.IOException;
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
    private Jedis jedis;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @ApiOperation("创建订单操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "foodName", value = "菜品名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "预定菜品的数量", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "isPackage", value = "是否打包", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "takeTime", value = "用户预取时间", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType create(String foodName, Integer number, Integer isPackage, String takeTime) throws CommonException {
        String userEmail = searchEmailBySessionId();
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
        // 查找该用户是否存在
        boolean exist = userService.judgeUserIsExistByEmail(userEmail);
        if(!exist){
            LOG.error("OrderController -> createOrder -> 用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
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
        orderService.addOrder(userEmail, foodName, takeTime2, isPackage, 0, orderTime, number, number * foodPrice);
        // 创建一个新的order之后要将缓存中对应用户的orderList删除 避免数据不一致
        String orderListKey = "user:orders:" + userEmail;
        if(jedis.exists(orderListKey)){
            jedis.del(orderListKey);
        }

        return CommonReturnType.success();
    }



    @ApiOperation("取消订单操作")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orderId", value = "要取消的订单号", dataType = "int", paramType = "query", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType delete(Integer orderId) throws CommonException {
        // 先检查参数是否为空
        if(orderId == null){
            LOG.error("OrderController -> deleteOrder -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 获取对应cookie中携带的sessionId对应的用户email
        // searchEmailBySessionId方法中会对不是用户的cookie进行拦截
        String userEmail = searchEmailBySessionId();
        // 然后判断对应订单是否存在
        boolean orderIsExist = orderService.judgeOrderIsExist(orderId);
        if(!orderIsExist){
            // 表示对应订单不存在
            LOG.error("OrderController -> deleteOrder -> 对应订单不存在");
            throw new CommonException(ResultCode.ORDER_IS_NOT_EXIST);
        }
        // 然后判断要删除的order是否属于该用户 如果不属于 没有权限删除
        boolean orderBelongToUser = orderService.judgeOrderBelongToUser(userEmail, orderId);
        if(!orderBelongToUser){
            // 表示对应订单不属于该用户
            LOG.error("OrderController -> deleteOrder -> 对应订单不属于该用户");
            throw new CommonException(ResultCode.ORDER_NOT_BELONG_USER);
        }
        // 还要检查对应订单是否已经完成 如果完成 不能取消订单
        orderService.judgeOrderCompleted(orderId);
        // 全部判断完成之后 才可以进行删除
        orderService.deleteOrder(orderId);
        // 删除之后要将用户orderList key删除 并且将对应orderId的orderInfo删除 防止数据不一致
        String orderListKey = "user:orders:" + userEmail;
        if(jedis.exists(orderListKey)){
            jedis.del(orderListKey);
        }
        String orderInfoKey = "order:info:" + orderId;
        if(jedis.exists(orderInfoKey)){
            jedis.del(orderInfoKey);
        }
        return CommonReturnType.success();
    }

    private String searchEmailBySessionId() throws CommonException {
        String userEmail = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("JSESSIONID")){
                String sessionId = cookie.getValue();
                if(jedis.exists(sessionId)){
                    // redis缓存中存在对应sessionId的键
                    String objectStr = jedis.get(sessionId);
                    try {
                        Object object = SerializeUtil.serializeToObject(objectStr);
                        if(object instanceof UserVO){
                            UserVO userVO = (UserVO)object;
                            LOG.info("OrderController -> 反序列化的对象为UserVO对象");
                            userEmail = userVO.getEmail();
                            break;
                        }else{
                            // 表示不是用户 所以没有权限
                            LOG.error("OrderController -> 反序列化对象不是UserVO对象,没有权限执行此操作");
                            throw new CommonException(ResultCode.HAVE_NOT_ACCESS);
                        }
                    } catch (ClassNotFoundException | IOException | CommonException e) {
                        e.printStackTrace();
                    }
                }else{
                    // 没有对应的键 表示身份认证过期
                    LOG.error("OrderController -> 身份认证过期");
                    throw new CommonException(ResultCode.AUTH_EXPIRED);
                }
            }
        }
        return userEmail;
    }

//    public static void main(String[] args) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");
//        LocalDateTime dateTime = LocalDateTime.now();
//        Timestamp dateTime1 = Timestamp.valueOf(formatter.format(dateTime));
//        System.out.println(dateTime1);
//
//        String time = "2020-11-05 15:36:57";
//        Timestamp dateTime2 = Timestamp.valueOf(time);
//        System.out.println(dateTime2);
//    }
}
