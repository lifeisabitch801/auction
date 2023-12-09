package com.yxq.auction.web;

import com.yxq.auction.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handle) throws IOException {
        String token = request.getHeader("token");
        if(token !=null && TokenUtil.verify(token)){
            return true;
        }else{
            response.getWriter().println("{\"code\":401}");
            //response.sendError(401,"鉴权失败");
            return false;
        }
    }
}
