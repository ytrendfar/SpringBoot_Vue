package com.young.config;

import com.young.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置项
 */
@Configuration      //所有配置类都需要添加configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")            //拦截所有请求，通过判断token是否合法来决定是否允许登录
                .excludePathPatterns("/user/login","/user/register","/*/export","/*/import");     //放行注册登录等路径
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
