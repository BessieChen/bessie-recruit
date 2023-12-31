package com.bessie.api;

import com.bessie.api.intercept.JWTCurrentUserInterceptor;
import com.bessie.api.intercept.SMSInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: bessie-recruit-dev
 * @description: * 拦截器的配置类，用于注册和生效拦截器
 * @author: Bessie
 * @create: 2023-06-28 17:39
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 在springboot容器中放入拦截器
     * @return
     */
    @Bean
    public SMSInterceptor smsInterceptor() {
        return new SMSInterceptor();
    }

    @Bean
    public JWTCurrentUserInterceptor jwtCurrentUserInterceptor() {
        return new JWTCurrentUserInterceptor();
    }

    /**
     * 注册拦截器，并且拦截指定的路由，否则不生效
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor())
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(jwtCurrentUserInterceptor())
                .addPathPatterns("/**");
    }
}