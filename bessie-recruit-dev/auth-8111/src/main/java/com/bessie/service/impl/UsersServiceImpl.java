package com.bessie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bessie.mapper.UsersMapper;
import com.bessie.pojo.Users;
import com.bessie.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
