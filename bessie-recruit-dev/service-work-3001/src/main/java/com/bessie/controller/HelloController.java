package com.bessie.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bessie-recruit-dev
 * @description: hello world test
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("w")
public class HelloController {
    @GetMapping("hello")
    public Object hello(){
        return "Hello, this is work controller";
    }

    @Value("${server.port}")
    private String port;
    @GetMapping("port")
    public Object port(){
        return "port" + port;
    }
}
