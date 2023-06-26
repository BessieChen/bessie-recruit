package com.bessie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bessie.pojo.Stu;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
public interface StuService {

    public void save(Stu stu);

}

/**
 * public interface StuService extends IService<Stu> {
 *
 * }
 */