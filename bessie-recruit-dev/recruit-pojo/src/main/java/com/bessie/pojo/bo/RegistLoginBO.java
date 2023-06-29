package com.bessie.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.NotBlank; -> deprecated, 不能用了
import javax.validation.constraints.NotBlank;

/**
 * @program: bessie-recruit-dev
 * @description: 用于注册登录
 * @author: Bessie
 * @create: 2023-06-29 17:16
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegistLoginBO {
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机长度不正确")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    private String smsCode;
}
