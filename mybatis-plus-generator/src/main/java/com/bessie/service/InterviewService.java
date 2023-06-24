package com.bessie.service;

import com.bessie.pojo.Interview;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 面试邀约表
本表为次表，可做冗余，可以用mongo或者es替代 服务类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
public interface InterviewService extends IService<Interview> {

}
