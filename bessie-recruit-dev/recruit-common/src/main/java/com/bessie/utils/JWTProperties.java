package com.bessie.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-06-27 23:13
 **/
@Component
@Data
@PropertySource("classpath:jwt.properties")
@ConfigurationProperties(prefix = "auth")
public class JWTProperties {
    private String key;
}
