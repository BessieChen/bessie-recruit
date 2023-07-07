package com.bessie.test;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-07 09:19
 **/
@SpringBootTest
public class JasyptTest {


    @Test
    public void testPwdEncrypt() {
        // 实例化加密器
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        // 配置加密算法和秘钥
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");    // 设置加密算法，默认的
        config.setPassword("bchen372bc2535");      // 用于加密的秘钥（盐），可以是随机字符串，一定要记住并且存储好
        encryptor.setConfig(config);

        // 对自己的密码进行加密
        //String myPwd = "bessie";
        String myPwd = "Chen@0921";
        //String myPwd = "guest";
        String encryptedPwd = encryptor.encrypt(myPwd);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("+ 原密码为：" + myPwd);
        System.out.println("+ 加密后的密码为：" + encryptedPwd);
        System.out.println("++++++++++++++++++++++++++++++");

        // 备注：此方式也可以用于账号密码登录的加盐操作
    }

//    bg4mklHlRGmEUrhSr2hoCA==
//    EkSpYxuOam5DntjuV/0K3w==
//    PGUSPogsj7txKyfrjwr8lA==
//    C8KPXXi1xf0BW2RJM2RHzw==


    @Test
    public void testPwdDecrypt() {
        // 实例化加密器
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        // 配置加密算法和秘钥
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");    // 设置加密算法，默认的
        config.setPassword("bchen372bc2535");      // 用于加密的秘钥（盐），可以是随机字符串，一定要记住并且存储好
        encryptor.setConfig(config);

        String pendingPwd = "C8KPXXi1xf0BW2RJM2RHzw==";
        String myPwd = encryptor.decrypt(pendingPwd);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("+ 解密后的密码为：" + myPwd);
        System.out.println("++++++++++++++++++++++++++++++");

    }
}