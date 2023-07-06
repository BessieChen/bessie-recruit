package com.bessie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bessie.pojo.Admin;
import com.bessie.pojo.bo.CreateAdminBO;
import com.bessie.utils.PagedGridResult;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
public interface AdminService {

    /**
     * 创建admin账号
     * @param createAdminBO
     */
    public void createAdmin(CreateAdminBO createAdminBO);

    /**
     * 查询admin列表
     * @param accountName
     * @param page
     * @param limit
     * @return
     */
    public PagedGridResult getAdminList(String accountName,
                                        Integer page,
                                        Integer limit);

}
