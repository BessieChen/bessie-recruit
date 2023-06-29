package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.utils.IPUtil;
import com.bessie.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: bessie-recruit-dev
 * @description: hello world test
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @PostMapping("getSMSCode")
    public GraceJsonResult getSMSCode(String mobile, //@RequestParam String mobile,
                                      HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(mobile)) {
            return GraceJsonResult.error();
        }

        String userIp = IPUtil.getRequestIp(request); // 获得用户ip
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, "no matter what value");
            // 根据用户ip进行限制，限制用户在60秒之内只能获得一次验证码
            // 写锁, 只要还存在, 就返回对应的错误, 不执行后序的代码

        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        //smsUtils.sendSMS(mobile, code); //我们之前发成功了一次, 现在就不必再发了, 否则腾讯云把你禁了
        log.error("验证码为: {}", code);

        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);

        return GraceJsonResult.ok();
    }

}