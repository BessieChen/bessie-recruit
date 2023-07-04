package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.pojo.Users;
import com.bessie.pojo.vo.SaasUserVO;
import com.bessie.service.UsersService;
import com.bessie.utils.JWTUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("saas")
@Slf4j
public class SaasPassportController extends BaseInfoProperties {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UsersService usersService;

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

    /**
     * 2. 手机端HR角色扫码，扫码成功，判断用户id以及app的token是否OK，如果没问题，则写入redis准备登录的pre_token
     */
    @PostMapping("scanCode")
    public GraceJsonResult scanCode(String qrToken, HttpServletRequest request) {

        if (StringUtils.isBlank(qrToken))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FAILED);

        // 获得app端用户的id以及登录token
        String headerUserId = request.getHeader("appUserId");
        String headerUserToken = request.getHeader("appUserToken");

        System.out.println("headerUserId = " + headerUserId);
        System.out.println("headerUserToken = " + headerUserToken);  //这个不是 qrToken, 而是用户登录的时候获得的 token{jwt}


        if (StringUtils.isBlank(headerUserToken)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.HR_TICKET_INVALID);
        }

        // 校验JWT，判断app用户是否登录
        String userJson = jwtUtils.checkJWT(headerUserToken.split("@")[1]);
        //微服务可以使用全局异常的 trycatch
        if (StringUtils.isBlank(userJson)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.HR_TICKET_INVALID);
        }

        // 判断没问题继续执行
        // redis中存入等待确认的token，如果前端用户在手机端确认登录，则系统登录用户成功，否则撤销
        String preToken = UUID.randomUUID().toString();
        System.out.println("preToken = " + preToken);
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, preToken, 5*60); //pretoken覆盖qrtoken, qrtoken失效.
        //因为我们的业务逻辑就是扫一次码, 码就失效. 当然你业务逻辑不是这样的话, 就不用覆盖 qrtoken

        // redis写入标记，当前qrToken已经被app读取，网页需要展示是否扫码的标记，并且把preToken传给网页端
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken, "1," + preToken, 5*60);
        // 存入redis标记当前的qrToken未被扫描读取
        //redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken,  "0", 5*60);


        // 返回给手机端，让手机端下次请求的时候携带preToken
        return GraceJsonResult.ok(preToken);
    }

    /**
     * 3. SAAS平台首页每3秒定时查询qrToken是否被读取，用于页面展示的判断
     *  前端处理：限制用户在页面不操作而频繁发起调用：页面失效，请刷新后再执行扫描登录！
     *  注：如果使用websocket或者netty可以直接在上一个接口通信浏览器进行状态标记
     */
    @PostMapping("codeHasBeenRead")
    public GraceJsonResult codeHasBeenRead(String qrToken) {
        String readStr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);
        List list = new ArrayList();

        if (StringUtils.isNotBlank(readStr)) {

            String[] readArr = readStr.split(",");
            if (readArr.length >= 2) {
                list.add(Integer.valueOf(readArr[0])); //字符串"1" -> 数字1
                list.add(readArr[1]);   // preToken 让前端H5读取
            } else {
                list.add(0);
            }

            return GraceJsonResult.ok(list);
        } else {
            list.add(0);
            return GraceJsonResult.ok(list);
        }
    }

    /**
     * 4. 手机端点击确认登录，携带pre_token与后端判断，如果ok则登录成功
     * 注：如果使用websocket或netty，可以再次直接通信H5进行页面的跳转
     */
    @PostMapping("goQRLogin")
    public GraceJsonResult goQRLogin(String userId, String qrToken, String preToken) {
        String preTokenRedisArr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);

        if (StringUtils.isNotBlank(preTokenRedisArr)) {
            String preTokenRedis = preTokenRedisArr.split(",")[1];
            if (preTokenRedis.equalsIgnoreCase(preToken)) {
                // 根据userId获得用户信息
                Users saasUser = usersService.getById(userId);
                if (saasUser == null) {
                    return GraceJsonResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
                }

                // 存入信息到redis，因为H5在未登录的情况下，拿不到用户id，所以暂存用户信息。如果使用websocket通信则无此问题。
                redis.set(REDIS_SAAS_USER_INFO + ":temp:" + preToken, new Gson().toJson(saasUser), 5*60);
            }
        }
        return GraceJsonResult.ok();
    }

    /**
     * 5. SAAS端平台登录页面自动刷新
     */
    @PostMapping("checkLogin")
    public GraceJsonResult checkLogin(String preToken) {

        if (StringUtils.isBlank(preToken)) {
            return GraceJsonResult.ok("");
        }

        // 获得临时用户信息
        String userJson = redis.get(REDIS_SAAS_USER_INFO + ":temp:" + preToken);

        if (StringUtils.isBlank(userJson)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        // 确认登录，saas用户的token重新生成，并且长期有效
        String saasUserToken = jwtUtils.createJWTWithPrefix(userJson,
//                                                            Long.valueOf(8 * 60 * 60 * 1000),       // 8小时
                TOKEN_SAAS_PREFIX);
        redis.set(REDIS_SAAS_USER_INFO + ":" + saasUserToken, userJson);
        return GraceJsonResult.ok(saasUserToken);
    }

    /**
     * 6. 获得用户基本信息并且展示
     * @param token
     * @return
     */
    @GetMapping("info")
    public GraceJsonResult info(String token) {

        String saasUserToken = token;

        String userJson = redis.get(REDIS_SAAS_USER_INFO + ":" + saasUserToken);
        Users saasUser = new Gson().fromJson(userJson,  Users.class);

        SaasUserVO saasUserVO = new SaasUserVO();
        BeanUtils.copyProperties(saasUser, saasUserVO);
        return GraceJsonResult.ok(saasUserVO);
    }

    @PostMapping("logout")
    public GraceJsonResult logout(String token) throws Exception {
        return GraceJsonResult.ok();
    }
}
