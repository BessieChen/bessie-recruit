package com.bessie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bessie.enums.Sex;
import com.bessie.enums.ShowWhichName;
import com.bessie.enums.UserRole;
import com.bessie.mapper.UsersMapper;
import com.bessie.pojo.Users;
import com.bessie.service.UsersService;
import com.bessie.utils.DesensitizationUtil;
import com.bessie.utils.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Autowired
    private UsersMapper usersMapper;

    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";

    @Override
    public Users queryMobileIsExist(String mobile) {
        Users user = usersMapper.selectOne(
                new QueryWrapper<Users>()
                        .eq("mobile", mobile)
        );
        return user;
    }

    @Transactional
    @Override
    public Users createUser(String mobile) {

        Users user = new Users();

        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.nickname.type);

        user.setSex(Sex.secret.type);
        user.setFace(USER_FACE1);
        user.setEmail("");


        LocalDate localDateBirthday = LocalDateUtils.parseLocalDate("1900-01-01",
                                                                    LocalDateUtils.DATE_PATTERN);
        user.setBirthday(localDateBirthday);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");

        // 我参加工作的时间，默认为注册当天
        user.setStartWorkDate(LocalDate.now());
        user.setPosition("底层码农");
        user.setRole(UserRole.CANDIDATE.type);
        user.setHrInWhichCompanyId("");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        usersMapper.insert(user);

        return user;
    }
}
