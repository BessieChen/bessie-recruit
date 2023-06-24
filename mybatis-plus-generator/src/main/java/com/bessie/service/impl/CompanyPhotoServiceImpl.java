package com.bessie.service.impl;

import com.bessie.pojo.CompanyPhoto;
import com.bessie.mapper.CompanyPhotoMapper;
import com.bessie.service.CompanyPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业相册表，本表只存企业上传的图片 服务实现类
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */
@Service
public class CompanyPhotoServiceImpl extends ServiceImpl<CompanyPhotoMapper, CompanyPhoto> implements CompanyPhotoService {

}
