package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.*;
import com.jiac.restaurantsystem.controller.VO.FoodVO;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.controller.VO.OrderVO;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.OrderService;
import com.jiac.restaurantsystem.service.UserService;
import com.jiac.restaurantsystem.service.WindowService;
import com.jiac.restaurantsystem.utils.SHA;
import com.jiac.restaurantsystem.utils.SerializeUtil;
import com.jiac.restaurantsystem.utils.UserNameGenerator;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * FileName: UserController
 * Author: Jiac
 * Date: 2020/10/9 9:10
 */
@Api(value = "用户controller", description = "用户操作")
@RestController
@RequestMapping("/api/dbcourse/user")
public class UserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private Jedis jedis;

//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public String test() throws IOException, ClassNotFoundException {
//        Cookie[] cookies = httpServletRequest.getCookies();
//        String s = null;
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals("JSESSIONID")){
//                s = jedis.get(cookie.getValue());
//                break;
//            }
//        }
//        if(s == null){
//            return "null";
//        }
//        Object o = SerializeUtil.serializeToObject(s);
//        System.out.println(o);
//        return "test";
//    }


    @ApiOperation("用户登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "query", required = true)
    })
    @ResponseBody
    public CommonReturnType login(String id, String password) throws CommonException, IOException {
        // 首先验证参数是否为空
        if (id == null || id.trim().length() == 0 || password == null || password.trim().length() == 0) {
            LOG.error("UserController -> 用户登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }

        boolean useEmailLogin = pattern.matcher(id).matches();
        boolean redisLoginSuccess = false;

        // 用户可能使用两种登录方式  这里对每种方式都进行存入 所以直接使用id
        String infoKey = "user:info:" + id;
        if(jedis.exists(infoKey)){
            // 表示redis缓存中有对应的用户的信息 直接使用缓存 不需要查询数据库
            LOG.info("UserController -> 使用redis缓存获取到信息");
            // 这里注意 用户密码 和 商家密码都是经过加密的 所以要加密后进行比对
            if(useEmailLogin){
                // 如果使用邮箱登录  就检查对应邮箱 和 密码 否则 检查学号 和 密码
                if(!jedis.hget(infoKey, "email").equals(id) || !jedis.hget(infoKey, "password").equals(SHA.getResult(password))){
                    LOG.error("UserController -> 用户名或密码不正确");
                    throw new CommonException(ResultCode.AUTH_FAILED);
                }
            }else{
                // 校验 学号 和 密码
                if(!jedis.hget(infoKey, "id").equals(id) || !jedis.hget(infoKey, "password").equals(SHA.getResult(password))){
                    LOG.error("UserController -> 用户名或密码不正确");
                    throw new CommonException(ResultCode.AUTH_FAILED);
                }
            }
            // 登录成功
            LOG.info("UserController -> 使用缓存登录成功");
            redisLoginSuccess = true;
        }
        User user = null;
        if(!redisLoginSuccess){
            // 如果没有使用redis验证 说明还没有对应的键 需要查询数据库进行登录 并将用户对应信息存入redis
            // 检验输入的是邮箱还是学号
            if (useEmailLogin) {
                LOG.info("用户使用邮箱登录");
                // 表示输入的是邮箱
                user = userService.loginByEmail(id, password);
            } else {
                LOG.info("用户使用学号登录");
                // 表示用学号登录
                user = userService.loginById(id, password);
            }
            jedis.hset(infoKey, "email", user.getEmail());
            jedis.hset(infoKey, "name", user.getName());
            jedis.hset(infoKey, "password", user.getPassword());
            jedis.hset(infoKey, "id", user.getId());
        }else{
            user = new User();
            user.setId(jedis.hget(infoKey, "id"));
            user.setEmail(jedis.hget(infoKey, "email"));
            user.setName(jedis.hget(infoKey, "name"));
            user.setPassword(jedis.hget(infoKey, "password"));
        }
        // 这里注意 如果使用了redis登录成功  user不会赋值 所以会空指针 所以要做一个判断
        String userEmail = redisLoginSuccess ? jedis.hget(infoKey, "email") : user.getEmail();
//        Cookie[] cookies = httpServletRequest.getCookies();
//        boolean hasSessionId = false;
//        if(cookies != null){
//            for(Cookie cookie : cookies){
//                if(cookie.getName().equals("JSESSIONID")){
//                    hasSessionId = true;
//                    break;
//                }
//            }
//        }
        String key = "session:user:" + userEmail;

        // 如果没有sessionId才创建cookie 否则不创建cookie
        if(!jedis.exists(key)){
            LOG.info("用户登录，创建cookie");
            UserVO userVO = convertFromUserDO(user);
            jedis.setex(key, 300, httpServletRequest.getSession().getId());
            jedis.setex(httpServletRequest.getSession().getId(), 300, SerializeUtil.serialize(userVO));
            Cookie cookie = new Cookie("JSESSIONID", httpServletRequest.getSession().getId());
            cookie.setMaxAge(60);
            httpServletResponse.addCookie(cookie);
        }else{
            LOG.error("不可以重复登录");
            throw new CommonException(ResultCode.IS_LOGINED);
        }

        // 没有问题 返回响应
        return CommonReturnType.success();
    }

    @ApiOperation("修改信息")
    @RequestMapping(value = "/modifyMsg", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户昵称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "学生号", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "email", value = "学生邮箱", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType modifyMsg(String name, String id, String email) throws CommonException {
        // 先判断参数是否为空
        if(name == null || name.trim().length() == 0
            || id == null || id.trim().length() == 0
            || email == null || email.trim().length() == 0){
            LOG.error("UserController -> modifyMsg -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        boolean userIsExist = userService.judgeUserIsExistByEmail(email);
        if(!userIsExist){
            LOG.error("UserController -> modifyMsg -> 邮箱对应用户不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 修改信息
        userService.modifyMsgByEmail(name, id, email);
        // 然后要删除掉redis中对应的缓存 防止数据不一致
        String key1 = "user:info:" + email;
        String key2 = "user:info:" + id;
        if(jedis.exists(key1)){
            jedis.del(key1);
        }
        if(jedis.exists(key2)){
            jedis.del(key2);
        }
        return CommonReturnType.success();
    }


    @ApiOperation("注册用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "确认密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType register(String password, String qualifyPass, String code, String email) throws CommonException {
        if (!password.equals(qualifyPass)) {
            LOG.error("UserController ->  用户注册 -> 两次输入密码不一致");
            throw new CommonException(ResultCode.PASSWORD_NOT_EQUAL);
        }
        if (password == null || password.trim().length() == 0
                || email == null || email.trim().length() == 0) {
            LOG.error("UserController -> 用户注册 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        String s = jedis.get(email);
        if (s == null) {
            LOG.error("UserController -> 用户注册 -> 验证码过期");
            throw new CommonException(ResultCode.CODE_IS_EXPIRED);
        }
        if (!s.equals(code)) {
            LOG.error("UserController -> 用户注册 -> 验证码不正确");
            throw new CommonException(ResultCode.CODE_IS_NOT_RIGHT);
        }
        String randomName = UserNameGenerator.getRandomName();
        User user = userService.register(randomName, SHA.getResult(password), email);
        UserVO userVO = convertFromUserDO(user);
        return CommonReturnType.success(userVO);
    }

    @ApiOperation("用户修改密码")
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "newPass", value = "用户新密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "oldPass", value = "用户旧密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "用户确认密码", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType modifyPass(String email, String oldPass, String newPass, String qualifyPass) throws CommonException {
        //首先验证参数是否为空
        if (email == null || email.trim().length() == 0 || oldPass == null || oldPass.trim().length() == 0
                || newPass == null || newPass.trim().length() == 0 || qualifyPass == null || qualifyPass.trim().length() == 0) {
            LOG.error("UserController -> 修改密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 验证两次输入的密码是否一致
        if (!newPass.equals(qualifyPass)) {
            LOG.error("UserController -> 两次输入密码不一致");
            throw new CommonException(ResultCode.PASSWORD_NOT_EQUAL);
        }
        // 如果参数验证成功 就调用service查询数据库
        User user = userService.modifyPass(email, oldPass, newPass, qualifyPass);
        String key1 = "user:info:" + user.getEmail();
        String key2 = "user:info:" + user.getId();
        if(jedis.exists(key1)){
            jedis.del(key1);
        }
        if(jedis.exists(key2)){
            jedis.del(key2);
        }

        return CommonReturnType.success();
    }

    @ApiOperation("用户找回密码")
    @RequestMapping(value = "/getbackPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType getbackPass(String email, String id) throws CommonException {
        //首先校验参数是否为空
        if (email == null || email.trim().length() == 0 ||
                id == null || id.trim().length() == 0) {
            LOG.error("UserController -> 找回密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }

        userService.getbackPass(email, id);

        return CommonReturnType.success();
    }

    @ApiOperation("用户获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public CommonReturnType getCode(String email) throws CommonException {
        String code = userService.getCode(email);
        jedis.setex(email, 180, code);
        return CommonReturnType.success();
    }

    @ApiOperation("用户退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public CommonReturnType logout(String email) throws CommonException{
        if(email == null || email.trim().length() == 0){
            LOG.error("UserController -> 用户退出登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        String key = "session:user:" + email;
        String s = jedis.get(key);
        if(s == null){
            LOG.info("UserController -> 用户退出登录 -> 用户身份已经失效,退出成功");
            return CommonReturnType.success();
        }
        // 如果用户身份还没有失效 退出登录后 删除对应的sessionId
        LOG.info("UserController -> 用户退出登录 -> 退出登录成功");
        jedis.del(key);
        // 还要删除用户序列化的信息
        jedis.del(s);
        return CommonReturnType.success();
    }

    @ApiOperation("用户获取自己所有订单")
    @RequestMapping(value = "/getAllOrders", method = RequestMethod.GET)
    public CommonReturnType getAllOrders() throws CommonException, IOException, ClassNotFoundException {
        String userEmail = null;
        try{
            // 先通过sessionId获取对应的用户邮箱
            userEmail = searchEmailBySessionId();
        }catch (CommonException e){
            throw new CommonException(e);
        }
        // 构建对应用户的全部订单的键
        String orderListKey = "user:orders:" + userEmail;
        List<OrderVO> userOrderVos = new ArrayList<>();
        if(jedis.exists(orderListKey)){
            LOG.info("UserController -> getAllOrders -> redis缓存中有对应用户的所有订单");
            List<String> orderIdList = jedis.lrange(orderListKey, 0, -1);
            for(String orderIdStr : orderIdList){
                // 先将orderId 转化为Integer
                Integer orderId = Integer.valueOf(orderIdStr);
                String orderInfoKey = "order:info:" + orderId;
                OrderVO orderVO = (OrderVO)(SerializeUtil.serializeToObject(jedis.get(orderInfoKey)));
                userOrderVos.add(orderVO);
            }
        }else{
            LOG.info("UserController -> getAllOrders -> redis缓存中没有对应用户的所有订单");
            // 然后通过该email 获取所有的订单
            List<Order> orders = orderService.getAllOrderByUserEmail(userEmail);
            addAllOrderInfoToRedis(orders, orderListKey);
            userOrderVos = convertFromOrderList(orders);
        }

        // 直接返回结果
        return CommonReturnType.success(userOrderVos);
    }

    @ApiOperation("用户评分订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "grade", value = "用户评分", dataType = "int", paramType = "query", required = true)
    })
    @RequestMapping(value = "/gradeOrder", method = RequestMethod.POST)
    public CommonReturnType gradeOrder(Integer orderId, Integer grade) throws CommonException {
        // 先判断参数是否为空
        if(orderId == null){
            LOG.error("UserController -> gradeOrder -> 参数为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 判断grade是否小于等于0
        if(grade <= 0 || grade > 10){
            LOG.error("UserController -> gradeOrder -> 评分不能小于等于0或大于10");
            throw new CommonException(ResultCode.GRADE_EXCEPTION);
        }
        boolean orderIsExist = orderService.judgeOrderIsExist(orderId);
        if(!orderIsExist){
            // 表示订单不存在
            LOG.error("UserController -> gradeOrder -> 订单不存在");
            throw new CommonException(ResultCode.ORDER_IS_NOT_EXIST);
        }
        boolean orderIsCompleted = orderService.judgeOrderIsCompleted(orderId);
        if(!orderIsCompleted){
            // 表示订单未完成 此时不能评分
            LOG.error("UserController -> gradeOrder -> 订单尚未完成,不能评分");
            throw new CommonException(ResultCode.ORDER_HAVE_NOT_COMPLETED);
        }
        // 都判断完成之后 才能进行评分
        orderService.gradeOrder(orderId, grade);

        // 返回结果
        return CommonReturnType.success();
    }

    private UserVO convertFromUserDO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
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

    private void addAllOrderInfoToRedis(List<Order> orders, String key) throws IOException {
        String orderInfoKey = null;
        for(Order order : orders){
            Integer orderId = order.getOrderId();
            // 获取对应order的id
            jedis.lpush(key, orderId.toString());
            // 设置过期时间
            jedis.expire(key, 60);
            orderInfoKey = "order:info:" + orderId;
            OrderVO orderVO = convertFromOrder(order);
            jedis.setex(orderInfoKey, 60, SerializeUtil.serialize(orderVO));
        }
    }

    private OrderVO convertFromOrder(Order order){
        if(order == null){
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }

    private List<OrderVO> convertFromOrderList(List<Order> orders) throws CommonException {
        if(orders == null){
            return null;
        }
        List<OrderVO> orderVOS = new ArrayList<>();
        for(Order order : orders){
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            Food food = foodService.selectFoodById(order.getFoodId());
            orderVO.setFoodName(food.getName());
            Window window = windowService.selectWindowById(food.getWicketId());
            orderVO.setWicketNumber(window.getWicketNumber());
            orderVO.setFloor(window.getFloor());
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }
}
