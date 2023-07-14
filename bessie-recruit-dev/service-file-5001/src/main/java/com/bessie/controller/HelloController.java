package com.bessie.controller;

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
@RequestMapping("file")
public class HelloController {
    @GetMapping("hello")
    public Object hello(){
        return "Hello File Service from bessie";
    }
}
