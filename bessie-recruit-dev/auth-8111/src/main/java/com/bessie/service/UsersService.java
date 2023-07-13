package com.bessie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bessie.pojo.Users;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
public interface UsersService extends IService<Users> {

    /**
     * 判断用户是否存在，如果存在则返回用户信息
     */
    public Users queryMobileIsExist(String mobile); //find mobile

    /**
     * 创建用户信息，并且返回用户对象
     */
    public Users createUsers(String mobile);

    /**
     * 消息的一致性
     * @param mobile
     * @return
     */
    public Users createUsersAndInitResumeMQ(String mobile);

}
