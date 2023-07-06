package com.bessie.controller;

import com.bessie.base.BaseInfoProperties;
import com.bessie.grace.result.GraceJsonResult;
import com.bessie.pojo.bo.CreateAdminBO;
import com.bessie.service.AdminService;
import com.bessie.utils.PagedGridResult;
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

    @PostMapping("list")
    public GraceJsonResult list(String accountName,
                                Integer page,
                                Integer limit) {

        if (page == null) page = 1;
        if (limit == null) page = 10;

        PagedGridResult listResult = adminService.getAdminList(accountName,
                page,
                limit);

        return GraceJsonResult.ok(listResult);
    }

    @PostMapping("delete")
    public GraceJsonResult delete(String username) {
        adminService.deleteAdmin(username);
        return GraceJsonResult.ok();
    }

}