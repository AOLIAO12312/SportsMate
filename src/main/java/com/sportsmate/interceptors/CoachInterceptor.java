package com.sportsmate.interceptors;

import com.sportsmate.pojo.UserType;
import com.sportsmate.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class CoachInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 ThreadLocal 获取登录用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();

        if (claims == null || claims.get("userType") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        UserType type = UserType.valueOf((String) claims.get("userType"));
        if (!type.equals(UserType.教练)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 无权限
            return false;
        }

        return true;
    }
}
