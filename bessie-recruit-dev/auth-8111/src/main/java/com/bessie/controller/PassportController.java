package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.pojo.Users;
import com.bessie.pojo.bo.RegistLoginBO;
import com.bessie.pojo.vo.UsersVO;
import com.bessie.service.UsersService;
import com.bessie.utils.IPUtil;
import com.bessie.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

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

    @Autowired
    private UsersService usersService;

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

    @PostMapping("login")
    public GraceJsonResult login(@Valid @RequestBody RegistLoginBO registLoginBO,
                                 HttpServletRequest request) throws Exception {

        String mobile = registLoginBO.getMobile();
        String code = registLoginBO.getSmsCode();


        // 1. 从redis中获得验证码进行校验是否匹配
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2. 查询数据库，判断用户是否存在
        Users user = usersService.queryMobileIsExist(mobile);
        if (user == null) {
            // 2.1 如果用户为空，表示没有注册过，则为null，需要注册信息入库
            user = usersService.createUser(mobile);
        }

        // 3. 如果不为空，可以继续下方业务，可以保存用户会话信息
        String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);


        // 4. 用户登录注册成功以后，删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 5. 返回用户信息，包含token令牌
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(uToken);


        // 4. 返回用户信息
        return GraceJsonResult.ok(usersVO); //GraceJsonResult.ok(user);
    }

    @PostMapping("logout")
    public GraceJsonResult logout(@RequestParam String userId,
                                  HttpServletRequest request) throws Exception {

        // 后端只需要清除用户的token信息即可，前端也需要清除相关的用户信息
        redis.del(REDIS_USER_TOKEN + ":" + userId);

        return GraceJsonResult.ok();
    }

}