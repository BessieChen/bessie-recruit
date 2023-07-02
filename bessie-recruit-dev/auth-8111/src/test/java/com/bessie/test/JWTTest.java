package com.bessie.test;

import com.bessie.pojo.test.Stu;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-02 10:37
 **/
@SpringBootTest
public class JWTTest {

    // 定义秘钥，提供给jwt加密解密，一般都是由开发者或者公司定的规范，建议32位
    public static final String USER_KEY = "bessie_123456789_123456789";

    @Test
    public void createJWT() {

        // 1. 对秘钥进行base64编码
        String base64 = new BASE64Encoder().encode(USER_KEY.getBytes());

        // 2. 对base64生成一个秘钥的对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());

        // 3. 通过jwt去生成一个token字符串
        Stu stu = new Stu("bessie-recruit", 1001 , 18);
        String stuJson = new Gson().toJson(stu);

        String myJWT = Jwts.builder()
                .setSubject(stuJson)         // 设置用户自定义数据
                .signWith(secretKey)    // 使用哪个秘钥对象进行jwt的生成
                .compact();             // 压缩并且生成jwt

        System.out.println(myJWT);
    }

}
