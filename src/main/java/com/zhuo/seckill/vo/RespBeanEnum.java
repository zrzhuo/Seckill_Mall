package com.zhuo.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    // 通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),
    ACCESS_LIMIT(500201,"未登录"),

    //注册模块
    REPEAT_MOBILE(500101, "手机号重复"),
    REGISTER_ERROR(500102, "注册失败"),

    // 登录模块
    LOGIN_ERROR(500210, "用户名或密码错误"),
    MOBILE_ERROR(500211, "手机号格式错误"),
    BIND_ERROR(500212, "参数校验异常"),

    // 秒杀模块
    EMPTY_STOCK(500510,"库存不足") ,
    REPEAT_ERROR(500511,"该商品每人仅限一件"),
    ERROR_PATH(500512, "路径错误"),
    ERROR_CAPTCHA(500513,"验证码错误");


    private final Integer code;
    private final String message;
}
