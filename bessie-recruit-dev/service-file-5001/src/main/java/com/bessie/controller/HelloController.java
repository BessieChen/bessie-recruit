package com.bessie.controller;

import com.bessie.grace.result.GraceJsonResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @program: bessie-recruit-dev
 * @description: hello world test
 * @author: Bessie
 * @create: 2023-06-21 16:39
 **/
@RestController
@RequestMapping("file")
public class HelloController {

    //www.bessie-recruit.com
    public static final String host = "http://192.168.31.208:8000/"; //注意每次重启macos都要修改啊

    @GetMapping("hello")
    public Object hello(){
        return "Hello File Service from bessie";
    }

    @PostMapping("uploadFace")
    public GraceJsonResult uploadFace(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") String userId,
                                       HttpServletRequest request) throws Exception {

        // 获得文件原始名称
        String filename = file.getOriginalFilename();

        //"abc.123.abc.png"
        // 根据文件名中最后一个点的位置向后进行截取
        String suffixName = filename.substring(filename.lastIndexOf("."));

        // 文件新的名称
        String newFileName = userId + suffixName;

        // 设置文件存储的路径，可以存放在指定的路径中，windows用户需要修改为对应的盘符
        String rootPath = "/Users/bessie" + File.separator;
        // 图片存储的完全路径
        String filePath = rootPath + File.separator + "faceFromBessieRecruit" + File.separator + newFileName;

        File newFile = new File(filePath);
        if (!newFile.getParentFile().exists()) {
            // 如果目标文件所在目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }

        // 将内存中的文件数据写入到磁盘
        file.transferTo(newFile);

        // 生成web可以被访问的url地址
        String userFaceUrl = host + "static/faceFromBessieRecruit/" + newFileName;

        return GraceJsonResult.ok(userFaceUrl);
    }
}
