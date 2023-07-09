package com.bessie.controller;

import com.bessie.api.mq.RabbitMQSMSConfig;
import com.bessie.api.task.SMSTask;
import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.pojo.Users;
import com.bessie.pojo.bo.RegistLoginBO;
import com.bessie.pojo.mq.SMSContentQO;
import com.bessie.pojo.vo.UsersVO;
import com.bessie.service.UsersService;
import com.bessie.utils.GsonUtils;
import com.bessie.utils.IPUtil;
import com.bessie.utils.JWTUtils;
import com.bessie.utils.SMSUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private SMSTask smsTask;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

        //smsTask.sendSMSTask(); //RetryComponent
        log.info("验证码为: {}", code);

        // 使用消息队列异步解耦发送短信
        SMSContentQO contentQO = new SMSContentQO();
        contentQO.setMobile(mobile);
        contentQO.setContent(code);

        // 定义confirm回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 回调函数
             * @param correlationData 相关性数据
             * @param ack 交换机是否成功接收到消息，true：成功
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData,
                                boolean ack,
                                String cause) {
                log.info("进入confirm");
                log.info("correlationData：{}", correlationData.getId());
                if (ack) {
                    log.info("交换机成功接收到消息~~ {}", cause);
                } else {
                    log.info("交换机接收消息失败~~失败原因： {}", cause);
                }
            }
        });

        // 定义return回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.info("进入return");
                log.info(returned.toString());
            }
        });

        for(int i = 0 ; i < 10; ++i){
            // 把短信内容和手机号构建为一个bean并且转换为json作为消息发送给mq
            rabbitTemplate.convertAndSend(
                    RabbitMQSMSConfig.SMS_EXCHANGE,                             //正确的交换机
//                RabbitMQSMSConfig.SMS_EXCHANGE + "123",                   //错误的交换机
                    RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN,               //正确的路由
//                "abc.123" + RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN, //错误的路由
                    GsonUtils.object2String(contentQO),
                    new CorrelationData(UUID.randomUUID().toString()));
        }

        // 消息属性处理的类对象（对当前需要的超时ttl进行参数属性的设置）
//        MessagePostProcessor processor = new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties()
//                        .setExpiration(String.valueOf(10*1000));
//                return message;
//            }
//        };
//        for(int i = 0 ; i < 10; ++i){
//            rabbitTemplate.convertAndSend(RabbitMQSMSConfig.SMS_EXCHANGE,
//                    RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN,
//                    GsonUtils.object2String(contentQO),
//                    message -> {
//                        message.getMessageProperties()
//                                .setExpiration(String.valueOf(30*1000));
//                        return message;
//                    },
//                    new CorrelationData(UUID.randomUUID().toString()));
//        }



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
        //String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID().toString();
        //redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
        String jwt = jwtUtils.createJWTWithPrefix(new Gson().toJson(user),
                                                    Long.valueOf(60 * 1000),
                                                    TOKEN_USER_PREFIX);


        // 4. 用户登录注册成功以后，删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 5. 返回用户信息，包含token令牌
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(jwt);          //usersVO.setUserToken(uToken);


        // 4. 返回用户信息
        return GraceJsonResult.ok(usersVO); //GraceJsonResult.ok(user);
    }

    @PostMapping("logout")
    public GraceJsonResult logout(@RequestParam String userId,
                                  HttpServletRequest request) throws Exception {

        // 后端只需要清除用户的token信息即可，前端也需要清除相关的用户信息
        // redis.del(REDIS_USER_TOKEN + ":" + userId);

        return GraceJsonResult.ok();
    }

}