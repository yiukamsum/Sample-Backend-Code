package com.example.demo;

import com.example.demo.interceptor.AuthInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new LogInterceptor());
        // registry.addInterceptor(new OldLoginInterceptor()).addPathPatterns("/admin/oldLogin");
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/*")
            .excludePathPatterns("/login");
    }
}
