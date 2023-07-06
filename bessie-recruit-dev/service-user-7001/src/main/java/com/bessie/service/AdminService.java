package com.bessie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bessie.pojo.Admin;
import com.bessie.pojo.bo.CreateAdminBO;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
public interface AdminService extends IService<Admin> {

    /**
     * 创建admin账号
     * @param createAdminBO
     */
    public void createAdmin(CreateAdminBO createAdminBO);

}
