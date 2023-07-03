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
    public static final String USER_KEY = "bessie_665544332211_112233445566"; //"bessie_123456789_123456789";

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
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJuYW1lXCI6XCJiZXNzaWUtcmVjcnVpdFwiLFwiYWdlXCI6MTAwMSxcImlkXCI6MTh9In0.RvMV8lbcxRtakX-Mn7ImUszzMftZgLAEfpUcACCBg3A";
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMTU0MTI5OTcyNDQ3ODY4MTA5MFwiLFwibW9iaWxlXCI6XCIxMzgxMjM0NTY3OFwiLFwibmlja25hbWVcIjpcIumjjumXtOW9seaciFwiLFwicmVhbE5hbWVcIjpcIueUqOaIt--8mjEzKioqKioqNjc4XCIsXCJzaG93V2hpY2hOYW1lXCI6MixcInNleFwiOjEsXCJmYWNlXCI6XCJodHRwOi8vMTIyLjE1Mi4yMDUuNzI6ODgvZ3JvdXAxL00wMC8wMC8wNS9DcG94eEY2WlV5U0FTTWJPQUFCQkFYaGpZMFk2NDkucG5nXCIsXCJlbWFpbFwiOlwiXCIsXCJiaXJ0aGRheVwiOntcInllYXJcIjoyMDEzLFwibW9udGhcIjoxLFwiZGF5XCI6MX0sXCJjb3VudHJ5XCI6XCLkuK3lm71cIixcInByb3ZpbmNlXCI6XCJcIixcImNpdHlcIjpcIlwiLFwiZGlzdHJpY3RcIjpcIlwiLFwiZGVzY3JpcHRpb25cIjpcIui_meWutuS8meW-iOaHku-8jOS7gOS5iOmDveayoeeVmeS4i35cIixcInN0YXJ0V29ya0RhdGVcIjp7XCJ5ZWFyXCI6MjAyMCxcIm1vbnRoXCI6MSxcImRheVwiOjF9LFwicG9zaXRpb25cIjpcIuW6leWxgueggeWGnDJcIixcInJvbGVcIjoyLFwiaHJJbldoaWNoQ29tcGFueUlkXCI6XCIxNTQxMzAwNDk1NjM5MTc1MTY5XCIsXCJoclNpZ25hdHVyZVwiOlwi5ZOI5ZOI5ZOIXCIsXCJoclRhZ3NcIjpcIizotYTmt7HmnrbmnoTluIgs5Zui6Zif6LSf6LSj5Lq6LOebruagh-euoeeQhizmiYHlubPljJbnrqHnkIYs5ri45oiP5a6FLFwiLFwiY3JlYXRlZFRpbWVcIjp7XCJkYXRlXCI6e1wieWVhclwiOjIwMjIsXCJtb250aFwiOjYsXCJkYXlcIjoyN30sXCJ0aW1lXCI6e1wiaG91clwiOjEzLFwibWludXRlXCI6NTcsXCJzZWNvbmRcIjo0NSxcIm5hbm9cIjowfX0sXCJ1cGRhdGVkVGltZVwiOntcImRhdGVcIjp7XCJ5ZWFyXCI6MjAyMixcIm1vbnRoXCI6NyxcImRheVwiOjI2fSxcInRpbWVcIjp7XCJob3VyXCI6MTYsXCJtaW51dGVcIjoxNCxcInNlY29uZFwiOjIwLFwibmFub1wiOjB9fX0iLCJleHAiOjE2ODgzOTEwNTd9.T-L6KbNoiGMIKWfCDWvNSNNVcK8DOpANxVgFy7Bu5Qo";
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
