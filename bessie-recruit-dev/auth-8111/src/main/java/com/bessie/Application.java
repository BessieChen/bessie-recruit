package com.bessie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-06-21 16:45
 **/
@SpringBootApplication      //@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableDiscoveryClient      // 开启服务注册与发现功能
@MapperScan(basePackages = "com.bessie.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
