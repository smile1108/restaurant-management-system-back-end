package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.controller.VO.AdminVO;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.utils.SerializeUtil;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.io.IOException;

/**
 * FileName: SessionController
 * Author: Jiac
 * Date: 2020/11/12 14:45
 */
@Api(value = "自动登录controller", description = "自动登录获取信息")
@RestController
@RequestMapping("/api/dbcourse")
public class SessionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private Jedis jedis;

    @ApiOperation("自动获取信息")
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public CommonReturnType getMessage() throws CommonException, IOException, ClassNotFoundException {
        // 首先获得cookie
        Cookie[] cookies = httpServletRequest.getCookies();
        // 然后找到cookie中对应的JSESSIONID
        String sessionId = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("JSESSIONID")){
                // 表示找到了对应的sessionId 赋值为sessionId
                sessionId = cookie.getValue();
                break;
            }
        }
        // 判断sessionId是否为空 如果为空 表示请求没有sessionId 则认为身份认证已过期
        if(sessionId == null){
            LOG.error("SessionController -> getMessage -> 身份验证过期,请重新登录");
            throw new CommonException(ResultCode.AUTH_EXPIRED);
        }
        // 没有抛出异常就代表身份认证成功 要去redis中找对应的信息 返回给前端
        if(!jedis.exists(sessionId)){
            // 如果jedis中不存在对应的sessionId 表示对应的身份信息也已经过期 需要重新登录
            LOG.error("SessionController -> getMessage -> redis中没有对应的sessionId,身份认证已经过期,请重新登录");
            throw new CommonException(ResultCode.AUTH_EXPIRED);
        }
        // 到这表示redis中也存在对应的键 根据键反序列化出对应的对象 传递给前端
        Object object = SerializeUtil.serializeToObject(jedis.get(sessionId));
        // 然后对object的类型进行判断
        if(object instanceof UserVO){
            UserVO userVO = (UserVO) object;
            LOG.info("SessionController -> getMessage -> 找到对应的用户信息");
            return CommonReturnType.success(userVO);
        }else if(object instanceof MerchantVO){
            MerchantVO merchantVO = (MerchantVO) object;
            LOG.info("SessionController -> getMessage -> 找到对应的商家信息");
            return CommonReturnType.success(merchantVO);
        }else{
            AdminVO adminVO = (AdminVO) object;
            LOG.info("SessionController -> getMessage -> 找到对应的管理员信息");
            return CommonReturnType.success(adminVO);
        }
    }

}
