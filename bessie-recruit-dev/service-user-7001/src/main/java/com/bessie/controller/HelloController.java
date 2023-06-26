package com.bessie.controller;

import com.bessie.grace.result.GraceJsonResult;
import com.bessie.pojo.test.Stu;
import com.bessie.service.StuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: bessie-recruit-dev
 * @description: hello world test
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("u")
@Slf4j
public class HelloController {

    @Autowired
    private StuService stuService;

    @GetMapping("stu")
    public Object stu(){
        com.bessie.pojo.Stu stu = new com.bessie.pojo.Stu();
        stu.setAge(12);
//        stu.setId(13); id是主键, 通过 assign-id 配置
        stu.setName("bessie");

        stuService.save(stu); //保存到数据库中

        return "Hello, this is stu";
    }

    @GetMapping("hello")
    public Object hello(){
        Stu stu = new Stu();
        stu.setAge(12);
        Stu stu1 = new Stu("123", 123, 123);
        Stu stu2 = new Stu();

        log.info("info");
        log.debug("debug");
        log.warn("warn");
        log.error("error");

        return "Hello, this is Bessie recruit";
    }

    @GetMapping("hello2")
    public Object hello2(){
        Stu stu = new Stu("123", 123, 123);
        return GraceJsonResult.ok(stu);
//        return GraceJsonResult.errorCustom(ResponseStatusEnum.SYSTEM_IO);

    }

}