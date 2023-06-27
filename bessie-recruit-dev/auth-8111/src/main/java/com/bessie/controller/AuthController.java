package com.bessie.controller;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("a")
@Slf4j
public class AuthController {

    @GetMapping("hello")
    public Object hello(){
        log.error("hi");
        return "Hello, Auth Controller";
    }

}