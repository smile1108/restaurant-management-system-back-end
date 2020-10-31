package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.UserService;
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
        User user = null;
        // 检验输入的是邮箱还是学号
        if (pattern.matcher(id).matches()) {
            LOG.info("用户使用邮箱登录");
            // 表示输入的是邮箱
            user = userService.loginByEmail(id, password);
        } else {
            LOG.info("用户使用学号登录");
            // 表示用学号登录
            user = userService.loginById(id, password);
        }
        String userEmail = user.getEmail();
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
        String s = jedis.get(key);

        // 如果没有sessionId才创建cookie 否则不创建cookie
        if(s == null){
            LOG.info("用户登录，创建cookie");
            UserVO userVO = convertFromUserDO(user);
            jedis.setex(key, 60, httpServletRequest.getSession().getId());
            jedis.setex(httpServletRequest.getSession().getId(), 60, SerializeUtil.serialize(userVO));
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
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "newPass", value = "用户新密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "oldPass", value = "用户旧密码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "qualifyPass", value = "用户确认密码", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType modifyPass(String id, String oldPass, String newPass, String qualifyPass) throws CommonException {
        //首先验证参数是否为空
        if (id == null || id.trim().length() == 0 || oldPass == null || oldPass.trim().length() == 0
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
        userService.modifyPass(id, oldPass, newPass, qualifyPass);

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
    @ResponseBody
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
    @ResponseBody
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

    private UserVO convertFromUserDO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
