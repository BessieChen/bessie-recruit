package com.bessie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bessie.api.feign.WorkMicroServiceFeign;
import com.bessie.api.mq.InitResumeMQConfig;
import com.bessie.enums.Sex;
import com.bessie.enums.ShowWhichName;
import com.bessie.enums.UserRole;
import com.bessie.mapper.UsersMapper;
import com.bessie.mq.InitResumeMQProducerHandler;
import com.bessie.pojo.Users;
import com.bessie.service.UsersService;
import com.bessie.utils.DesensitizationUtil;
import com.bessie.utils.LocalDateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private WorkMicroServiceFeign workMicroServiceFeign;

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

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Autowired
    public InitResumeMQProducerHandler producerHandler;

    @Transactional
    @Override
    public Users createUsersAndInitResumeMQ(String mobile) {

        // 创建用户
        Users user = createUsers(mobile);

        // 通过消息助手类进行本地消息的存储
        producerHandler.saveLocalMsg(
                InitResumeMQConfig.INIT_RESUME_EXCHANGE,
                InitResumeMQConfig.ROUTING_KEY_INIT_RESUME,
                user.getId());


        // 发送消息，初始化简历
//        rabbitTemplate.convertAndSend(
//                InitResumeMQConfig.INIT_RESUME_EXCHANGE,
//                InitResumeMQConfig.ROUTING_KEY_INIT_RESUME,
//                user.getId());

        return user;
    }


    @Transactional
//    @GlobalTransactional
    @Override
    public Users createUsers(String mobile) {

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

        // 发起远程调用，初始化用户简历，新增一条空记录
//        GraceJsonResult graceJsonResult = workMicroServiceFeign.init(user.getId());
//        if (graceJsonResult.getStatus() != 200) {
//            // 如果调用状态不是200，则手动回滚全局事务
//            String xid = RootContext.getXID();
//            if (StringUtils.isNotBlank(xid)) {
//
//                try {
//                    GlobalTransactionContext.reload(xid).rollback();
//                } catch (TransactionException e) {
//                    e.printStackTrace();
//                } finally {
//                    GraceException.doException(ResponseStatusEnum.USER_REGISTER_ERROR);
//                }
//            }
//        }

        //int a = 1 / 0;

        return user;
    }
}
