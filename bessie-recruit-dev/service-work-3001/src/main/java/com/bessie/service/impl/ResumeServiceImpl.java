package com.bessie.service.impl;

import com.bessie.mapper.ResumeMapper;
import com.bessie.pojo.Resume;
import com.bessie.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Transactional
    @Override
    public void initResume(String userId) {

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdatedTime(LocalDateTime.now());

        //int a = 1 / 0;

        resumeMapper.insert(resume);
    }
}
