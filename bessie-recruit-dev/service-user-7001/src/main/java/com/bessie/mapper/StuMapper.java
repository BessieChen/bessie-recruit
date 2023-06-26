package com.bessie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bessie.pojo.Stu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
@Repository
public interface StuMapper extends BaseMapper<Stu> {

}
