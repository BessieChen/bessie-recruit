package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.pojo.bo.CreateAdminBO;
import com.bessie.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: bessie-recruit-dev
 * @description: admin
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("admininfo")
@Slf4j
public class AdminInfoController extends BaseInfoProperties {

    @Autowired
    private AdminService adminService;

    @PostMapping("create")
    public GraceJsonResult create(@Valid @RequestBody CreateAdminBO createAdminBO) {
        adminService.createAdmin(createAdminBO);
        return GraceJsonResult.ok();
    }

}