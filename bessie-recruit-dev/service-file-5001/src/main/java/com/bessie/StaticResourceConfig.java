package com.bessie;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @program: bessie-recruit-dev
 * @description: 静态资源
 * @author: Bessie
 * @create: 2023-07-14 09:21
 **/

@Configuration
public class StaticResourceConfig extends WebMvcConfigurationSupport {

    /**
     * 添加静态资源映射的路径，css/js/html等都可以放在其中进行虚拟化
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        /**
         * addResourceHandler: 指的对外暴露的访问路径
         * addResourceLocations: 指的是文件配置的本地系统目录
         */

        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:/Users/bessie/");

        super.addResourceHandlers(registry);
    }
}
