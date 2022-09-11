package com.zhuo.seckill.vo;

import com.zhuo.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 注册参数
 */
@Data
public class RegisterVo {
    @NotNull // 不能为null
    @IsMobile // 自定义注解, 进行手机号校验
    private String mobile;

    @NotNull
    private String nickname;

    @NotNull // 不能为null
    @Length(min=32) // 最少32位
    private String password;
}
