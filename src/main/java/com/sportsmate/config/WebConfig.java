package com.sportsmate.config;

import com.sportsmate.interceptors.CoachInterceptor;
import com.sportsmate.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private CoachInterceptor coachInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录和注册接口不拦截
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login", "/user/register");

//        // 教练权限拦截器：仅拦截 /coach/** 路径
//        registry.addInterceptor(coachInterceptor)
//                .addPathPatterns("/coach/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("http://localhost:5173") // 允许前端地址（开发阶段）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // 如果你需要携带 cookie
                .maxAge(3600); // 预检请求缓存时间
    }
}
