package com.bessie.controller;

import com.bessie.api.intercept.JWTCurrentUserInterceptor;
import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.pojo.Admin;
import com.bessie.pojo.Users;
import com.bessie.pojo.bo.AdminBO;
import com.bessie.pojo.bo.RegistLoginBO;
import com.bessie.pojo.vo.AdminVO;
import com.bessie.pojo.vo.UsersVO;
import com.bessie.service.AdminService;
import com.bessie.service.UsersService;
import com.bessie.utils.IPUtil;
import com.bessie.utils.JWTUtils;
import com.bessie.utils.SMSUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @program: bessie-recruit-dev
 * @description: admin
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController extends BaseInfoProperties {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("login")
    public GraceJsonResult getSMSCode(@Valid @RequestBody AdminBO adminBO){

        // 执行登录判断用户是否存在
        boolean isExist = adminService.adminLogin(adminBO);
        if (!isExist)
            return GraceJsonResult.errorCustom(
                    ResponseStatusEnum.ADMIN_LOGIN_ERROR);

        // 登录成功之后获得admin信息
        Admin admin = adminService.getAdminInfo(adminBO);
        String adminToken = jwtUtils.createJWTWithPrefix(new Gson().toJson(admin),
                TOKEN_ADMIN_PREFIX);

        return GraceJsonResult.ok(adminToken);
    }

    @PostMapping("logout")
    public GraceJsonResult logout() {
        return GraceJsonResult.ok();
    }

    @GetMapping("info")
    public GraceJsonResult info(String token) {
        Admin admin = JWTCurrentUserInterceptor.adminUser.get();
        System.out.println(admin);

        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);
        return GraceJsonResult.ok(adminVO);
    }
}