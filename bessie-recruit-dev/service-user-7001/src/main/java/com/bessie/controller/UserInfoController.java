package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.pojo.Users;
import com.bessie.pojo.bo.ModifyUserBO;
import com.bessie.pojo.vo.UsersVO;
import com.bessie.service.UserService;
import com.bessie.utils.JWTUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-13 19:49
 **/
@RestController
@RequestMapping("userinfo")
@Slf4j
public class UserInfoController extends BaseInfoProperties {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("modify")
    public GraceJsonResult modify(@RequestBody ModifyUserBO userBO) throws Exception {

        // 修改用户信息
        userService.modifyUserInfo(userBO);

        // 返回最新用户信息
        UsersVO usersVO = getUserInfo(userBO.getUserId());

        return GraceJsonResult.ok(usersVO);
    }

    private UsersVO getUserInfo(String userId) {
        // 查询获得用户的最新信息
        Users latestUser = userService.getById(userId);

        // 重新生成并且覆盖原来的token
        String uToken = jwtUtils.createJWTWithPrefix(new Gson().toJson(latestUser),
                TOKEN_USER_PREFIX);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(latestUser, usersVO);
        usersVO.setUserToken(uToken);

        return usersVO;
    }
}