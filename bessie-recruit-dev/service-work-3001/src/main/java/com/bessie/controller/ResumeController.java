package com.bessie.controller;

import com.bessie.grace.result.GraceJsonResult;
import com.bessie.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 初始化用户简历
     * @param userId
     * @return
     */
    @PostMapping("init")
    public GraceJsonResult init(@RequestParam("userId") String userId) {
        resumeService.initResume(userId);
        return GraceJsonResult.ok();
    }

}
