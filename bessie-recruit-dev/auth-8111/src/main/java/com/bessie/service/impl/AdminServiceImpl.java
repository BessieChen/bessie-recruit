package com.bessie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bessie.mapper.AdminMapper;
import com.bessie.pojo.Admin;
import com.bessie.pojo.bo.AdminBO;
import com.bessie.service.AdminService;
import com.bessie.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务实现类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean adminLogin(AdminBO adminBO) {

        // 根据用户名获得盐
        Admin admin = getSelfAdmin(adminBO.getUsername());

        // 如果为空，则直接返回false
        if (admin == null) {
            return false;
        } else {    // 如果不为空，则加盐判断用户登录
            String salt = admin.getSlat();
            String md5Str = MD5Utils.encrypt(adminBO.getPassword(), salt);
            if (md5Str.equalsIgnoreCase(admin.getPassword())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Admin getAdminInfo(AdminBO adminBO) {
        return this.getSelfAdmin(adminBO.getUsername());
    }

    private Admin getSelfAdmin(String username) {
        Admin admin = adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("username", username)
        );
        return admin;
    }
}
