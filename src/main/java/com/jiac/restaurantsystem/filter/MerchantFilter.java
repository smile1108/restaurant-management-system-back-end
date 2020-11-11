package com.jiac.restaurantsystem.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiac.restaurantsystem.controller.VO.MerchantVO;
import com.jiac.restaurantsystem.controller.VO.UserVO;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.utils.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * FileName: MerchantFilter
 * Author: Jiac
 * Date: 2020/11/7 12:21
 */
@Order(4)
@WebFilter(urlPatterns = {"/api/dbcourse/merchant/getAllOrders", "/api/dbcourse/merchant/completeOrder"})
public class MerchantFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantFilter.class);

    @Autowired
    private Jedis jedis;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
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
                            LOG.info("MerchantFilter -> 反序列化的对象为MerchantVO对象");
                            filterChain.doFilter(servletRequest, servletResponse);
                            break;
                        }else{
                            LOG.error("MerchantFilter -> 没有权限访问");
                            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(ResultCode.HAVE_NOT_ACCESS));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    // 没有对应的键 表示身份认证过期
                    LOG.error("MerchantFilter -> 身份认证过期");
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(ResultCode.AUTH_EXPIRED));
                }
            }
        }
    }
}
