package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Admin;
import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.controller.VO.AdminVO;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.AdminService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * FileName: AdministratorController
 * Author: Jiac
 * Date: 2020/10/9 11:14
 */
@Api(value = "管理员controller", description = "管理员操作")
@RestController
@RequestMapping("/api/dbcourse/admin")
public class AdminController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private Jedis jedis;

    @Autowired
    private AdminService adminService;

    @ApiOperation("管理员登录验证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "管理员账号", dataType = "string", paramType = "query", required = true ),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "query", required = true)
    })
    public CommonReturnType login(String name, String password) throws CommonException, IOException {
        // 首先校验参数是否为空
        if(name == null || name.trim().length() == 0 || password == null || password.trim().length() == 0){
            LOG.error("AdministratorController -> 管理员登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }

        boolean redisLoginSuccess = false;

        // 使用登录用户名作为redis的key的一部分
        String infoKey = "admin:info:" + name;
        if(jedis.exists(infoKey)){
            // 表示redis缓存中有对应的用户的信息 直接使用缓存 不需要查询数据库
            LOG.info("AdminController -> 使用redis缓存获取到信息");
            if(!jedis.hget(infoKey, "username").equals(name) || !jedis.hget(infoKey, "password").equals(password)){
                LOG.error("AdminController -> 用户名或密码不正确");
                throw new CommonException(ResultCode.AUTH_FAILED);
            }
            // 登录成功
            LOG.info("AdminController -> 使用缓存登录成功");
            redisLoginSuccess = true;
        }

        // 如果参数不为空 使用adminService进行登录认证
        Admin admin = null;
        if(!redisLoginSuccess){
            admin = adminService.login(name, password);
            // 用户密码验证正确 加入redis缓存
            jedis.hset(infoKey, "administratorId", admin.getAdministratorId().toString());
            jedis.hset(infoKey, "username", admin.getUsername());
            jedis.hset(infoKey, "password", admin.getPassword());
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

        String key = "session:admin:" + name;
        String s = jedis.get(key);
        // 如果没有sessionId才创建cookie 否则不创建cookie
        if(s == null){
            LOG.info("管理员登录，创建cookie");
            AdminVO adminVO = convertFromAdmin(admin);
            jedis.setex(key, 60, httpServletRequest.getSession().getId());
            jedis.setex(httpServletRequest.getSession().getId(), 60, SerializeUtil.serialize(adminVO));
            Cookie cookie = new Cookie("JSESSIONID", httpServletRequest.getSession().getId());
            cookie.setMaxAge(60);
            httpServletResponse.addCookie(cookie);
        }else{
            LOG.error("不可以重复登录");
            throw new CommonException(ResultCode.IS_LOGINED);
        }

        return CommonReturnType.success();
    }

    @ApiOperation("管理员退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "管理员用户名", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType logout(String name) throws CommonException{
        if(name == null || name.trim().length() == 0){
            LOG.error("AdminController -> 管理员退出登录 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        String key = "session:admin:" + name;
        String s = jedis.get(key);
        if(s == null){
            LOG.info("AdminController -> 管理员退出登录 -> 用户身份已经失效,退出成功");
            return CommonReturnType.success();
        }
        // 如果用户身份还没有失效 退出登录后 删除对应的sessionId
        LOG.info("AdminController -> 管理员退出登录 -> 退出登录成功");
        jedis.del(key);
        return CommonReturnType.success();
    }

    private AdminVO convertFromAdmin(Admin admin){
        // 如果传递进来的对象为null 直接返回null
        if(admin == null){
            return null;
        }
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);
        return adminVO;
    }
}
