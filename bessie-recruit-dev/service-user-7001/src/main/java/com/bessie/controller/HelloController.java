package com.bessie.controller;

import com.bessie.pojo.Stu;
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
@RequestMapping("u")
public class HelloController {
    @GetMapping("hello")
    public Object hello(){
        Stu stu = new Stu();
        stu.setAge(12);
        System.out.println(stu.toString());

        Stu stu1 = new Stu("123", 123, 123);
        Stu stu2 = new Stu();

        return "Hello, this is Bessie recruit";
    }

}