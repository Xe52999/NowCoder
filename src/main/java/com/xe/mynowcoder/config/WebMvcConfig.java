package com.xe.mynowcoder.config;

import com.xe.mynowcoder.controller.interceptor.AlphaInterceptor;
import com.xe.mynowcoder.controller.interceptor.LoginRequiredInterceptor;
import com.xe.mynowcoder.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xjx
 * @DATE 2022/8/16
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //配置拦截器
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //排除所有的静态资源
        //addPathPatterns 明确拦截的路径
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login");

        //拦截所有请求
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        //拦截所有请求
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
