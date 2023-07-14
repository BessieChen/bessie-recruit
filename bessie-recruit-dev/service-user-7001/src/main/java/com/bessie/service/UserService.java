package com.bessie.service;

import com.bessie.pojo.Users;
import com.bessie.pojo.bo.ModifyUserBO;

public interface UserService {

    /**
     * 修改用户信息
     * @param userBO
     */
    public void modifyUserInfo(ModifyUserBO userBO);

    /**
     * 获得用户信息
     * @param uid
     * @return
     */
    public Users getById(String uid);

}
