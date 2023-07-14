package com.bessie;

import io.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-06-21 16:42
 **/

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SeataAutoConfiguration.class
})   //@SpringBootApplication
@EnableDiscoveryClient      // 开启服务注册与发现功能
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
