package com.jiac.restaurantsystem.filter;

import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.ResultCode;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * FileName: BaseFilter
 * Author: Jiac
 * Date: 2020/10/30 10:21
 */
@Order(2)
@WebFilter
public class InterceptFilter implements Filter {

    // 不需要登录就可以访问的路径
    String[] includeUrls = new String[]{"/user/login", "/user/register", "/user/getbackPass",
                                        "/merchant/login", "/merchant/register", "/merchant/getbackPass",
                                        "/admin/login"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String uri = httpServletRequest.getRequestURI();
        if(!needFilter(uri)){
            // 如果不需要过滤请求 直接交给filterChain
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            // 需要过滤
            // 查看请求的cookie中是否有sessionId
            Cookie[] cookies = httpServletRequest.getCookies();
            boolean intercept = false;
            if(cookies == null){
                // 如果cookies为空 表示没有cookie 当然就没有登录
                intercept = true;
            }else{
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("JSESSIONID")){
                        // 有sessionId 表示登录了 所以可以放行
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
            }
            // 如果没有sessionId 表示还没有登录 不能访问对应资源
            if(intercept){
                httpServletResponse.getWriter().write(ResultCode.IS_NOT_LOGIN.toString());
            }
        }
    }


    // 判断是否需要过滤请求的方法
    private boolean needFilter(String uri){
        for(String str : includeUrls){
            if(("/api/dbcourse" + str).equals(uri)){
                return false;
            }
        }

        return true;
    }
}
