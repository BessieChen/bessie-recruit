package com.bessie.test;

import com.bessie.pojo.test.Stu;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
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

    @Test
    public void checkJWT() { //校验
        // 模拟假设从前端获得的jwt, 上节课生成的
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJuYW1lXCI6XCJiZXNzaWUtcmVjcnVpdFwiLFwiYWdlXCI6MTAwMSxcImlkXCI6MTh9In0.RvMV8lbcxRtakX-Mn7ImUszzMftZgLAEfpUcACCBg3A";

        //错误jwt
        //String jwt = "xxxGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJuYW1lXCI6XCJiZXNzaWUtcmVjcnVpdFwiLFwiYWdlXCI6MTAwMSxcImlkXCI6MTh9In0.RvMV8lbcxRtakX-Mn7ImUszzMftZgLAEfpUcACCBg3A";


        // 1. 对秘钥进行base64编码
        String base64 = new BASE64Encoder().encode(USER_KEY.getBytes());
        System.out.println(base64);

        // 2. 对base64生成秘钥对象，自动选择加密算法
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());

        // 3. 校验JWT
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();    // 构建jwt解析器
        // parse解析成功，获得claims，如果这里抛出异常，说明解析不通过，前端token被篡改了
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);      // 解析jwt

        String stuJson = claimsJws.getBody().getSubject();     // 获得主体body
        System.out.println(stuJson);
        Stu stu = new Gson().fromJson(stuJson, Stu.class);

        System.out.println(stu);
    }

}
