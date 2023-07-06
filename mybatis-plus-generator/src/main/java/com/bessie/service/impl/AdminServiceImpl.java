package com.bessie.service.impl;

import com.bessie.pojo.ar.AdminAR;
import com.bessie.mapper.AdminMapper;
import com.bessie.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务实现类
 * </p>
 *
 * @author bessie
 * @since 2023-07-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminAR> implements AdminService {

}
