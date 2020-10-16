package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.User;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * FileName: UserController
 * Author: Jiac
 * Date: 2020/10/9 9:10
 */
@Api(value = "用户controller", description = "用户操作")
@RestController
@RequestMapping("/api/dbcourse/user")
public class UserController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private JedisPool jedisPool;


    @ApiOperation("用户登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true ),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "body", required = true)
    })
    @ResponseBody
    public CommonReturnType login(String id, String password) throws CommonException {
        // 首先验证参数是否为空
        if(id == null || id.trim().length() == 0 || password == null || password.trim().length() == 0){
            LOG.error("UserController -> 用户登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 如果参数验证成功 就调用service查询数据库
        User user = userService.login(id, password);

        // 将DO模型转换为前端用户看的模型
        UserVO userVO = convertFromUserDO(user);

        // 没有问题 返回用户数据
        return CommonReturnType.success(userVO);
    }

    @ApiOperation("用户修改密码")
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true),
        @ApiImplicitParam(name = "newPass", value = "用户新密码", dataType = "string", paramType = "body", required = true),
        @ApiImplicitParam(name = "qualifyPass", value = "用户确认密码", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType modifyPass(String id, String oldPass, String newPass, String qualifyPass) throws CommonException {
        //首先验证参数是否为空
        if(id == null || id.trim().length() == 0 || oldPass == null || oldPass.trim().length() == 0
            || newPass == null || newPass.trim().length() == 0 || qualifyPass == null || qualifyPass.trim().length() == 0){
            LOG.error("UserController -> 修改密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        // 验证两次输入的密码是否一致
        if(!newPass.equals(qualifyPass)){
            LOG.error("两次输入密码不一致");
            throw new CommonException(ResultCode.PASSWORD_NOT_EQUAL);
        }
        // 如果参数验证成功 就调用service查询数据库
        userService.modifyPass(id, oldPass, newPass, qualifyPass);

        return CommonReturnType.success();
    }

    @ApiOperation("用户找回密码")
    @RequestMapping(value = "/getbackPass", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "string", paramType = "body", required = true),
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "string", paramType = "body", required = true)
    })
    public CommonReturnType getbackPass(String email, String id) throws CommonException {
        //首先校验参数是否为空
        if(email == null || email.trim().length() == 0 ||
            id == null || id.trim().length() == 0){
            LOG.error("UserController -> 找回密码 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }

        userService.getbackPass(email, id);

        return CommonReturnType.success();
    }

    private UserVO convertFromUserDO(User user){
        if(user == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
