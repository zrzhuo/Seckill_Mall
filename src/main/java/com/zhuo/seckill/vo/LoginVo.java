package com.zhuo.seckill.vo;

import com.zhuo.seckill.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 登录参数
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {

    @NotNull // 不能为null
    @IsMobile // 自定义注解, 进行手机号校验
    private String mobile;

    @NotNull // 不能为null
    @Length(min=32) // 最少32位
    private String password;

}
