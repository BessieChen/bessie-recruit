package com.bessie.service.impl;

import com.bessie.base.BaseInfoProperties;
import com.bessie.exceptions.GraceException;
import com.bessie.grace.result.ResponseStatusEnum;
import com.bessie.mapper.UsersMapper;
import com.bessie.pojo.Users;
import com.bessie.pojo.bo.ModifyUserBO;
import com.bessie.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends BaseInfoProperties implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional
    @Override
    public void modifyUserInfo(ModifyUserBO userBO) {

        String userId = userBO.getUserId();
        if (StringUtils.isBlank(userId))
            GraceException.doException(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);

        Users pendingUser = new Users();
        pendingUser.setId(userId);
        pendingUser.setUpdatedTime(LocalDateTime.now());

        BeanUtils.copyProperties(userBO, pendingUser);

        usersMapper.updateById(pendingUser);
    }

    @Override
    public Users getById(String uid) {
        return usersMapper.selectById(uid);
    }
}
