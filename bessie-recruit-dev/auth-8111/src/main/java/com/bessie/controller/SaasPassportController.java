package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("saas")
@Slf4j
public class SaasPassportController extends BaseInfoProperties {

    /**
     * 1. 获得二维码token令牌
     * @return
     */
    @PostMapping("getQRToken")
    public GraceJsonResult getQRToken() {

        // 生成扫码登录的token
        String qrToken = UUID.randomUUID().toString();
        // 把qrToken存入到redis，设置一定时效，默认二维码超时，则需要刷新后再次获得新的二维码
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken,  qrToken, 5*60);
        // 存入redis标记当前的qrToken未被扫描读取
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken,  "0", 5*60);
        log.warn(qrToken);

        //返回给前端，让前端下一次请求的时候需要带上qrToken
        return GraceJsonResult.ok(qrToken);
    }
}
