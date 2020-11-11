package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.Order;
import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.controller.VO.OrderVO;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.FoodService;
import com.jiac.restaurantsystem.service.MerchantService;
import com.jiac.restaurantsystem.service.OrderService;
import com.jiac.restaurantsystem.utils.SHA;
import com.jiac.restaurantsystem.utils.SerializeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: MerchantController
 * Author: Jiac
 * Date: 2020/10/9 10:32
 */
@Api(value = "商家controller", description = "商家操作")
@RestController
@RequestMapping("/api/dbcourse/merchant")
public class MerchantController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

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

    @ApiOperation("商家登录验证")
        @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "query", required = true ),
            @ApiImplicitParam(name = "password", value = "商家密码", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType login(String email, String password) throws CommonException, IOException {
        // 商家使用商家id和密码进行登录
        // 首先校验参数是否为空
        if(email == null || email.trim().length() == 0 || password == null || password.trim().length() == 0){
            LOG.error("MerchantController -> 商家登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }

        boolean redisLoginSuccess = false;

        // 商家使用邮箱登录 使用邮箱作为redis键的一部分
        String infoKey = "merchant:info:" + email;
        if(jedis.exists(infoKey)){
            // 表示redis缓存中有对应的用户的信息 直接使用缓存 不需要查询数据库
            LOG.info("MerchantController -> 使用redis缓存获取到信息");
            if(!jedis.hget(infoKey, "email").equals(email) || !jedis.hget(infoKey, "password").equals(SHA.getResult(password))){
                LOG.error("MerchantController -> 用户名或密码不正确");
                throw new CommonException(ResultCode.AUTH_FAILED);
            }
            // 登录成功
            LOG.info("MerchantController -> 使用缓存登录成功");
            redisLoginSuccess = true;
        }

        Merchant merchant = null;
        if(!redisLoginSuccess){
            // 如果没有使用redis登录 表示缓存中还没有数据 所以使用数据库进行查询登录
            // 如果参数不为空 使用merchantService进行登录认证
            merchant = merchantService.login(email, password);
            jedis.hset(infoKey, "email", merchant.getEmail());
            jedis.hset(infoKey, "password", merchant.getPassword());
            jedis.hset(infoKey, "name", merchant.getName());
            jedis.hset(infoKey, "merchantId", merchant.getMerchantId().toString());
        }else{
            merchant = new Merchant();
            merchant.setMerchantId(Integer.valueOf(jedis.hget(infoKey, "merchantId")));
            merchant.setEmail(jedis.hget(infoKey, "email"));
            merchant.setName(jedis.hget(infoKey, "name"));
            merchant.setPassword(jedis.hget(infoKey, "password"));
        }
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
        String key = "session:merchant:" + email;
        String s = jedis.get(key);
        // 如果没有sessionId才创建cookie 否则不创建cookie
        if(s == null){
            LOG.info("商家登录，创建cookie");
            MerchantVO merchantVO = convertFromMerchant(merchant);
            jedis.setex(key, 60, httpServletRequest.getSession().getId());
            jedis.setex(httpServletRequest.getSession().getId(), 60, SerializeUtil.serialize(merchantVO));
            Cookie cookie = new Cookie("JSESSIONID", httpServletRequest.getSession().getId());
            cookie.setMaxAge(60);
            httpServletResponse.addCookie(cookie);
        }else{
            LOG.error("不可以重复登录");
            throw new CommonException(ResultCode.IS_LOGINED);
        }

        return CommonReturnType.success();
    }

    @ApiOperation("商家修改密码")
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家email", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "newPass", value = "商家新密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "oldPass", value = "商家旧密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "商家确认密码", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType modifyPass(String email, String oldPass, String newPass, String qualifyPass) throws CommonException {
        // 首先校验参数是否为空
        if(email == null || email.trim().length() == 0 || oldPass == null || oldPass.trim().length() == 0
            || newPass == null || newPass.trim().length() == 0
            || qualifyPass == null || qualifyPass.trim().length() == 0){
            LOG.error("MerchantController -> 商家修改密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 验证两次输入的密码是否一致
        if(!newPass.equals(qualifyPass)){
            LOG.error("MerchantController -> 两次输入密码不一致");
            throw new CommonException(ResultCode.PASSWORD_NOT_EQUAL);
        }

        merchantService.modifyPass(email, oldPass, newPass, qualifyPass);

        // 修改密码后 商家信息更改 所以缓存中的信息 已经不同步 需要删除redis中对应的键值
        // 需要先判断这个键是否存在
        String key = "merchant:info:" + email;
        if(jedis.exists(key)){
            // 如果存在 再删除
            jedis.del(key);
        }

        return CommonReturnType.success();
    }

    @ApiOperation("商家找回密码")
    @RequestMapping(value = "/getbackPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType getbackPass(String email, Integer merchantId) throws CommonException {
        //首先校验参数是否为空
        if(email == null || email.trim().length() == 0 ||
                merchantId == null){
            LOG.error("MerchantController -> 商家找回密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        merchantService.getbackPass(email, merchantId);

        return CommonReturnType.success();
    }

    @ApiOperation("注册商家")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "商家密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "确认密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType register(String password, String qualifyPass, String code, String email) throws CommonException {
        if(!password.equals(qualifyPass)){
            LOG.error("MerchantController ->  商家注册 -> 两次输入密码不一致");
            throw new CommonException(ResultCode.PASSWORD_NOT_EQUAL);
        }
        if(password == null || password.trim().length() == 0
            || email == null || email.trim().length() == 0){
            LOG.error("MerchantController -> 商家注册 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        String s = jedis.get(email);
        if(s == null){
            LOG.error("MerchantController -> 商家注册 -> 验证码过期");
            throw new CommonException(ResultCode.CODE_IS_EXPIRED);
        }
        if(!s.equals(code)){
            LOG.error("MerchantController -> 商家注册 -> 验证码不正确");
            throw new CommonException(ResultCode.CODE_IS_NOT_RIGHT);
        }
        Merchant merchant = merchantService.register(SHA.getResult(password), email);
        MerchantVO merchantVO = convertFromMerchant(merchant);
        return CommonReturnType.success(merchantVO);
    }

    @ApiOperation("商家获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getCode(String email) throws CommonException {
        String code = merchantService.getCode(email);
        jedis.setex(email, 180, code);
        return CommonReturnType.success();
    }

    @ApiOperation("商家退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "商家邮箱", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType logout(String email) throws CommonException{
        if(email == null || email.trim().length() == 0){
            LOG.error("MerchantController -> 商家退出登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        String key = "session:merchant:" + email;
        String s = jedis.get(key);
        if(s == null){
            LOG.info("MerchantController -> 商家退出登录 -> 用户身份已经失效,退出成功");
            return CommonReturnType.success();
        }
        // 如果用户身份还没有失效 退出登录后 删除对应的sessionId
        LOG.info("MerchantController -> 商家退出登录 -> 退出登录成功");
        jedis.del(key);
        // 还要删除商家序列化的信息
        jedis.del(s);
        return CommonReturnType.success();
    }

    @ApiOperation("商家获取自己所有订单")
    @GetMapping("/getAllOrders")
    @ResponseBody
    public CommonReturnType getAllOrders() throws CommonException, IOException, ClassNotFoundException {
        Integer merchantId = null;
        try{
            // 先通过sessionId获取对应的用户邮箱
            merchantId = searchMerchantIdBySessionId();
        }catch (CommonException e){
            throw new CommonException(e);
        }
        List<Order> orderList = orderService.getAllOrderByMerchantId(merchantId);
        List<OrderVO> orderVOS = convertFromOrderList(orderList);
        // 直接返回结果
        return CommonReturnType.success(orderVOS);
    }

    @ApiOperation("商家完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/completeOrder")
    @ResponseBody
    public CommonReturnType completeOrder(Integer orderId) throws CommonException {
        // 先检查参数是否为空
        if(orderId == null){
            LOG.error("MerchantController -> completeOrder -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 然后检查对应order是否已完成
        orderService.judgeOrderCompleted(orderId);
        // 如果都没有错 再修改对应订单的完成状态
        orderService.completeOrder(orderId);
        return CommonReturnType.success();
    }

    private MerchantVO convertFromMerchant(Merchant merchant){
        if(merchant == null){
            return null;
        }
        MerchantVO merchantVO = new MerchantVO();
        BeanUtils.copyProperties(merchant, merchantVO);
        return merchantVO;
    }

    private Integer searchMerchantIdBySessionId() throws CommonException {
        Integer merchantId = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("JSESSIONID")){
                String sessionId = cookie.getValue();
                if(jedis.exists(sessionId)){
                    // redis缓存中存在对应sessionId的键
                    String objectStr = jedis.get(sessionId);
                    try {
                        Object object = SerializeUtil.serializeToObject(objectStr);
                        if(object instanceof MerchantVO){
                            MerchantVO merchantVO = (MerchantVO)object;
                            LOG.info("MerchantController -> 反序列化的对象为MerchantVO对象");
                            merchantId = merchantVO.getMerchantId();
                            break;
                        }else{
                            // 表示不是商家 所以没有权限
                            LOG.error("MerchantController -> 反序列化对象不是MerchantVO对象,没有权限执行此操作");
                            throw new CommonException(ResultCode.HAVE_NOT_ACCESS);
                        }
                    } catch (ClassNotFoundException | IOException | CommonException e) {
                        e.printStackTrace();
                    }
                }else{
                    // 没有对应的键 表示身份认证过期
                    LOG.error("MerchantController -> 身份认证过期");
                    throw new CommonException(ResultCode.AUTH_EXPIRED);
                }
            }
        }
        return merchantId;
    }

    private List<OrderVO> convertFromOrderList(List<Order> orders){
        if(orders == null){
            return null;
        }
        List<OrderVO> orderVOS = new ArrayList<>();
        for(Order order : orders){
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }
}
