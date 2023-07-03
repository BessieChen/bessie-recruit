package com.bessie.controller;

import com.bessie.api.intercept.JWTCurrentUserInterceptor;
import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.pojo.Admin;
import com.bessie.pojo.Users;
import com.bessie.pojo.test.Stu;
import com.bessie.service.StuService;
import com.bessie.utils.MyInfo;
import com.bessie.utils.SMSUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.bessie.api.intercept.JWTCurrentUserInterceptor.adminUser;

/**
 * @program: bessie-recruit-dev
 * @description: hello world test
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("u")
@Slf4j
public class HelloController extends BaseInfoProperties {

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

    @Value("${server.port}")
    private String port;

    @GetMapping("hello")
    public Object hello(HttpServletRequest request){
        String userJson = request.getHeader(APP_USER_JSON); //controller 根据key来获得信息value
        Users jwtUser = new Gson().fromJson(userJson, Users.class);
        log.warn(jwtUser.toString()); //System.out.println(jwtUser);

        Users currentUser = JWTCurrentUserInterceptor.currentUser.get();
        log.warn("JWTCurrentUserInterceptor：" + currentUser.toString());

        //Admin adminUser = JWTCurrentUserInterceptor.adminUser.get();
        //log.info("JWTCurrentUserInterceptor：" + adminUser.toString());

        return "Hello, this is Bessie recruit";
    }

    @GetMapping("hello2")
    public Object hello2(){
        Stu stu = new Stu("123", 123, 123);
        return GraceJsonResult.ok(stu);
//        return GraceJsonResult.errorCustom(ResponseStatusEnum.SYSTEM_IO);

    }

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("sms")
    public Object sms() throws Exception {

        String code = "123456";
        smsUtils.sendSMS(MyInfo.getMobile(), code);

        return GraceJsonResult.ok();
    }

}